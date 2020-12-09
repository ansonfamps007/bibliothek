
package com.dlib.bibliothek.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.dlib.bibliothek.model.Author;

public interface AuthorRepository extends CrudRepository<Author, Integer>{

	boolean existsByName(String name);
	
	List<Author> findAll();

	Optional<Author> findByName(String name);

}
