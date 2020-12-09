
package com.dlib.bibliothek.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dlib.bibliothek.exception.ValidationException;
import com.dlib.bibliothek.request.AuthorForm;
import com.dlib.bibliothek.response.ApiResponse;
import com.dlib.bibliothek.response.AuthorResponse;
import com.dlib.bibliothek.response.Data;
import com.dlib.bibliothek.service.AuthorService;
import com.dlib.bibliothek.util.ApiConstants;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/author")
@Slf4j
public class AuthorController {

	@Autowired
	private AuthorService authorService;

	@PostMapping(value = "/add", produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {
			MediaType.APPLICATION_JSON_VALUE })
	public ApiResponse addAuthor(@Valid @RequestBody AuthorForm authorForm) {
		log.debug("AuthorController : addAuthor {} ");
		if (!authorService.existByAuthorName(authorForm.getName())) {
			authorService.addAuthor(authorForm.getName());
			return ApiResponse.builder().error(false).message("Author Inserted").build();
		} else {
			throw new ValidationException("Author name already exist !");
		}
	}

	@GetMapping(value = "/get_authors", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ApiResponse getAuthors() {
		log.info("AuthorController - getAuthors {} ");
		Data authorData = new Data();
		List<AuthorResponse> authorList = authorService.fetchAllAuthors();
		if (!ObjectUtils.isEmpty(authorList)) {
			authorData.setAuthors(authorList);
			log.debug("authorList : ", authorList);
			return ApiResponse.builder().error(false).data(authorData).message("OK").build();
		} else {
			log.debug(ApiConstants.NO_DATA);
			throw new ValidationException(ApiConstants.NO_DATA);
		}
	}

	@GetMapping(value = "/get_authors/{name}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ApiResponse getAuthorByName(@PathVariable String name) {
		log.info("AuthorController - getAuthors {} ", name);
		Data authorData = new Data();
		return authorService.fetchAuthorByName(name).map(author -> {
			authorData.setAuthors(author);
			return ApiResponse.builder().error(false).data(authorData).message("OK").build();
		}).orElseThrow(() -> new ValidationException(ApiConstants.NO_DATA));
	}

	@PutMapping(value = "/update", produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {
			MediaType.APPLICATION_JSON_VALUE })
	public ApiResponse updateAuthor(@Valid @RequestBody AuthorForm authorForm) {
		log.debug("AuthorController : updateAuthor {} ");
		if (authorService.existByAuthorId(authorForm.getId())) {
			authorService.updateAuthor(authorForm);
			return ApiResponse.builder().error(false).message("Author Updated").build();
		} else {
			throw new ValidationException(ApiConstants.NO_DATA);
		}
	}

	@DeleteMapping(value = "/delete/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ApiResponse deleteAuthor(@PathVariable int id) {
		log.debug("AuthorController : deleteAuthor {} ");
		if (authorService.existByAuthorId(id)) {
			authorService.deleteAuthor(id);
			return ApiResponse.builder().error(false).message("Author Deleted").build();
		} else {
			throw new ValidationException(ApiConstants.NO_DATA);
		}
	}
}
