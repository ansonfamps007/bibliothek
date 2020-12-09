
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
import com.dlib.bibliothek.request.LanguageForm;
import com.dlib.bibliothek.response.ApiResponse;
import com.dlib.bibliothek.response.Data;
import com.dlib.bibliothek.response.LanguageResponse;
import com.dlib.bibliothek.service.LanguageService;
import com.dlib.bibliothek.util.ApiConstants;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/language")
@Slf4j
public class LanguageController {

	@Autowired
	private LanguageService languageService;

	/**
	 * 
	 * 
	 * @param languageForm
	 * @return
	 */
	@PostMapping(value = "/add_language", produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {
			MediaType.APPLICATION_JSON_VALUE })
	public ApiResponse addLanguage(@RequestBody LanguageForm languageForm) {

		log.debug("LanguageController : add_language {} ");
		if (!languageService.existsByName(languageForm.getName())) {
			languageService.addLanguage(languageForm.getName());
			return ApiResponse.builder().error(false).message("Language added successfully.").build();
		} else {
			throw new ValidationException("Language name already exist !");
		}
	}

	/**
	 * 
	 * @param languageForm
	 * @return
	 */
	@PostMapping(value = "/update_language", produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {
			MediaType.APPLICATION_JSON_VALUE })
	public ApiResponse updateLanguage(@RequestBody LanguageForm languageForm) {

		if (languageService.existsByLanguageId(languageForm.getId())) {
			languageService.updateLanguage(languageForm);
			return ApiResponse.builder().error(false).message("Language updated successfully.").build();
		} else {
			throw new ValidationException(ApiConstants.NO_DATA);
		}
	}

	/**
	 * 
	 * 
	 * @param languageForm
	 * @return
	 */
	@DeleteMapping(value = "/delete_language/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ApiResponse deleteLanguage(@PathVariable int id) {

		if (languageService.existsByLanguageId(id)) {
			languageService.deleteLanguage(id);
			return ApiResponse.builder().error(false).message("Language deleted successfully.").build();
		} else {
			throw new ValidationException(ApiConstants.NO_DATA);
		}
	}

	/**
	 * 
	 * @return
	 */
	@GetMapping(value = "/get_languages", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ApiResponse getLanguages() {
		List<LanguageResponse> languages = languageService.getAllLangauges();
		if (!ObjectUtils.isEmpty(languages)) {
			Data languageData = new Data();
			languageData.setLanguages(languages);
			return ApiResponse.builder().error(false).message("OK").data(languageData).build();
		} else {
			throw new ValidationException(ApiConstants.NO_DATA);
		}
	}

	@GetMapping(value = "/get_languages/{name}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ApiResponse getLanguageByName(@PathVariable String name) {
		log.info("CategoryController - getCategoryByName {} ", name);
		Data languageData = new Data();
		return languageService.fetchLanguageByName(name).map(language -> {
			languageData.setLanguages(language);
			return ApiResponse.builder().error(false).data(languageData).message("OK").build();
		}).orElseThrow(() -> new ValidationException(ApiConstants.NO_DATA));
	}

}
