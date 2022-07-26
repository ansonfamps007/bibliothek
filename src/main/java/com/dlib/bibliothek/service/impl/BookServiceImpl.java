
package com.dlib.bibliothek.service.impl;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.dlib.bibliothek.exception.ValidationException;
import com.dlib.bibliothek.model.Author;
import com.dlib.bibliothek.model.Book;
import com.dlib.bibliothek.model.Category;
import com.dlib.bibliothek.model.Language;
import com.dlib.bibliothek.model.User;
import com.dlib.bibliothek.repository.AuthorRepository;
import com.dlib.bibliothek.repository.BookIssueRepository;
import com.dlib.bibliothek.repository.BookRepository;
import com.dlib.bibliothek.repository.CategoryRepository;
import com.dlib.bibliothek.repository.LanguageRepository;
import com.dlib.bibliothek.repository.UserRepository;
import com.dlib.bibliothek.repository.WatchlistRepository;
import com.dlib.bibliothek.request.BookForm;
import com.dlib.bibliothek.response.BookDto;
import com.dlib.bibliothek.response.BookResponse;
import com.dlib.bibliothek.response.Data;
import com.dlib.bibliothek.response.FeedItems;
import com.dlib.bibliothek.response.Meta;
import com.dlib.bibliothek.response.Notifications;
import com.dlib.bibliothek.service.BookService;
import com.dlib.bibliothek.util.ApiConstants;
import com.dlib.bibliothek.util.BookUtil;
/*import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;*/
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BookServiceImpl implements BookService {

	@Value("${app.default-cover-url}")
	private String defaultCoverurl;

	@Value("${app.default-cover-thumb-url}")
	private String defaultCoverThumbUrl;

	@Value("${app.cover-image-path}")
	private String coverImagePath;

	@Value("${app.cover-thumb-image-path}")
	private String coverThumbImagePath;

	@Value("${app.api-host}")
	private String host;

	@Value("${app.qr-code-image-path}")
	private String qrCodeImagePath;

	@Value("${app.qr-code-pdf-path}")
	private String qrCodePdfPath;

	@Value("${app.return-limit}")
	private String returnLimit;

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private LanguageRepository languageRepository;

	@Autowired
	private AuthorRepository authorRepository;

	@Autowired
	private WatchlistRepository watchlistRepository;

	@Autowired
	private BookUtil bookUtil;

	@Autowired
	private BookIssueRepository bookIssueRepository;

	@Override
	public Data getFeedBooks(String interval, int pageId, int itemsPerPage, String userName) {
		try {
			Optional<User> userObj = userRepository.findByUsername(userName);
			Pageable pageable = PageRequest.of(pageId <= 0 ? 0 : pageId - 1, itemsPerPage);
			List<FeedItems> feedList = new ArrayList<>();
			Page<Object[]> feedBookPage = bookRepository.findAllFeedBooks(Integer.valueOf(interval), pageable,
					userObj.get().getId());
			if (!ObjectUtils.isEmpty(feedBookPage) && !ObjectUtils.isEmpty(feedBookPage.getContent())) {
				log.debug("BookServiceImpl : getFeedBooks - feedBookPage " + feedBookPage);
				feedBookPage.getContent().forEach(feedBook -> {
					BookResponse bookResponse = mapFeedBookResponse(feedBook, userObj.get().getId());
					feedList.add(FeedItems.builder().type(feedBook[10]).book(bookResponse).build());
				});
				if (!ObjectUtils.isEmpty(feedList)) {
					Data feedData = new Data();
					feedData.setMeta(Meta.builder().totalPages(feedBookPage.getTotalPages())
							.itemsPerPage(feedBookPage.getSize()).totalItems(feedBookPage.getTotalElements()).build());
					feedData.setFeedItems(feedList);
					return feedData;
				} else {
					throw new ValidationException(ApiConstants.NO_DATA);
				}
			} else {
				throw new ValidationException(ApiConstants.NO_DATA);
			}
		} catch (Exception ex) {
			log.debug("BookServiceImpl : getFeedBooks - Exception {} ",
					ex.getCause() + " - " + (null != ex.getCause() ? ex.getCause().getCause() : ex));
			throw new ValidationException(ApiConstants.INVALID);
		}
	}

	private BookResponse mapFeedBookResponse(Object[] feedBook, int userId) {
		BookResponse bookResponse = new BookResponse();
		bookResponse.setId(Integer.valueOf("" + feedBook[0]));
		bookResponse.setStatus("" + feedBook[1]);
		bookResponse.setCoverUrl("" + feedBook[2]);
		bookResponse.setCoverThumbUrl("" + feedBook[3]);
		bookResponse.setTitle("" + feedBook[4]);

		if (null != feedBook[5]) {
			Optional<Author> authorObj = authorRepository.findById(Integer.valueOf("" + feedBook[5]));
			if (authorObj.isPresent()) {
				Author author = authorObj.get();
				bookResponse.setAuthor(author.getName());
				bookResponse.setAuthorId(author.getId());
			}
		}

		if (null != feedBook[6]) {
			Optional<Language> languageObj = languageRepository.findById(Integer.valueOf("" + feedBook[6]));
			if (languageObj.isPresent()) {
				Language language = languageObj.get();
				bookResponse.setLanguage(language.getName());
				bookResponse.setLanguageId(language.getId());
			}
		}

		if (null != feedBook[7]) {
			Optional<Category> categoryObj = categoryRepository.findById(Integer.valueOf("" + feedBook[7]));
			if (categoryObj.isPresent()) {
				Category category = categoryObj.get();
				bookResponse.setCategory(category.getName());
				bookResponse.setCategoryId(category.getId());
			}
		}

		if (null != feedBook[8]) {
			Optional<User> userObj = userRepository.findById(Integer.valueOf("" + feedBook[8]));
			if (userObj.isPresent()) {
				User user = userObj.get();
				bookResponse.setIsDonated(true);
				bookResponse.setDonatedBy(user.getName());
			}

		} else {
			bookResponse.setIsDonated(false);
		}

		if (null != feedBook[9]) {
			bookResponse.setCreatedAt(Timestamp.valueOf("" + feedBook[9]).getTime());
		}
		int isWatchList = watchlistRepository.isWatchlistedBook(Integer.valueOf("" + feedBook[0]), userId);
		bookResponse.setIsWatchListed(isWatchList > 0 ? Boolean.TRUE : Boolean.FALSE);
		bookResponse.setIsApproved(
				!StringUtils.isEmpty(feedBook[11]) && Integer.valueOf("" + feedBook[11]) == 1 ? Boolean.TRUE
						: Boolean.FALSE);
		bookResponse.setDescription("" + feedBook[14]);
		LocalDateTime currentDate = LocalDateTime.now();

		if (null != feedBook[15]) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			LocalDateTime formatDateTime = LocalDateTime.parse(String.valueOf(feedBook[15]), formatter);
			bookResponse.setExpiredAt(Timestamp.valueOf("" + feedBook[15]).getTime());
			boolean flag = currentDate.isAfter(formatDateTime) == Boolean.TRUE ? Boolean.TRUE : Boolean.FALSE;
			bookResponse.setIsExpired(flag);
		} else {
			bookResponse.setIsExpired(false);
		}

		return bookResponse;
	}

	@Override
	@Transactional
	public Book addBook(BookForm bookForm, String userName) {
		try {
			Optional<User> userObj = userRepository.findByUsername(userName);
			if (userObj.isPresent() && !StringUtils.isEmpty(userObj.get().getRole())) {
				User user = userObj.get();
				Book book = new Book();
				// isAddBook set true to identify book save
				book = mapBookRequest(bookForm, book, true);

				if (user.getRole().equalsIgnoreCase("user")) {
					book.setIsApproved(false);
					book.setDonated(user);
				} else if (user.getRole().equalsIgnoreCase("admin")) {
					book.setIsApproved(true);
				}
				return bookRepository.save(book);
			} else {
				log.debug("User is not authorized or no role found !");
				throw new ValidationException("User is not authorized");
			}
		} catch (Exception ex) {
			log.debug("BookServiceImpl : addbook - Exception {} ",
					ex.getMessage() + " - " + (null != ex.getCause() ? ex.getCause().getCause() : ex));
			throw new ValidationException(ApiConstants.INVALID + " - " + ex.getMessage() + " - "
					+ (null != ex.getCause() ? ex.getCause().getCause() : ex));
		}

	}

	@Override
	public void updateBook(BookForm bookForm) {
		try {
			Optional<Book> bookObj = bookRepository.findById(bookForm.getId());
			if (bookObj.isPresent()) {
				Book book = bookObj.get();
				// isAddBook set false to identify book update
				book = mapBookRequest(bookForm, book, false);
				bookRepository.save(book);
			} else {
				throw new ValidationException(ApiConstants.NO_DATA);
			}
		} catch (Exception ex) {
			log.debug("Update Book failed : ",
					ex.getMessage() + " - " + (null != ex.getCause() ? ex.getCause().getCause() : ex));
			throw new ValidationException(ApiConstants.INVALID + " - " + ex.getMessage() + " - "
					+ (null != ex.getCause() ? ex.getCause().getCause() : ex));
		}
	}

	private Book mapBookRequest(BookForm bookForm, Book book, boolean isAddBook) {
		book.setCoverThumbUrl(
				!StringUtils.isEmpty(bookForm.getCoverThumbUrl()) ? bookForm.getCoverThumbUrl() : defaultCoverThumbUrl);
		book.setCoverUrl(!StringUtils.isEmpty(bookForm.getCoverUrl()) ? bookForm.getCoverUrl() : defaultCoverurl);
		book.setTitle(bookForm.getTitle());
		book.setIsDeleted(false);
		book.setDescription(bookForm.getDescription());
		if (isAddBook) {
			book.setStatus("AVAILABLE");
		}

		Optional<Author> authorObj = authorRepository.findById(bookForm.getAuthorId());
		if (authorObj.isPresent()) {
			book.setAuthor(authorObj.get());
		} else if (bookForm.getAuthorId() == 0 && !StringUtils.isEmpty(bookForm.getAuthor())) {
			Author author = new Author();
			author.setName(bookForm.getAuthor());
			book.setAuthor(author);
		} else {
			throw new ValidationException("Author not found.");
		}

		Optional<Category> categoryObj = categoryRepository.findById(bookForm.getCategoryId());
		if (categoryObj.isPresent()) {
			book.setCategory(categoryObj.get());
		} else {
			throw new ValidationException("Category not found.");
		}

		Optional<Language> languageObj = languageRepository.findById(bookForm.getLanguageId());
		if (languageObj.isPresent()) {
			book.setLanguage(languageObj.get());
		} else {
			throw new ValidationException("Language not found.");
		}

		LocalDateTime currentDate = LocalDateTime.now();
		LocalDateTime expiryDate = bookForm.getNoOfMonths() > 0 ? currentDate.plusMonths(bookForm.getNoOfMonths())
				: null;
		book.setExpiryDate(expiryDate);
		return book;
	}

	@Override
	public boolean existsByBookId(int id) {
		return bookRepository.existsById(id);
	}

	@Override
	@Transactional
	public void deleteBook(int bookId) {

		int isAvailable = isBookAvailable(bookId);
		if (isAvailable > 0) {
			watchlistRepository.deleteByBookId(bookId);
			bookIssueRepository.deleteByBookId(bookId);
			bookRepository.deleteBookById(bookId);
		} else {
			throw new ValidationException("Can't delete, the book is not returned !");
		}

	}

	@Override
	public BookResponse getBookById(int id, String userName) {
		Optional<User> userObj = userRepository.findByUsername(userName);
		int userId = userObj.isPresent() ? userObj.get().getId() : 0;
		Optional<Book> bookObj = bookRepository.findById(id);
		if (bookObj.isPresent()) {
			Book book = bookObj.get();
			return bookUtil.mapBookResponse(book, userId);
		} else {
			throw new ValidationException(ApiConstants.NO_DATA);
		}

	}

	@Override
	public Data getBookByKeyword(int page, String keyword, int itemsPerPage, String userName) {
		Optional<User> userObj = userRepository.findByUsername(userName);
		Pageable pageable = PageRequest.of(page <= 0 ? 0 : page - 1, itemsPerPage);
		List<BookResponse> books = new ArrayList<>();
		Page<Book> searchBookPage;
		if (userObj.isPresent() && userObj.get().getRole().equals("admin")) {
			searchBookPage = bookRepository.findAllBooksByTitle(pageable,
					StringUtils.isEmpty(keyword) ? "%" : "%" + keyword + "%");
		} else {
			searchBookPage = bookRepository.findAllBooksByTitleUser(pageable,
					StringUtils.isEmpty(keyword) ? "%" : "%" + keyword + "%");
		}

		if (!ObjectUtils.isEmpty(searchBookPage)) {
			searchBookPage.getContent().forEach(searchBook -> {
				BookResponse bookResponse = bookUtil.mapBookResponse(searchBook, userObj.get().getId());
				bookResponse.setTakenBy(bookRepository.findBookTakenBy(searchBook.getId()));
				books.add(bookResponse);
			});
			Data bookData = new Data();
			bookData.setMeta(Meta.builder().totalPages(searchBookPage.getTotalPages())
					.itemsPerPage(searchBookPage.getSize()).totalItems(searchBookPage.getTotalElements()).build());
			bookData.setBooks(books);
			return bookData;

		} else {
			throw new ValidationException(ApiConstants.NO_BOOK);
		}
	}

	@Override
	@Transactional
	public void changeState(int bookId, String userName, int isApproved) {

		try {

			Optional<User> userObj = userRepository.findByUsername(userName);
			if (userObj.isPresent()) {
				if (userObj.get().getRole().equalsIgnoreCase("admin")) {
					bookRepository.updateState(bookId, isApproved);
				}
			} else {
				throw new ValidationException(ApiConstants.NO_DATA);
			}

		} catch (Exception ex) {
			log.debug(" State change failed : ", ex.getCause());

		}
	}

	@Override
	public Data loadDonateBook() {

		try {
			List<Author> authors = authorRepository.findAll();
			List<Category> categories = categoryRepository.findAll();
			List<Language> languages = languageRepository.findAll();

			Data loadData = new Data();
			loadData.setAuthors(authors);
			loadData.setCategories(categories);
			loadData.setLanguages(languages);

			return loadData;

		} catch (Exception ex) {
			log.debug(" State change failed : ", ex.getCause());
		}
		return null;

	}

	@Override
	public Data getNotifications(String userName, int pageNo, int itemsPerPage) {

		try {
			Pageable pageable = PageRequest.of(pageNo <= 0 ? 0 : pageNo - 1, itemsPerPage);
			List<Notifications> notifications = new ArrayList<>();
			Optional<User> userObj = userRepository.findByUsername(userName);
			if (userObj.isPresent()) {

				Page<Object[]> bookNotificationPage = bookRepository.findBooksForNotification(userObj.get().getId(),
						pageable, Integer.valueOf(returnLimit));

				if (!ObjectUtils.isEmpty(bookNotificationPage)
						&& !ObjectUtils.isEmpty(bookNotificationPage.getContent())) {
					log.debug("BookServiceImpl : getNotifications - bookNotificationPage " + bookNotificationPage);
					bookNotificationPage.getContent().forEach(notification -> {
						BookResponse bookResponse = mapNotificationResponse(notification);
						notifications.add(Notifications.builder().type(notification[13]).daysToReturn(notification[12])
								.book(bookResponse).build());
					});
					if (!ObjectUtils.isEmpty(notifications)) {
						Data notificationData = new Data();
						notificationData.setMeta(Meta.builder().totalPages(bookNotificationPage.getTotalPages())
								.itemsPerPage(bookNotificationPage.getSize())
								.totalItems(bookNotificationPage.getTotalElements()).build());
						notificationData.setNotifications(notifications);
						return notificationData;
					} else {
						throw new ValidationException(ApiConstants.NO_DATA);
					}
				} else {
					throw new ValidationException(ApiConstants.NO_DATA);
				}
			}

		} catch (Exception ex) {
			log.debug(" Get notifications failed : ", ex.getCause());
		}
		return null;
	}

	private BookResponse mapNotificationResponse(Object[] notification) {
		BookResponse bookResponse = new BookResponse();
		bookResponse.setId(Integer.valueOf("" + notification[0]));
		bookResponse.setCoverUrl("" + notification[1]);
		bookResponse.setCoverThumbUrl("" + notification[2]);
		bookResponse.setTitle("" + notification[3]);
		bookResponse.setStatus("" + notification[7]);
		bookResponse.setDescription("" + notification[9]);
		bookResponse.setIsWatchListed(
				!StringUtils.isEmpty(notification[13]) && notification[13].equals("AVAILABLE_ALERT") ? Boolean.TRUE
						: Boolean.FALSE);
		bookResponse.setIsApproved(
				!StringUtils.isEmpty(notification[10]) && Integer.valueOf("" + notification[10]) == 1 ? Boolean.TRUE
						: Boolean.FALSE);

		if (null != notification[11]) {
			bookResponse.setCreatedAt(Timestamp.valueOf("" + notification[11]).getTime());
		}

		if (null != notification[8]) {
			Optional<User> userObj = userRepository.findById(Integer.valueOf("" + notification[8]));
			if (userObj.isPresent()) {
				User user = userObj.get();
				bookResponse.setIsDonated(true);
				bookResponse.setDonatedBy(user.getName());
			} else {
				bookResponse.setIsDonated(false);
			}
		}

		if (null != notification[4]) {
			Optional<Author> authorObj = authorRepository.findById(Integer.valueOf("" + notification[4]));
			if (authorObj.isPresent()) {
				Author author = authorObj.get();
				bookResponse.setAuthor(author.getName());
				bookResponse.setAuthorId(author.getId());
			}
		}

		if (null != notification[5]) {
			Optional<Language> languageObj = languageRepository.findById(Integer.valueOf("" + notification[5]));
			if (languageObj.isPresent()) {
				Language language = languageObj.get();
				bookResponse.setLanguage(language.getName());
				bookResponse.setLanguageId(language.getId());
			}
		}

		if (null != notification[6]) {
			Optional<Category> categoryObj = categoryRepository.findById(Integer.valueOf("" + notification[6]));
			if (categoryObj.isPresent()) {
				Category category = categoryObj.get();
				bookResponse.setCategory(category.getName());
				bookResponse.setCategoryId(category.getId());
			}
		}

		LocalDateTime currentDate = LocalDateTime.now();

		if (null != notification[14]) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			LocalDateTime formatDateTime = LocalDateTime.parse(String.valueOf(notification[14]), formatter);
			bookResponse.setExpiredAt(Timestamp.valueOf("" + notification[14]).getTime());
			boolean flag = currentDate.isAfter(formatDateTime) == Boolean.TRUE ? Boolean.TRUE : Boolean.FALSE;
			bookResponse.setIsExpired(flag);
		} else {
			bookResponse.setIsExpired(false);
		}

		return bookResponse;
	}

	@Override
	public int isBookAvailable(int bookId) {

		log.debug("BookServiceImpl : isBookAvailable {} ");
		return bookRepository.isBookAvailable(bookId);
	}

	/*
	 * @Override
	 * 
	 * @Async public String generateQRCodeImage(Integer id, int width, int height) {
	 * 
	 * try { QRCodeWriter qrCodeWriter = new QRCodeWriter(); String qrCode = "TPL-"
	 * + id; BitMatrix bitMatrix = qrCodeWriter.encode(qrCode,
	 * BarcodeFormat.QR_CODE, width, height); Path path =
	 * FileSystems.getDefault().getPath(qrCodeImagePath + qrCode + ".png");
	 * MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path); return qrCode; }
	 * catch (Exception ex) { log.debug("QR code generation failed - " +
	 * ex.getMessage() + " - " + (null != ex.getCause() ? ex.getCause().getCause() :
	 * ex)); return null; }
	 * 
	 * }
	 * 
	 * @Override public void generatePdf() {
	 * 
	 * // PDF generator Document document = new Document(); try { PdfWriter writer =
	 * PdfWriter.getInstance(document, new FileOutputStream(qrCodePdfPath));
	 * document.open();
	 * 
	 * PdfPTable table = new PdfPTable(1); table.setWidthPercentage(100); // Width
	 * 100% table.setSpacingBefore(10f); // Space before table
	 * table.setSpacingAfter(10f); // Space after table
	 * 
	 * try (Stream<Path> walk = Files.walk(Paths.get(qrCodeImagePath))) {
	 * 
	 * Map<Integer, Book> bookMap = bookRepository.findAllActiveBooks().stream()
	 * .collect(Collectors.toMap(Book::getId, Function.identity()));
	 * walk.filter(file ->
	 * file.getFileName().toString().endsWith(".png")).forEach(file -> { int bookId
	 * =
	 * Integer.parseInt(file.getFileName().toString().split("\\.")[0].split("\\-")[1
	 * ]); if (bookMap.containsKey(bookId)) {
	 * 
	 * Image image;
	 * 
	 * // Create Image object try { image = Image.getInstance(qrCodeImagePath +
	 * file.getFileName().toString()); image.scaleAbsolute(200, 150);
	 * image.setAbsolutePosition(100f, 700f);
	 * 
	 * PdfPCell cell = new PdfPCell(); cell.addElement(image); cell.addElement(new
	 * Paragraph(bookMap.get(bookId).getTitle()));
	 * 
	 * cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	 * cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	 * 
	 * table.addCell(cell); } catch (BadElementException | IOException ex) {
	 * log.debug("Pdf generation failed - " + ex.getMessage() + " - " + (null !=
	 * ex.getCause() ? ex.getCause().getCause() : ex)); throw new
	 * ValidationException("Pdf generation failed !"); } } }); } catch (Exception
	 * ex) { log.debug("Pdf generation failed - " + ex.getMessage() + " - " + (null
	 * != ex.getCause() ? ex.getCause().getCause() : ex)); throw new
	 * ValidationException("Pdf generation failed !"); } document.add(table);
	 * document.close(); writer.close();
	 * 
	 * } catch (Exception ex) { log.debug("Pdf generation failed - " +
	 * ex.getMessage() + " - " + (null != ex.getCause() ? ex.getCause().getCause() :
	 * ex));
	 * 
	 * } }
	 */

	@Override
	@Transactional
	public String saveCoverImage(MultipartFile uploadImage, int bookId) {
		try {
			log.debug("BookServiceImpl : saveCoverImage");
			byte[] bytes = uploadImage.getBytes();
			Path path = null;
			StringBuilder imgUrl = new StringBuilder("http://").append(host).append("/book/images/");
			StringBuilder bookName = new StringBuilder("TPL-").append(bookId).append(".")
					.append(uploadImage.getOriginalFilename().split("\\.")[1]);
			path = Paths.get(coverImagePath + bookName);
			imgUrl.append("cover/").append(bookName);
			bookRepository.uploadCoverUrl(imgUrl.toString(), bookId);
			bookRepository.uploadCoverThumpUrl(imgUrl.toString(), bookId);

			Files.write(path, bytes);
			return imgUrl.toString();
		} catch (Exception ex) {
			log.debug("Error in saveCoverImage - " + ex.getMessage() + " - "
					+ (null != ex.getCause() ? ex.getCause().getCause() : ex));
			throw new ValidationException("Image upload failed !");
		}
	}

	@Override
	@Transactional
	public String saveCoverImageThumb(MultipartFile uploadImage, int bookId) {
		try {
			log.debug("BookServiceImpl : saveCoverImage");
			byte[] bytes = uploadImage.getBytes();
			Path path = null;
			StringBuilder imgUrl = new StringBuilder("http://").append(host).append("/book/images/");

			path = Paths.get(coverThumbImagePath + uploadImage.getOriginalFilename());
			imgUrl.append("thumb/").append(uploadImage.getOriginalFilename());
			bookRepository.uploadCoverThumpUrl(imgUrl.toString(), bookId);

			Files.write(path, bytes);
			return imgUrl.toString();
		} catch (Exception ex) {
			log.debug("Error in saveCoverImage - " + ex.getMessage() + " - "
					+ (null != ex.getCause() ? ex.getCause().getCause() : ex));
			throw new ValidationException("Image upload failed !");
		}

	}

	@Override
	public List<BookDto> getAllBooks(int pageNo, int pageLimit) {
		try {
			HttpClient client = HttpClient.newBuilder().version(Version.HTTP_2).connectTimeout(Duration.ofSeconds(20))
					.build();
			StringBuilder uri = new StringBuilder("http://localhost:8081/api/book/getAll?").append("pageNo=")
					.append(pageNo).append("&pageLimit=").append(pageLimit);

			HttpRequest request = HttpRequest.newBuilder().uri(new URI(uri.toString())).GET().build();
			HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
			ObjectMapper mapper = new ObjectMapper();
			List<BookDto> bookList = mapper.readValue(response.body(),
					mapper.getTypeFactory().constructCollectionType(List.class, BookDto.class));

			return bookList;
			
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}
}
