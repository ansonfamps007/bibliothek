package com.dlib.bibliothek.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.dlib.bibliothek.exception.ValidationException;
import com.dlib.bibliothek.model.Book;
import com.dlib.bibliothek.model.User;
import com.dlib.bibliothek.model.Watch;
import com.dlib.bibliothek.repository.BookRepository;
import com.dlib.bibliothek.repository.UserRepository;
import com.dlib.bibliothek.repository.WatchlistRepository;
import com.dlib.bibliothek.response.BookResponse;
import com.dlib.bibliothek.service.WatchlistService;
import com.dlib.bibliothek.util.ApiConstants;
import com.dlib.bibliothek.util.BookUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WatchlistServiceImpl implements WatchlistService {

	@Autowired
	private WatchlistRepository watchlistRepository;

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BookUtil bookUtil;

	@Override
	public boolean existsByBookId(int bookId) {
		return watchlistRepository.existsByBookId(bookId);
	}

	@Override
	public void addToWatchlist(int bookId, String userName) {

		Optional<User> userObj = userRepository.findByUsername(userName);

		if (userObj.isPresent()) {
			if (isWatchListed(bookId, userObj.get().getId())) {
				throw new ValidationException("This book is already added in your watch list !");
			} else {
				Optional<Book> bookObj = bookRepository.findById(bookId);
				if (bookObj.isPresent()) {
					Book book = bookObj.get();
					Watch watch = new Watch();
					watch.setBook(book);
					watch.setUser(userObj.get());
					watchlistRepository.save(watch);
				} else {
					throw new ValidationException(ApiConstants.NO_BOOK);
				}
			}

		} else {
			throw new ValidationException(ApiConstants.NO_USER_DATA);
		}
	}

	@Override
	@Transactional
	public int removeFromWatchlist(int bookId, String userName) {

		try {
			Optional<User> userObj = userRepository.findByUsername(userName);
			if (userObj.isPresent()) {
				return watchlistRepository.deleteByBookIdAndUserId(bookId, userObj.get().getId());
			} else {
				throw new ValidationException(ApiConstants.NO_USER_DATA);
			}
		} catch (Exception ex) {
			log.debug(" Deletion failed : ", ex.getCause() + " - " + ex.getMessage());
			return 0;
		}
	}

	@Override
	public List<BookResponse> getWatchlistedBooks(String userName) {
		try {
			Optional<User> userObj = userRepository.findByUsername(userName);
			if (userObj.isPresent()) {
				List<Book> booksWatchlisted = watchlistRepository.findAllWatchlistedBooks(userObj.get().getId());
				List<BookResponse> bookResponseList = new ArrayList<>();
				booksWatchlisted.forEach(book -> {
					bookResponseList.add(bookUtil.mapBookResponse(book, userObj.get().getId()));
				});
				return bookResponseList;
			}

		} catch (Exception e) {
			log.debug(" getWatchlistedBooks failed : ", e.getCause() + " - " + e.getMessage());
		}
		return new ArrayList<>();
	}

	@Override
	public List<Watch> getWatchlist(int bookId) {
		return watchlistRepository.findAllWatchlist(bookId);
	}

	@Override
	public boolean isWatchListed(int bookId, int userId) {
		return watchlistRepository.existsByBookIdAndUserId(bookId, userId);
	}

	@Override
	@Async
	public void returnNotification(int bookId) {
		List<Watch> watchList = this.getWatchlist(bookId);
		if (!CollectionUtils.isEmpty(watchList)) {
			watchList.forEach(watch -> {
				try {
					Book book = watch.getBook();
					JSONObject data = new JSONObject();
					data.put("type", "WATCHLIST_ALERT");
					data.put("message", "Hey, Book " + book.getTitle() + " is available right now!!");
					data.put("book_id", book.getId());
					bookUtil.sendNotification(watch.getUser().getFcmId(), data);
				} catch (Exception e) {
					log.debug("Error in returnNotification - " + e.getMessage() + "- " + e.getCause());
				}
			});
		}
	}

}
