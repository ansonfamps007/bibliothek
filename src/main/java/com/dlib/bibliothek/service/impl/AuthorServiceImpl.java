
package com.dlib.bibliothek.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.dlib.bibliothek.exception.ValidationException;
import com.dlib.bibliothek.model.Author;
import com.dlib.bibliothek.repository.AuthorRepository;
import com.dlib.bibliothek.request.AuthorForm;
import com.dlib.bibliothek.response.AuthorResponse;
import com.dlib.bibliothek.service.AuthorService;

@Service
public class AuthorServiceImpl implements AuthorService {

	@Autowired
	private AuthorRepository authorRepository;

	@Override
	@Transactional
	public Author addAuthor(String name) {
		Author author = new Author();
		author.setName(name);
		return authorRepository.save(author);
	}

	@Override
	public boolean existByAuthorName(String name) {
		return authorRepository.existsByName(name);
	}

	@Override
	public boolean existByAuthorId(int id) {
		return authorRepository.existsById(id);
	}

	@Override
	public void updateAuthor(AuthorForm authorForm) {
		Author author = new Author();
		author.setName(authorForm.getName());
		author.setId(authorForm.getId());
		authorRepository.save(author);
	}

	@Override
	public void deleteAuthor(int id) {
		try {
			authorRepository.deleteById(id);
		} catch (Exception ex) {
			throw new ValidationException("Author mapped with book, delete operation is not possible !");
		}
	}

	@Override
	public List<AuthorResponse> fetchAllAuthors() {
		List<Author> authorList = authorRepository.findAll();
		List<AuthorResponse> authorResponseList = new ArrayList<>();
		if (!ObjectUtils.isEmpty(authorList)) {
			authorList.forEach(author -> {
				authorResponseList.add(AuthorResponse.builder().id(author.getId()).name(author.getName()).build());
			});
		}
		return authorResponseList;
	}

	@Override
	public Optional<Author> fetchAuthorByName(String name) {
		return authorRepository.findByName(name);
	}
}
