
package com.dlib.bibliothek.util;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.dlib.bibliothek.config.HeaderRequestInterceptor;
import com.dlib.bibliothek.model.Book;
import com.dlib.bibliothek.repository.WatchlistRepository;
import com.dlib.bibliothek.response.BookResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BookUtil {

	@Value("${app.firebase-server-key}")
	private String fcmServerKey;

	@Value("${app.firebase-api-url}")
	private String fireBaseApiUrl;

	@Autowired
	private WatchlistRepository watchlistRepository;

	public BookResponse mapBookResponse(Book feedBook, int userId) {
		log.debug("BookUtil : mapBook - mapping books  ", feedBook);
		BookResponse bookResponse = new BookResponse();
		bookResponse.setId(feedBook.getId());

		if (null != feedBook.getAuthor()) {
			bookResponse.setAuthor(feedBook.getAuthor().getName());
			bookResponse.setAuthorId(feedBook.getAuthor().getId());
		}

		if (null != feedBook.getCategory()) {
			bookResponse.setCategory(feedBook.getCategory().getName());
			bookResponse.setCategoryId(feedBook.getCategory().getId());
		}

		if (null != feedBook.getLanguage()) {
			bookResponse.setLanguage(feedBook.getLanguage().getName());
			bookResponse.setLanguageId(feedBook.getLanguage().getId());
		}

		bookResponse.setCoverThumbUrl(feedBook.getCoverThumbUrl());
		bookResponse.setCoverUrl(feedBook.getCoverUrl());
		if (!StringUtils.isEmpty(feedBook.getDonated())) {
			bookResponse.setIsDonated(true);
			bookResponse.setDonatedBy(feedBook.getDonated().getName());
		} else {
			bookResponse.setIsDonated(false);
		}

		bookResponse.setStatus(feedBook.getStatus());
		bookResponse.setTitle(feedBook.getTitle());
		bookResponse.setCreatedAt(Timestamp.valueOf(feedBook.getCreatedAt()).getTime());
		bookResponse.setDescription(feedBook.getDescription());
		bookResponse.setIsApproved(feedBook.getIsApproved());

		LocalDateTime currentDate = LocalDateTime.now();
		if (null != feedBook.getExpiryDate()) {
			boolean flag = currentDate.isAfter(feedBook.getExpiryDate()) == Boolean.TRUE ? Boolean.TRUE : Boolean.FALSE;
			bookResponse.setIsExpired(flag);
			bookResponse.setExpiredAt(Timestamp.valueOf(feedBook.getExpiryDate()).getTime());
		} else {
			bookResponse.setIsExpired(false);
		}

		int isWatchList = watchlistRepository.isWatchlistedBook(feedBook.getId(), userId);
		bookResponse.setIsWatchListed(isWatchList > 0 ? Boolean.TRUE : Boolean.FALSE);
		return bookResponse;
	}

	@Async
	public CompletableFuture<String> sendNotification(String fcmId, JSONObject data) {
		log.debug("sendNotification :  fcmId {}", fcmId);
		log.debug("sendNotification :  JSONObject {}", data);
		try {
			JSONObject body = new JSONObject();
			body.put("to", fcmId);
			body.put("priority", 10);
			body.put("data", data);

			HttpEntity<String> request = new HttpEntity<>(body.toString());
			RestTemplate restTemplate = new RestTemplate();

			ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
			interceptors.add(new HeaderRequestInterceptor("Authorization", "key=" + fcmServerKey));
			interceptors.add(new HeaderRequestInterceptor("Content-Type", "application/json"));
			restTemplate.setInterceptors(interceptors);

			String firebaseResponse = restTemplate.postForObject(fireBaseApiUrl, request, String.class);

			return CompletableFuture.completedFuture(firebaseResponse);

		} catch (Exception e) {
			log.debug("Error in sendNotification - " + e.getMessage() + "- " + e.getCause());
		}
		return null;
	}
}
