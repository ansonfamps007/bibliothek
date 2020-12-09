
package com.dlib.bibliothek.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dlib.bibliothek.exception.ValidationException;
import com.dlib.bibliothek.request.SignUpForm;
import com.dlib.bibliothek.response.ApiResponse;
import com.dlib.bibliothek.response.Data;
import com.dlib.bibliothek.response.UserResponse;
import com.dlib.bibliothek.service.UserService;
import com.dlib.bibliothek.util.ApiConstants;
import com.dlib.bibliothek.util.JwtUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * The user data controller - For user details update and display.
 */

@RestController
@RequestMapping("/api/user/data")
@Slf4j
public class UserDataController {

	@Autowired
	private UserService userService;

	@Autowired
	private JwtUtil jwtUtil;

	/**
	 * 
	 * @param userName
	 * @return
	 */
	@GetMapping(value = "/get_users", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ApiResponse getUsers(@RequestParam(value = "user_name", required = false) String userName) {
		log.info("UserDataController - getUsers : fetch users {} ", userName);
		if (StringUtils.isEmpty(userName)) {
			List<UserResponse> userList = userService.fetchAllUsers();
			if (!ObjectUtils.isEmpty(userList)) {
				Data userData = new Data();
				userData.setUser(userList);
				log.debug("userList : ", userList);
				return ApiResponse.builder().error(false).data(userData).message("OK").build();
			} else {
				log.debug(ApiConstants.NO_USER_DATA);
				throw new ValidationException(ApiConstants.NO_USER_DATA);
			}
		}
		return null;
	}

	/**
	 * 
	 * @param request
	 * @param fcmId
	 * @return
	 */
	@PostMapping(value = "/update_fcm", produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {
			MediaType.APPLICATION_JSON_VALUE })
	public ApiResponse updateFcm(@RequestBody SignUpForm signUpForm, HttpServletRequest request) {
		String jwt = jwtUtil.getJwt(request);
		String userName = (null != jwt) ? jwtUtil.retriveUsernameFromJsonString(jwtUtil.getDataFromToken(jwt)) : null;
		String fcmId = signUpForm.getFcmId();
		log.info("UserDataController - updateFcm {} ", fcmId);
		return userService.findByUsername(userName).map(user -> {
			user.setFcmId(fcmId);
			userService.updateUser(user);
			return ApiResponse.builder().error(false).message("Fcm id updated").build();
		}).orElseThrow(() -> new ValidationException("Fcm Updation failed !"));
	}

	/**
	 * 
	 * @return
	 */
	@GetMapping(value = "/not_retuned_users", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ApiResponse notRetunedUsers() {

		List<UserResponse> userList = userService.notReturnedUser();
		if (!ObjectUtils.isEmpty(userList)) {
			Data userData = new Data();
			userData.setUser(userList);
			log.debug("userList : ", userList);
			return ApiResponse.builder().error(false).data(userData).message("OK").build();
		} else {
			log.debug(ApiConstants.NO_USER_DATA);
			throw new ValidationException(ApiConstants.NO_USER_DATA);
		}

	}
}
