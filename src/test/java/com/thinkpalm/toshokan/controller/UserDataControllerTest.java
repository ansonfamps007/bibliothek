
package com.thinkpalm.toshokan.controller;

import static org.junit.Assert.assertEquals;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.dlib.bibliothek.controller.UserDataController;
import com.dlib.bibliothek.model.User;
import com.dlib.bibliothek.request.SignUpForm;
import com.dlib.bibliothek.response.ApiResponse;
import com.dlib.bibliothek.service.UserService;
import com.dlib.bibliothek.util.JwtUtil;
import com.thinkpalm.toshokan.ToshokanApplicationTests;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ToshokanApplicationTests.class)
@AutoConfigureMockMvc
public class UserDataControllerTest {


	@InjectMocks
	private UserDataController userDataController;

	@Mock
	private UserService userService;

	@Mock
	JwtUtil jwtUtil;

	@Test
	public void updateFcmTest() {
		User user = new User();
		user.setUsername("Chandana");
		user.setFcmId("fcmId");
		user.setIsActive(true);

		SignUpForm signUp = new SignUpForm();
		signUp.setName("Chandana");
		signUp.setFcmId("fcmId");
		signUp.setPassword("Admin@123");
		signUp.setRole("User");
		HttpServletRequest mockedRequest = Mockito.mock(HttpServletRequest.class);
		String jwt = "token";
		Optional<User> userOptional = Optional.of(user);

		Mockito.when(jwtUtil.getJwt(mockedRequest)).thenReturn(jwt);
		//Mockito.when(jwtUtil.extractUsername(jwt)).thenReturn(user.getUsername());
		Mockito.when(userService.findByUsername(user.getUsername())).thenReturn(userOptional);
		userOptional.get().setFcmId("FCM");
		userService.updateUser(user);
		ApiResponse apiResponse = userDataController.updateFcm(signUp, mockedRequest);
		assertEquals("Fcm id updated", apiResponse.getMessage());
	}
}
