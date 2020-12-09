
package com.dlib.bibliothek.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dlib.bibliothek.model.Category;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Integer>{

	boolean existsByName(String name);

	List<Category> findAll();

	Optional<Category> findByName(String name);

}
