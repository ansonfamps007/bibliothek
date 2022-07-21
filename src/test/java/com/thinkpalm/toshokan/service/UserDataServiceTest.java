package com.thinkpalm.toshokan.service;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.dlib.bibliothek.model.User;
import com.dlib.bibliothek.repository.UserRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserDataServiceTest {

	private MockMvc mockMvc;

	@Mock
	UserRepository userRepository;

	/*
	 * @InjectMocks UserDetailsServiceImpl userService;
	 */

	@Test
	public void updateFcmTest() {
		User user = new User();
		user.setUsername("Chandana");
		user.setFcmId("fcmId");
		user.setIsActive(true);
		Optional<User> userOptional = Optional.of(user);
		Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
		// Optional<User> result = userService.findByUsername(user.getUsername());
		// response = result.get().setUsername("Chandana");
		// assertEquals("Chandana", result.get().getUsername());
	}

}
