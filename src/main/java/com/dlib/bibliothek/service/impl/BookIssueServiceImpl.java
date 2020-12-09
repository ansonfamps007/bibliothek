
package com.dlib.bibliothek.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.dlib.bibliothek.exception.ValidationException;
import com.dlib.bibliothek.model.Book;
import com.dlib.bibliothek.model.BookReturnResponse;
import com.dlib.bibliothek.model.User;
import com.dlib.bibliothek.repository.BookIssueRepository;
import com.dlib.bibliothek.repository.BookRepository;
import com.dlib.bibliothek.repository.UserRepository;
import com.dlib.bibliothek.response.BookResponse;
import com.dlib.bibliothek.service.BookIssueService;
import com.dlib.bibliothek.util.ApiConstants;
import com.dlib.bibliothek.util.BookUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BookIssueServiceImpl implements BookIssueService {

	@Value("${app.book-return-days}")
	private int returnDays;

	@Value("${app.return-limit}")
	private String returnLimit;

	@Value("${app.book-taken-limit}")
	private String bookTakenLimit;

	@Autowired
	private BookIssueRepository bookIssueRepository;

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BookUtil bookUtil;

	@Override
	@Transactional
	public String takeRenewBook(int bookId, String userName, boolean isRenew) {
		int userId = getUserId(userName);
		log.debug("BookActivityServiceImpl : issueBook {} ");

		if (!isRenew && bookIssueRepository.getMyBooksCount(userId) >= Integer.valueOf(bookTakenLimit)) {
			throw new ValidationException("Sorry, you can't take more than " + bookTakenLimit + " book !");
		}

		boolean isBookAvailable = bookRepository.isBookAvailable(bookId) > 0 ? Boolean.TRUE : Boolean.FALSE;
		if (!isBookAvailable && !isRenew) {
			throw new ValidationException("Sorry, this book is already taken or expired !");
		} else {
			int flag = bookIssueRepository.deleteBookIssue(bookId, userId);
			LocalDateTime issueDate = LocalDateTime.now();
			LocalDateTime returnDate = issueDate.plusDays(returnDays);
			String returnStrDate = returnDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));
			String responseMsg = null;
			if (isRenew) {
				if (flag == 0) {
					throw new ValidationException("Sorry, you can't renew new book");
				} else {
					if (bookIssueRepository.takeRenewBook(bookId, userId, issueDate, returnDate, "RENEWED") > 0) {
						responseMsg = "Book renewed. Your next renewal date is " + returnStrDate;
					}
				}

			} else {
				if (bookIssueRepository.takeRenewBook(bookId, userId, issueDate, returnDate, "TAKEN") > 0) {
					responseMsg = "Book issued. Your return date is " + returnStrDate;
				}
			}
			if (null != responseMsg) {
				bookRepository.updateBookStatus(bookId, "TAKEN");
			}
			return responseMsg;
		}

	}

	@Override
	@Transactional
	public int returnBook(int bookId, String userName) {

		log.debug("BookActivityServiceImpl : returnBook {} ");
		LocalDateTime returnedOn = LocalDateTime.now();
		int userId = getUserId(userName);
		bookIssueRepository.deleteBookIssue(bookId, userId);
		if (bookIssueRepository.returnBook(bookId, userId, returnedOn) > 0) {
			return bookRepository.updateBookStatus(bookId, "AVAILABLE");
		}
		return 0;
	}

	@Override
	public List<BookResponse> getMyBooks(String userName) {

		Optional<User> userObj = userRepository.findByUsername(userName);
		if (userObj.isPresent()) {
			Pageable pageable = PageRequest.of(1, 10);
			List<Object> bookList = bookIssueRepository.findMyBooks(userObj.get().getId(), Integer.valueOf(returnLimit),
					pageable);
			List<BookResponse> books = new ArrayList<>();
			if (!CollectionUtils.isEmpty(bookList)) {
				bookList.forEach(book -> {
					Object[] bookObj = (Object[]) book;
					BookResponse bookResponse = bookUtil.mapBookResponse((Book) bookObj[0], userObj.get().getId());
					bookResponse.setDaysToReturn(Integer.valueOf("" + bookObj[1]));
					bookResponse.setIsRenewable(Boolean.valueOf("" + bookObj[2]));
					books.add(bookResponse);
				});
				return books;
			} else {
				throw new ValidationException(ApiConstants.NO_DATA);
			}
		} else {
			throw new ValidationException(ApiConstants.NO_USER_DATA);
		}
	}

	private int getUserId(String userName) {
		Optional<User> userObj = userRepository.findByUsername(userName);
		if (userObj.isPresent()) {
			return userObj.get().getId();
		} else {
			throw new ValidationException(ApiConstants.NO_USER_DATA);
		}
	}

	@Override
	@Async
	public void sendReturnNotification() {
		List<BookReturnResponse> returnList = bookRepository.findAllReturnAlert(Integer.valueOf(returnLimit));
		if (!CollectionUtils.isEmpty(returnList)) {
			returnList.forEach(list -> {
				try {
					JSONObject data = new JSONObject();
					data.put("type", "RETURN_ALERT");
					if (list.getDaysToReturn() < 0) {
						data.put("message", "Hey, Book- " + list.getTitle() + " overdue by "
								+ Math.abs(list.getDaysToReturn()) + " days, please return !");
					} else if (list.getDaysToReturn() == 0) {
						data.put("message", "Hey, Book- " + list.getTitle()
								+ " return date expiring today. Please renew/return !");
					} else {
						data.put("message", "Hey, You have only " + list.getDaysToReturn()
								+ " days remaining to return the book - " + list.getTitle());
					}
					bookUtil.sendNotification(list.getFcmId(), data);
				} catch (Exception e) {
					log.debug("Error in returnNotification - " + e.getMessage() + "- " + e.getCause());
				}
			});
		}
	}
}
