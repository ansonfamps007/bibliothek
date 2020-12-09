
package com.thinkpalm.toshokan.service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.dlib.bibliothek.model.Preference;
import com.dlib.bibliothek.repository.PreferenceRepository;
import com.dlib.bibliothek.service.impl.PreferenceServiceImpl;

public class PrefereneServiceTest {

	@InjectMocks
	PreferenceServiceImpl preferenceServiceImpl;

	@Mock
	PreferenceRepository preferenceRepository;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

	}

	@Test
	public void getAllPreferenceTest() throws Exception {
		List<Preference> preferences = new ArrayList<>();
		Preference preference = new Preference();
		preference.setId((long) 1);
		preference.setDownReason("Technical Issue");
		preference.setIsDown(true);
		preference.setLatestVersionCode(1011);
		preference.setLatestVersionMessage("Please update");
		preferences.add(preference);

		Mockito.when(preferenceRepository.findAll()).thenReturn(preferences);
		List<Preference> result = preferenceServiceImpl.getAllPreferences();
		assertEquals(true, result.get(0).getIsDown());
	}
}
