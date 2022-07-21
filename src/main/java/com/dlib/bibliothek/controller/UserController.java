/*
 * package com.dlib.bibliothek.controller;
 * 
 * import javax.naming.ServiceUnavailableException; import
 * javax.servlet.http.HttpServletRequest; import javax.validation.Valid;
 * 
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.http.MediaType; import
 * org.springframework.security.authentication.BadCredentialsException; import
 * org.springframework.security.core.userdetails.UserDetails; import
 * org.springframework.security.core.userdetails.UserDetailsService; import
 * org.springframework.security.core.userdetails.UsernameNotFoundException;
 * import org.springframework.util.StringUtils; import
 * org.springframework.web.bind.annotation.GetMapping; import
 * org.springframework.web.bind.annotation.PathVariable; import
 * org.springframework.web.bind.annotation.PostMapping; import
 * org.springframework.web.bind.annotation.RequestBody; import
 * org.springframework.web.bind.annotation.RequestMapping; import
 * org.springframework.web.bind.annotation.RestController;
 * 
 * import com.dlib.bibliothek.exception.ValidationException; import
 * com.dlib.bibliothek.request.LoginForm; import
 * com.dlib.bibliothek.request.ResetPasswordForm; import
 * com.dlib.bibliothek.request.SignUpForm; import
 * com.dlib.bibliothek.response.ApiResponse; import
 * com.dlib.bibliothek.response.Data; import
 * com.dlib.bibliothek.response.UserResponse; import
 * com.dlib.bibliothek.service.UserService; import
 * com.microsoft.aad.adal4j.AuthenticationResult;
 * 
 * import lombok.extern.slf4j.Slf4j;
 * 
 *//**
	 * 
	 * The User controller - For user registration.
	 */
/*
 * 
 * @RestController
 * 
 * @RequestMapping("/api/user")
 * 
 * @Slf4j public class UserController {
 * 
 * @Autowired private UserService userService;
 * 
 * @Autowired private UserDetailsService userDetailsService;
 * 
 *//**
	 * 
	 * @param loginRequest
	 * @return
	 */
/*
 * @PostMapping(value = "/login", produces = { MediaType.APPLICATION_JSON_VALUE
 * }, consumes = { MediaType.APPLICATION_JSON_VALUE }) public ApiResponse
 * userLogin(@Valid @RequestBody final LoginForm loginForm, HttpServletRequest
 * request) { try {
 * 
 * LoginForm loginRequest = new LoginForm();
 * loginRequest.setUsername(loginForm.getUsername() + "@thinkpalm.com");
 * loginRequest.setPassword(loginForm.getPassword());
 * loginRequest.setFcmId(loginForm.getFcmId());
 * log.debug("userLogin : Authenticating user {} ", loginRequest);
 * 
 * AuthenticationResult result = userService.authenticateUser(loginRequest);
 * UserDetails userDetails = null; if (null != result &&
 * !StringUtils.isEmpty(result.getAccessToken())) { String givenName =
 * result.getUserInfo().getGivenName() + " " +
 * result.getUserInfo().getFamilyName(); try { userDetails =
 * userDetailsService.loadUserByUsername(loginRequest.getUsername());
 * userService.updateFcm(loginRequest.getFcmId(), loginRequest.getUsername()); }
 * catch (UsernameNotFoundException ex) {
 * 
 * log.debug("New user from Azure active directory ! ");
 * 
 * SignUpForm signUpRequest = new SignUpForm();
 * signUpRequest.setUsername(loginRequest.getUsername());
 * signUpRequest.setPassword(loginRequest.getPassword());
 * signUpRequest.setName(givenName); signUpRequest.setRole("user");
 * signUpRequest.setFcmId(loginRequest.getFcmId());
 * userService.save(signUpRequest, request); userDetails =
 * userDetailsService.loadUserByUsername(loginRequest.getUsername()); } }
 * 
 * log.debug("Succesfully Authenticated ! "); return
 * ApiResponse.builder().error(false).message("OK").data(userService.
 * getUserToken(userDetails, result)) .build(); } catch (Exception ex) { if
 * (ex.getCause() instanceof BadCredentialsException) { throw new
 * BadCredentialsException(ex.getMessage()); } else { throw new
 * ValidationException(ex.getMessage()); } } }
 * 
 *//**
	 * 
	 * @param signUpRequest
	 * @param request
	 * @return
	 */
/*
 * @PostMapping(value = "/signup", produces = { MediaType.APPLICATION_JSON_VALUE
 * }, consumes = { MediaType.APPLICATION_JSON_VALUE }) public ApiResponse
 * userRegistration(@Valid @RequestBody final SignUpForm signUpRequest,
 * HttpServletRequest request) {
 * log.info("userRegistration : User Registration {} ", signUpRequest); if
 * (userService.existsByUsername(signUpRequest.getUsername())) {
 * log.debug("Registartion Failed : Username Exist! "); throw new
 * ValidationException("Username/Email is already taken!"); } // Creating user's
 * account UserResponse user = userService.save(signUpRequest, request); Data
 * userData = new Data(); userData.setUser(user); return
 * ApiResponse.builder().error(false).message("OK").data(userData).build(); }
 * 
 *//**
	 * 
	 * @param signUpRequest
	 * @param request
	 * @return
	 * @throws ServiceUnavailableException
	 */
/*
 * @PostMapping(value = "/get_new_api_key", produces = {
 * MediaType.APPLICATION_JSON_VALUE }, consumes = {
 * MediaType.APPLICATION_JSON_VALUE }) public ApiResponse
 * getAccessToken(@RequestBody SignUpForm refreshToken, HttpServletRequest
 * request) throws ServiceUnavailableException {
 * log.info("userRegistration :getAccessToken with refresh token {} ",
 * refreshToken);
 * 
 * Data userData = new Data(); userData.setNewApiKey("Bearer " +
 * userService.getTokenFromRefreshToken(refreshToken.getRefreshToken())); return
 * ApiResponse.builder().error(false).message("OK").data(userData).build(); }
 * 
 *//**
	 * 
	 * @param emailToken
	 * @return
	 */
/*
 * @GetMapping(value = "/verify_email/{emailToken}", produces = {
 * MediaType.APPLICATION_JSON_VALUE }) public ApiResponse
 * accountActivation(@PathVariable String emailToken) {
 * log.info("accountActivation : Activate account by email token {} ",
 * emailToken); return userService.findByEmailToken(emailToken).map(user -> {
 * userService.activateUser(user); log.debug("User activated "); return
 * ApiResponse.builder().error(false).message("Email verified successfully!").
 * build(); }).orElseThrow(() -> new
 * ValidationException("Email verification failed due to invalid token !")); }
 * 
 *//**
	 * 
	 * @param email
	 * @param request
	 * @return
	 */
/*
 * @PostMapping(value = "/forgot_password", produces = {
 * MediaType.APPLICATION_JSON_VALUE }, consumes = {
 * MediaType.APPLICATION_JSON_VALUE }) public ApiResponse
 * forgotPassword(@RequestBody LoginForm loginForm, HttpServletRequest request)
 * { String email = loginForm.getUsername();
 * log.info("forgotPassword : Password forgot email {} ", email); return
 * userService.findByUsername(email).map(user -> {
 * userService.forgotPasswordMail(user, request);
 * log.debug("Forgot email sent"); return ApiResponse.builder().error(false)
 * .message("We've sent you a confirmation email to { " + email +
 * " }. Please check your inbox") .build(); }).orElseThrow(() -> new
 * ValidationException("Invalid token or user!")); }
 * 
 *//**
	 * 
	 * @param resetPasswordForm
	 * @return
	 *//*
		 * @PostMapping(value = "/reset_password", produces = {
		 * MediaType.APPLICATION_JSON_VALUE }, consumes = {
		 * MediaType.APPLICATION_JSON_VALUE }) public ApiResponse
		 * resetPassword(@Valid @RequestBody final ResetPasswordForm resetPasswordForm)
		 * { log.info("resetPassword : reset password with reset token {} ",
		 * resetPasswordForm); return
		 * userService.findByResetToken(resetPasswordForm.getResetToken()).map(user -> {
		 * userService.resetPassword(user, resetPasswordForm);
		 * log.debug("Forgot email sent"); return
		 * ApiResponse.builder().error(false).message("Password reset successfully!").
		 * build(); }).orElseThrow(() -> new
		 * ValidationException("Password reset failed!")); } }
		 */