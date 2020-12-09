
package com.dlib.bibliothek.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dlib.bibliothek.exception.ValidationException;
import com.dlib.bibliothek.response.ApiResponse;
import com.dlib.bibliothek.response.BookResponse;
import com.dlib.bibliothek.response.Data;
import com.dlib.bibliothek.service.BookIssueService;
import com.dlib.bibliothek.service.WatchlistService;
import com.dlib.bibliothek.util.ApiConstants;
import com.dlib.bibliothek.util.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/book")
@Slf4j
public class BookIssueController {

	@Autowired
	private BookIssueService bookIssueService;
	
	@Autowired
	private WatchlistService watchlistService;

	@Autowired
	private JwtUtil jwtUtil;

	@PostMapping(value = "/take_book/{book_id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ApiResponse takeBook(@PathVariable(value = "book_id", required = true) int bookId,
			HttpServletRequest request) {

		log.debug("BookIssueController : issueBook {} ");
		String jwt = jwtUtil.getJwt(request);
		String userName = (null != jwt) ? jwtUtil.retriveUsernameFromJsonString(jwtUtil.getDataFromToken(jwt)) : null;
		String responseMsg = bookIssueService.takeRenewBook(bookId, userName, false);
		return ApiResponse.builder().error(false).message(responseMsg).build();
	}

	@PostMapping(value = "/return_book/{book_id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ApiResponse returnBook(@PathVariable(value = "book_id", required = true) int bookId,
			HttpServletRequest request) {
		String jwt = jwtUtil.getJwt(request);
		String userName = (null != jwt) ? jwtUtil.retriveUsernameFromJsonString(jwtUtil.getDataFromToken(jwt)) : null;
		log.debug("BookIssueController : returnBook {} ");
		if (bookIssueService.returnBook(bookId, userName) > 0) {
			watchlistService.returnNotification(bookId);
			return ApiResponse.builder().error(false).message("Book returned successfully.").build();
		} else {
			throw new ValidationException("Book return failed !");
		}
	}

	@PostMapping(value = "/renew_book/{book_id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ApiResponse renewBook(@PathVariable(value = "book_id", required = true) int bookId,
			HttpServletRequest request) {

		log.debug("BookIssueController : renewBook {} ");
		String jwt = jwtUtil.getJwt(request);
		String userName = (null != jwt) ? jwtUtil.retriveUsernameFromJsonString(jwtUtil.getDataFromToken(jwt)) : null;
		String responseMsg = bookIssueService.takeRenewBook(bookId, userName, true);
		return ApiResponse.builder().error(false).message(responseMsg).build();
	}

	@GetMapping(value = "/get_my_books", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ApiResponse getMyBooks(HttpServletRequest request) {
		String jwt = jwtUtil.getJwt(request);
		String userName = (null != jwt) ? jwtUtil.retriveUsernameFromJsonString(jwtUtil.getDataFromToken(jwt)) : null;
		List<BookResponse> bookList = bookIssueService.getMyBooks(userName);
		if (!CollectionUtils.isEmpty(bookList)) {
			Data bookData = new Data();
			bookData.setBooks(bookList);
			return ApiResponse.builder().error(false).message("OK").data(bookData).build();
		} else {
			throw new ValidationException(ApiConstants.NO_BOOK);
		}
	}
	
	//@Scheduled(cron = "0 0 9,17   ")
	public void sendReturnNotification() {
		bookIssueService.sendReturnNotification();
	}
}
