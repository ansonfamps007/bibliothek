
package com.dlib.bibliothek.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.dlib.bibliothek.exception.ValidationException;
import com.dlib.bibliothek.model.Category;
import com.dlib.bibliothek.repository.CategoryRepository;
import com.dlib.bibliothek.request.CategoryForm;
import com.dlib.bibliothek.response.CategoryResponse;
import com.dlib.bibliothek.service.CategoryService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	CategoryRepository categoryRepository;

	@Override
	public boolean existsByName(String name) {
		return categoryRepository.existsByName(name);
	}

	@Override
	public void addCategory(String name) {
		try {
			Category category = new Category();
			category.setName(name);
			categoryRepository.save(category);
		} catch (Exception e) {

			log.debug("CategoryServiceImpl : addCategory - Exception {} ", e);
			throw new ValidationException("Invalid request");
		}

	}

	@Override
	public boolean existsByCategoryId(int id) {
		return categoryRepository.existsById(id);
	}

	@Override
	public void updateCategory(CategoryForm categoryForm) {

		Category category = new Category();
		category.setName(categoryForm.getName());
		category.setId(categoryForm.getId());
		categoryRepository.save(category);

	}

	@Override
	public void deleteCategory(int id) {
		try {
			categoryRepository.deleteById(id);
		} catch (Exception ex) {
			throw new ValidationException("Category mapped with book, delete operation is not possible !");
		}

	}

	@Override
	public List<CategoryResponse> getAllCategories() {
		List<Category> categoryList = categoryRepository.findAll();
		List<CategoryResponse> categoryResponseList = new ArrayList<>();
		if (!ObjectUtils.isEmpty(categoryList)) {
			categoryList.forEach(category -> {
				categoryResponseList
						.add(CategoryResponse.builder().id(category.getId()).name(category.getName()).build());
			});
		}
		return categoryResponseList;
	}

	@Override
	public Optional<Category> fetchCategoryByName(String name) {
		return categoryRepository.findByName(name);
	}

}
