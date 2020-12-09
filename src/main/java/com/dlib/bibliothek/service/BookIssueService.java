
package com.dlib.bibliothek.service;

import java.util.List;

import com.dlib.bibliothek.response.BookResponse;

public interface BookIssueService {

	String takeRenewBook(int bookId, String userName, boolean b);

	int returnBook(int bookId, String userName);
	
	List<BookResponse> getMyBooks(String userName);

	void sendReturnNotification();
}
