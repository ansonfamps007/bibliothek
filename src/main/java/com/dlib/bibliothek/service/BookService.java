
package com.dlib.bibliothek.service;

import org.springframework.web.multipart.MultipartFile;

import com.dlib.bibliothek.model.Book;
import com.dlib.bibliothek.request.BookForm;
import com.dlib.bibliothek.response.BookResponse;
import com.dlib.bibliothek.response.Data;
//import com.google.zxing.WriterException;

public interface BookService {

	Data getFeedBooks(String interval, int pageId, int itemsPerPage, String userName);

	boolean existsByBookId(int id);

	void deleteBook(int id);

	BookResponse getBookById(int id, String userName);

	void updateBook(BookForm bookForm);

	void changeState(int bookId, String userName, int isApproved);

	Book addBook(BookForm bookForm, String userName);

	Data loadDonateBook();

	int isBookAvailable(int bookId);

	//String generateQRCodeImage(Integer id, int width, int height) /* throws WriterException */;

	Data getNotifications(String userName, int pageNo, int itemsPerPage);

	String saveCoverImage(MultipartFile uploadImage, int bookId);

	//void generatePdf();

	Data getBookByKeyword(int page, String keyword, int itemsPerPage, String userName);

	String saveCoverImageThumb(MultipartFile uploadImage, int bookId);

}
