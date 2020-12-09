
package com.dlib.bibliothek.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dlib.bibliothek.exception.ValidationException;
import com.dlib.bibliothek.request.CategoryForm;
import com.dlib.bibliothek.response.ApiResponse;
import com.dlib.bibliothek.response.CategoryResponse;
import com.dlib.bibliothek.response.Data;
import com.dlib.bibliothek.service.CategoryService;
import com.dlib.bibliothek.util.ApiConstants;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/category")
@Slf4j
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	/**
	 * 
	 * 
	 * @param categoryForm
	 * @return
	 */
	@PostMapping(value = "/add_category", produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {
			MediaType.APPLICATION_JSON_VALUE })
	public ApiResponse addCategory(@RequestBody CategoryForm categoryForm) {

		log.debug("CategoryController : addCategory {} ");
		if (!categoryService.existsByName(categoryForm.getName())) {
			categoryService.addCategory(categoryForm.getName());
			return ApiResponse.builder().error(false).message("Category added successfully.").build();
		} else {
			throw new ValidationException("Category name already exist !");

		}
	}

	/**
	 * 
	 * @param categoryForm
	 * @return
	 */
	@PostMapping(value = "/update_category", produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {
			MediaType.APPLICATION_JSON_VALUE })
	public ApiResponse updateCategory(@RequestBody CategoryForm categoryForm) {

		log.debug("CategoryController : updateCategory {} ");
		if (categoryService.existsByCategoryId(categoryForm.getId())) {
			categoryService.updateCategory(categoryForm);
			return ApiResponse.builder().error(false).message("Category updated successfully.").build();
		} else {
			throw new ValidationException(ApiConstants.NO_DATA);
		}
	}

	/**
	 * 
	 * @param categoryForm
	 * @return
	 */
	@DeleteMapping(value = "/delete_category/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ApiResponse deleteCategory(@PathVariable int id) {

		log.debug("CategoryController : deleteCategory {} ");
		if (categoryService.existsByCategoryId(id)) {
			categoryService.deleteCategory(id);
			return ApiResponse.builder().error(false).message("Category deleted successfully.").build();
		} else {
			throw new ValidationException(ApiConstants.NO_DATA);
		}
	}

	/**
	 * 
	 * @return
	 */

	@GetMapping(value = "/get_categories", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ApiResponse getCategories() {
		log.info("CategoryController - getCategories {} ");
		List<CategoryResponse> categories = categoryService.getAllCategories();
		if (!ObjectUtils.isEmpty(categories)) {
			Data categoryData = new Data();
			categoryData.setCategories(categories);
			return ApiResponse.builder().error(false).message("OK").data(categoryData).build();
		} else {
			throw new ValidationException(ApiConstants.NO_DATA);
		}
	}

	@GetMapping(value = "/get_categories/{name}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ApiResponse getCategoryByName(@PathVariable String name) {
		log.info("CategoryController - getCategoryByName {} ", name);
		Data categoryData = new Data();
		return categoryService.fetchCategoryByName(name).map(category -> {
			categoryData.setCategories(category);
			return ApiResponse.builder().error(false).data(categoryData).message("OK").build();
		}).orElseThrow(() -> new ValidationException(ApiConstants.NO_DATA));
	}

}
