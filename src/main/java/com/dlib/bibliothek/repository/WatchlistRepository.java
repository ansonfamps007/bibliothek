package com.dlib.bibliothek.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dlib.bibliothek.model.Book;
import com.dlib.bibliothek.model.Watch;

@Repository
public interface WatchlistRepository extends CrudRepository<Watch, Integer> {

	void deleteByBookId(int bookId);

	boolean existsByBookId(int bookId);

	Optional<Watch> findByBookId(int bookId);

	int deleteByBookIdAndUserId(int bookId, Integer id);

	@Query("SELECT b FROM Book b inner join Watch w on (b.id = w.book) WHERE w.user.id = :user_id")
	List<Book> findAllWatchlistedBooks(@Param("user_id") Integer userId);

	@Query("SELECT w FROM Watch w where w.book.id = :book_id")
	List<Watch> findAllWatchlist(@Param("book_id") Integer bookId);

	boolean existsByBookIdAndUserId(int bookId, int userId);

	@Query(value = "select count(1) from watch where book_id = :book_id and user_id = :user_id", nativeQuery = true)
	int isWatchlistedBook(@Param("book_id") int bookId, @Param("user_id") int userId);

}
