
package com.dlib.bibliothek.repository;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author anson.f
 * 
 */
@Repository
@Slf4j
public class BookIssueRepositoryImpl implements BookIssueRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@Transactional
	public int takeRenewBook(int bookId, int userId, LocalDateTime issueDate, LocalDateTime returnDate,
			String userBookStatus) {
		try {
			log.debug("BookIssueRepositoryImpl : issueBook ");
			return entityManager.createNativeQuery(
					"insert into book_issue_details (book_id , user_id, status, issued_at, return_dt) values (:bookId , :userId, :userBookStatus, :issueDate, :returnDate)")
					.setParameter("bookId", bookId).setParameter("userId", userId).setParameter("issueDate", issueDate)
					.setParameter("userBookStatus", userBookStatus).setParameter("returnDate", returnDate)
					.executeUpdate();
		} catch (Exception ex) {
			log.debug("issueBook - Error in BookIssueRepositoryImpl : "
					+ (null != ex.getCause() ? ex.getCause().getCause() : ex) + " " + ex.getMessage());
			return 0;
		}
	}

	@Override
	@Transactional
	public int returnBook(int bookId, int userId, LocalDateTime returnedOn) {
		try {
			log.debug("BookIssueRepositoryImpl : returnBook ");
			return entityManager.createNativeQuery(
					"insert into book_issue_details (book_id , user_id, status, returned_on) values (:bookId , :userId, :userBookStatus, :returnedOn)")
					.setParameter("bookId", bookId).setParameter("userId", userId)
					.setParameter("userBookStatus", "RETURNED").setParameter("returnedOn", returnedOn).executeUpdate();
		} catch (Exception ex) {
			log.debug("returnBook - Error in BookIssueRepositoryImpl : "
					+ (null != ex.getCause() ? ex.getCause().getCause() : ex) + " " + ex.getMessage());
			return 0;
		}
	}

	@Override
	@Transactional
	public int deleteBookIssue(int bookId, int userId) {
		try {
			log.debug("BookIssueRepositoryImpl : returnBook ");
			return entityManager
					.createNativeQuery("delete from book_issue_details where book_id=:bookId and user_id= :userId")
					.setParameter("bookId", bookId).setParameter("userId", userId).executeUpdate();
		} catch (Exception ex) {
			log.debug("deleteBookIssue - Error in BookIssueRepositoryImpl : "
					+ (null != ex.getCause() ? ex.getCause().getCause() : ex) + " " + ex.getMessage());
			return 0;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Object> findMyBooks(int userId, int returnLimit, Pageable pageable) {
		try {
			log.debug("BookIssueRepositoryImpl : returnBook ");
			return entityManager.createNativeQuery(
					"SELECT b., DATEDIFF(details.return_dt, now()) AS days_to_return, case when DATEDIFF(details.return_dt, "
							+ " now())<=:returnLimit then true else false end as is_renewable  FROM book b inner join book_issue_details "
							+ "details on(b.id = details.book_id)  where details.returned_on is null "
							+ " and details.user_id = :userId and b.is_approved = 1 and b.is_deleted = 0 ",
					"BookMapping").setParameter("userId", userId).setParameter("returnLimit", returnLimit)
					.getResultList();
		} catch (Exception ex) {
			log.debug("findMyBooks - Error in BookIssueRepositoryImpl : "
					+ (null != ex.getCause() ? ex.getCause().getCause() : ex) + " " + ex.getMessage());
			return new ArrayList<>();
		}
	}

	@Override
	public Integer getMyBooksCount(int userId) {
		try {
			log.debug("BookIssueRepositoryImpl : returnBook ");
			return ((BigInteger) entityManager
					.createNativeQuery(
							"select count() from book_issue_details where user_id = :userId and status!='RETURNED' ")
					.setParameter("userId", userId).getSingleResult()).intValue();
		} catch (Exception ex) {
			log.debug("returnBook - Error in BookIssueRepositoryImpl : "
					+ (null != ex.getCause() ? ex.getCause().getCause() : ex) + " " + ex.getMessage());
			return 0;
		}
	}

	@Override
	@Transactional
	public int deleteByBookId(int bookId) {
		try {
			log.debug("BookIssueRepositoryImpl : deleteByBookId ");
			return entityManager.createNativeQuery("delete from book_issue_details where book_id=:bookId ")
					.setParameter("bookId", bookId).executeUpdate();
		} catch (Exception ex) {
			log.debug("deleteByBookId - Error in BookIssueRepositoryImpl : "
					+ (null != ex.getCause() ? ex.getCause().getCause() : ex) + " " + ex.getMessage());
			return 0;
		}
	}
}
