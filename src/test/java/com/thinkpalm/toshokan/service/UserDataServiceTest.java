package com.thinkpalm.toshokan.service;

import static org.junit.Assert.assertEquals;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.dlib.bibliothek.model.User;
import com.dlib.bibliothek.repository.UserRepository;
import com.dlib.bibliothek.service.impl.UserDetailsServiceImpl;
import com.thinkpalm.toshokan.ToshokanApplicationTests;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ToshokanApplicationTests.class)
@AutoConfigureMockMvc
public class UserDataServiceTest {
	
	private MockMvc mockMvc;
	
	@Mock
	UserRepository userRepository;
	
	@InjectMocks
	UserDetailsServiceImpl userService;
 
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

	} 
	
	@Test
	public void updateFcmTest() {
		User user = new User();
		user.setUsername("Chandana");
		user.setFcmId("fcmId");
		user.setIsActive(true);
		Optional<User> userOptional = Optional.of(user);
		Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
		Optional<User> result = userService.findByUsername(user.getUsername());
		 // response = result.get().setUsername("Chandana");
		assertEquals("Chandana", result.get().getUsername()); 
		}
	


}
