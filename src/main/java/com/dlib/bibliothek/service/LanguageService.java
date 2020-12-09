
package com.dlib.bibliothek.service;

import java.util.List;
import java.util.Optional;

import com.dlib.bibliothek.model.Language;
import com.dlib.bibliothek.request.LanguageForm;
import com.dlib.bibliothek.response.LanguageResponse;

public interface LanguageService {

	void addLanguage(String languageName);

	void updateLanguage(LanguageForm languageForm);

	boolean existsByName(String name);

	boolean existsByLanguageId(int id);

	void deleteLanguage(int id);

	List<LanguageResponse> getAllLangauges();
	
	Optional<Language> fetchLanguageByName(String name);

}
