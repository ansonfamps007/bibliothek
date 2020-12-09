
package com.dlib.bibliothek.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.dlib.bibliothek.model.Book;
import com.dlib.bibliothek.model.BookReturnResponse;

/**
 * 
 * @author anson.f
 * 
 */
public interface BookRepository extends CrudRepository<Book, Integer> {

	@Query(value = "select book.id, book.status, book.cover_url, book.cover_thumb_url, book.title, book.author_id, book.language_id, book.category_id, "
			+ "book.donated_id, book.created_at, "
			+ "'WATCH_LIST' as feed_type, book.is_approved, book.is_deleted, book.qr_code, book.book_desc, cast(book.expiry_date as char) from book book "
			+ " inner join watch watch "
			+ "on(book.id = watch.book_id) where book.status = 'AVAILABLE' and watch.user_id=:userId and book.is_approved = 1 and book.is_deleted = 0  "
			+ " and (book.expiry_date > now() or book.expiry_date is null) group by book.id " + "union "
			+ "select book.id, book.status, book.cover_url, book.cover_thumb_url, book.title, book.author_id, book.language_id, book.category_id, "
			+ "book.donated_id, book.created_at,"
			+ "'NEW_BOOK' as feed_type, book.is_approved, book.is_deleted, book.qr_code, book.book_desc, cast(book.expiry_date as char) from book book where "
			+ "book.created_at between now() - interval :interval day and now() and book.is_approved = 1 and book.is_deleted = 0  "
			+ " and (expiry_date > now() or expiry_date is null) and book.id not in "
			+ " (select book.id from book book inner join watch watch on(book.id = watch.book_id) where book.status = 'AVAILABLE' "
			+ " and book.is_approved = 1 and book.is_deleted = 0 and watch.user_id=:userId group by book.id order by book.created_at desc ) "
			+ " order by created_at desc", countQuery = "select ( "
					+ "  select count() from book book inner join watch watch on(book.id = watch.book_id) where book.status = 'AVAILABLE' "
					+ " and watch.user_id=:userId and book.is_approved = 1 and book.is_deleted = 0 and (expiry_date > now() or expiry_date is null) ) "
					+ " + " + " (select count() from book book where "
					+ " book.created_at between now() - interval :interval day and now() and "
					+ "book.is_approved = 1 and book.is_deleted = 0 and book.id not in (select book.id from book book inner join watch "
					+ " watch on(book.id = watch.book_id) where book.status = 'AVAILABLE' and book.is_approved = 1 and book.is_deleted = 0  "
					+ " and watch.user_id=:userId group by book.id )) as sub ", nativeQuery = true)
	Page<Object[]> findAllFeedBooks(@Param("interval") int interval, Pageable pageable, @Param("userId") int userId);

	@Query(value = "select  from book where title like :keyword and is_deleted = 0 order by created_at desc", nativeQuery = true)
	Page<Book> findAllBooksByTitle(Pageable pageable, @Param("keyword") String keyword);

	@Query(value = "select  from book where title like :keyword and is_deleted = 0 and is_approved = 1 and (expiry_date > now() or expiry_date is null) "
			+ " order by created_at desc", nativeQuery = true)
	Page<Book> findAllBooksByTitleUser(Pageable pageable, @Param("keyword") String keyword);

	@Modifying(clearAutomatically = true)
	@Query(value = "update book set is_approved=:is_approved where id=:book_id", nativeQuery = true)
	void updateState(@Param("book_id") int bookId, @Param("is_approved") int isApproved);

	@Modifying(clearAutomatically = true)
	@Query(value = "update book set status=:status where id=:book_id", nativeQuery = true)
	int updateBookStatus(@Param("book_id") int bookId, @Param("status") String status);

	@Query(value = "select case when status = 'AVAILABLE' and (expiry_date > now() or expiry_date is null) then 1 else 0 end "
			+ " from book where id = :book_id", nativeQuery = true)
	int isBookAvailable(@Param("book_id") int bookId);

	@Query(value = "select b.id, b.cover_url, b.cover_thumb_url, b.title, b.author_id, b.language_id, b.category_id, b.status, false , "
			+ "b.book_desc, b.is_approved, b.created_at,  "
			+ " DATEDIFF(details.return_dt, now()) AS daysToReturn, 'RETURN_ALERT' as notify_type, cast(b.expiry_date as char) from book b inner "
			+ " JOIN book_issue_details details ON details.book_id = b.id"
			+ " WHERE details.status!= 'RETURNED' and details.user_id = :user_id and b.is_approved = 1 and b.is_deleted = 0 and "
			+ " DATEDIFF(return_dt ,  now())<=:returnLimit" + " union "
			+ " select b.id, b.cover_url, b.cover_thumb_url, b.title, b.author_id, b.language_id, b.category_id, b.status, false, b.book_desc, "
			+ "b.is_approved, b.created_at, "
			+ " null AS daysToReturn, 'AVAILABLE_ALERT' as notify_type, cast(b.expiry_date as char) from book b INNER JOIN watch w ON (w.book_id = b.id) "
			+ " WHERE b.status = 'AVAILABLE' and w.user_id = :user_id and b.is_approved = 1 and b.is_deleted = 0 "
			+ " and (expiry_date > now() or expiry_date is null)", nativeQuery = true)
	Page<Object[]> findBooksForNotification(@Param("user_id") Integer id, Pageable pageable,
			@Param("returnLimit") int returnLimit);

	@Modifying(clearAutomatically = true)
	@Query(value = "update book set cover_url=:cover_url where id=:book_id", nativeQuery = true)
	int uploadCoverUrl(@Param("cover_url") String coverUrl, @Param("book_id") int bookId);

	@Modifying(clearAutomatically = true)
	@Query(value = "update book set cover_thumb_url=:cover_thumb_url where id=:book_id", nativeQuery = true)
	int uploadCoverThumpUrl(@Param("cover_thumb_url") String coverThumbUrl, @Param("book_id") int bookId);

	@Query(value = "SELECT  FROM book where is_approved = 1 and is_deleted = 0", nativeQuery = true)
	List<Book> findAllActiveBooks();

	@Modifying(clearAutomatically = true)
	@Query(value = "delete from book where id=:book_id", nativeQuery = true)
	void deleteBookById(@Param("book_id") int bookId);

	@Query(value = "select distinct name from users u inner join book_issue_details issue on(u.id= issue.user_id and "
			+ "issue.status ='TAKEN' and issue.book_id = :book_id) ", nativeQuery = true)
	String findBookTakenBy(@Param("book_id") int bookId);

	@Query(value = "select b.title as title, u.fcm_id as fcmId, DATEDIFF(details.return_dt, now()) AS daysToReturn from book b inner JOIN book_issue_details details "
			+ " ON (details.book_id = b.id and details.status!= 'RETURNED' and b.is_approved = 1 and DATEDIFF(return_dt ,  now())<=:returnLimit) "
			+ " inner join users u on (details.user_id = u.id)", nativeQuery = true)
	List<BookReturnResponse> findAllReturnAlert(@Param("returnLimit") int returnLimit);

	@Query("select u from Book u where u.id = 209")
	List<Book> findAllUserBooks();

}
