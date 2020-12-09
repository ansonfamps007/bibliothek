
package com.thinkpalm.toshokan.controller;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ValidationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.dlib.bibliothek.controller.PreferenceController;
import com.dlib.bibliothek.model.Preference;
import com.dlib.bibliothek.response.ApiResponse;
import com.dlib.bibliothek.service.PreferenceService;
import com.thinkpalm.toshokan.ToshokanApplicationTests;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ToshokanApplicationTests.class)
@AutoConfigureMockMvc
public class PreferenceControllerTest {

	@InjectMocks
	private PreferenceController preferenceController;

	@Mock
	private PreferenceService preferenceService;

	/**
	 * 
	  To test preference listing
	 */
	@Test
	public void getPreferenceTest() throws Exception {

		List<Preference> preferenceList = new ArrayList<Preference>();
		Preference preference = new Preference();
		preference.setId((long) 1);
		preference.setDownReason("Technical Issue");
		preference.setIsDown(true);
		preference.setLatestVersionCode(1011);
		preference.setLatestVersionMessage("Please update");
		preferenceList.add(preference);
		Mockito.when(preferenceService.getAllPreferences()).thenReturn(preferenceList);
		ApiResponse apiResponse = preferenceController.getPreference();
		assertEquals("OK", apiResponse.getMessage());
	}

	@Test(expected = ValidationException.class)
	public void getPreferenceWhenNull() throws Exception {
		List<Preference> preferenceList = new ArrayList<Preference>();
		Mockito.when(preferenceService.getAllPreferences()).thenReturn(preferenceList);
		preferenceController.getPreference();
	}
}
