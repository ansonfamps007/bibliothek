
package com.dlib.bibliothek.service;

import java.util.List;
import java.util.Optional;

import com.dlib.bibliothek.model.Category;
import com.dlib.bibliothek.request.CategoryForm;
import com.dlib.bibliothek.response.CategoryResponse;

public interface CategoryService {

	boolean existsByName(String name);

	void addCategory(String name);

	boolean existsByCategoryId(int id);

	void updateCategory(CategoryForm categoryForm);

	void deleteCategory(int id);

	List<CategoryResponse> getAllCategories();

	Optional<Category> fetchCategoryByName(String name);

}
