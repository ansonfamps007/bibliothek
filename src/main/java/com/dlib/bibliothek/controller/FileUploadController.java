
package com.dlib.bibliothek.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dlib.bibliothek.exception.ValidationException;
import com.dlib.bibliothek.response.ApiResponse;
import com.dlib.bibliothek.service.BookService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/upload")
@Slf4j
public class FileUploadController {

	@Autowired
	private BookService bookService;

	@PostMapping(value = "/cover_image/{book_id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ApiResponse uploadCoverImage(@PathVariable(value = "book_id") int bookId,
			@RequestParam("cover_image") MultipartFile coverImg) {
		log.debug("FileUploadController : uploadCoverImage {} ");
		if (!coverImg.isEmpty()) {
			String path = bookService.saveCoverImage(coverImg, bookId);
			return ApiResponse.builder().error(false).message("Image uploaded to the path : " + path).build();
		} else {
			throw new ValidationException("Please choose a file !");
		}
	}

	@PostMapping(value = "/cover_thumb_image/{book_id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ApiResponse uploadCoverThumbImage(@PathVariable(value = "book_id") int bookId,
			@RequestParam("cover_thumb_image") MultipartFile coverThumbImg) {
		log.debug("FileUploadController : uploadCoverImage {} ");
		if (!coverThumbImg.isEmpty()) {
			String path = bookService.saveCoverImageThumb(coverThumbImg, bookId);
			return ApiResponse.builder().error(false).message("Image uploaded to the path : " + path).build();
		} else {
			throw new ValidationException("Please choose a file !");
		}
	}
}
