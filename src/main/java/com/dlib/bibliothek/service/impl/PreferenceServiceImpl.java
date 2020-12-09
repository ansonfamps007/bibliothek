
package com.dlib.bibliothek.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dlib.bibliothek.model.Preference;
import com.dlib.bibliothek.repository.PreferenceRepository;
import com.dlib.bibliothek.service.PreferenceService;

@Service
public class PreferenceServiceImpl implements PreferenceService {
	
	@Autowired
	private PreferenceRepository preferenceRepository;

	@Override
	public List<Preference> getAllPreferences() {
		try {
			
			return preferenceRepository.findAll();
			
		} catch (Exception e) {
			

		}
		return null;
	}

}
