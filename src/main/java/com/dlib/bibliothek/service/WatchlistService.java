package com.dlib.bibliothek.service;

import java.util.List;

import com.dlib.bibliothek.model.Watch;
import com.dlib.bibliothek.response.BookResponse;

public interface WatchlistService {

	boolean existsByBookId(int bookId);
	
	void addToWatchlist(int bookId,String userName);

	int removeFromWatchlist(int bookId, String userName);

	List<BookResponse> getWatchlistedBooks(String userName);

	boolean isWatchListed(int bookId, int userId);

	List<Watch> getWatchlist(int bookId);

	void returnNotification(int bookId);

}
