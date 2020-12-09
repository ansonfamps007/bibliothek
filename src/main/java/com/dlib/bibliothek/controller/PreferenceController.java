
package com.dlib.bibliothek.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dlib.bibliothek.exception.ValidationException;
import com.dlib.bibliothek.model.Preference;
import com.dlib.bibliothek.response.ApiResponse;
import com.dlib.bibliothek.response.Data;
import com.dlib.bibliothek.response.PreferenceResponse;
import com.dlib.bibliothek.service.PreferenceService;
import com.dlib.bibliothek.util.ApiConstants;

@RestController
@RequestMapping("/api/preference")
public class PreferenceController {

	@Autowired
	private PreferenceService preferenceService;

	@GetMapping("/get_public_preference")
	public ApiResponse getPreference() {
		try {
			List<Preference> preferences = preferenceService.getAllPreferences();
			if (!ObjectUtils.isEmpty(preferences)) {
				Preference preference = preferences.get(0);
				PreferenceResponse preferenceResponse = new PreferenceResponse();
				preferenceResponse.setDownReason(preference.getDownReason());
				preferenceResponse.setIsDown(preference.getIsDown());
				preferenceResponse.setLatestVersionCode(preference.getLatestVersionCode());
				preferenceResponse.setLatestVersionMessage(preference.getLatestVersionMessage());
				Data preferData = new Data();
				preferData.setPrefs(preferenceResponse);
				return ApiResponse.builder().error(false).message("OK").data(preferData).build();
			} else {
				throw new ValidationException(ApiConstants.INVALID);
			}
		} catch (Exception e) {
			throw new ValidationException(ApiConstants.INVALID);

		}
	}
}
