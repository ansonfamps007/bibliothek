
package com.dlib.bibliothek.service;

import java.util.List;
import java.util.Optional;

import com.dlib.bibliothek.model.Author;
import com.dlib.bibliothek.request.AuthorForm;
import com.dlib.bibliothek.response.AuthorResponse;

public interface AuthorService {

	Author addAuthor(String name);

	boolean existByAuthorName(String name);

	boolean existByAuthorId(int id);

	void updateAuthor(AuthorForm authorForm);

	List<AuthorResponse> fetchAllAuthors();

	Optional<Author> fetchAuthorByName(String name);

	void deleteAuthor(int id);

}
