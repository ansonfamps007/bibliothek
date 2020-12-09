
package com.dlib.bibliothek.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

/**
 * 
  @author anson.f
 
 */
@Repository
public interface BookIssueRepository {

	int returnBook(int bookId, int userId, LocalDateTime returnedOn);

	int takeRenewBook(int bookId, int userId, LocalDateTime issueDate, LocalDateTime returnDate, String string);

	List<Object> findMyBooks(int userId, int returnLimit, Pageable pageable);

	int deleteBookIssue(int bookId, int userId);

	Integer getMyBooksCount(int userId);

	int deleteByBookId(int bookId);

}
