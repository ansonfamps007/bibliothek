/*
 * package com.dlib.bibliothek.service.impl;
 * 
 * import java.net.MalformedURLException; import java.sql.Timestamp; import
 * java.util.ArrayList; import java.util.List; import java.util.Optional; import
 * java.util.UUID; import java.util.concurrent.ExecutionException;
 * 
 * import javax.naming.ServiceUnavailableException; import
 * javax.servlet.http.HttpServletRequest; import
 * javax.transaction.Transactional; import javax.validation.Valid;
 * 
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.security.authentication.BadCredentialsException; import
 * org.springframework.security.core.userdetails.UserDetails; import
 * org.springframework.security.core.userdetails.UserDetailsService; import
 * org.springframework.security.core.userdetails.UsernameNotFoundException;
 * import org.springframework.security.crypto.password.PasswordEncoder; import
 * org.springframework.stereotype.Service; import
 * org.springframework.util.ObjectUtils;
 * 
 * import com.dlib.bibliothek.exception.ValidationException; import
 * com.dlib.bibliothek.model.User; import
 * com.dlib.bibliothek.repository.UserRepository; import
 * com.dlib.bibliothek.request.LoginForm; import
 * com.dlib.bibliothek.request.ResetPasswordForm; import
 * com.dlib.bibliothek.request.SignUpForm; import
 * com.dlib.bibliothek.response.Data; import
 * com.dlib.bibliothek.response.NotReturnedUsers; import
 * com.dlib.bibliothek.response.UserResponse; import
 * com.dlib.bibliothek.service.MailService; import
 * com.dlib.bibliothek.service.UserService; import
 * com.dlib.bibliothek.util.JwtUtil; import
 * com.microsoft.aad.adal4j.AuthenticationResult;
 * 
 * import lombok.extern.slf4j.Slf4j;
 * 
 * @Service
 * 
 * @Slf4j public class UserDetailsServiceImpl implements UserDetailsService,
 * UserService {
 * 
 * @Autowired private UserRepository userRepository;
 * 
 * @Autowired private PasswordEncoder encoder;
 * 
 * @Autowired private JwtUtil jwtUtil;
 * 
 * @Autowired private MailService mailService;
 * 
 * @Override
 * 
 * @Transactional public UserDetails loadUserByUsername(String username) { User
 * user = userRepository.findByUsername(username) .orElseThrow(() -> new
 * UsernameNotFoundException("Incorrect username or password !")); if
 * (user.getIsActive().equals(true)) { return UserPrinciple.build(user); } else
 * { throw new BadCredentialsException("Inactive user or email not verified !");
 * } }
 * 
 * @Override public List<UserResponse> fetchAllUsers() { List<User> userList =
 * userRepository.findAll(); List<UserResponse> userResponseList = new
 * ArrayList<>(); if (!ObjectUtils.isEmpty(userList)) {
 * userList.stream().forEach(user -> {
 * userResponseList.add(UserResponse.builder().fcmId(user.getFcmId()).name(user.
 * getName())
 * .email(user.getUsername()).role(user.getRole()).createdAt(user.getCreatedAt()
 * ) .isActive(user.getIsActive()).id(user.getId()).build()); }); } return
 * userResponseList; }
 * 
 * @Override public boolean existsByUsername(String username) { return
 * userRepository.existsByUsername(username); }
 * 
 * @Override
 * 
 * @Transactional public UserResponse save(SignUpForm signUpRequest,
 * HttpServletRequest request) { User user = new User(signUpRequest.getName(),
 * signUpRequest.getUsername(), encoder.encode(signUpRequest.getPassword()));
 * user.setRole(signUpRequest.getRole()); user.setIsActive(true);
 * user.setConfirmToken(UUID.randomUUID().toString());
 * user.setFcmId(signUpRequest.getFcmId()); userRepository.save(user);
 * log.debug("save : Saved user details {} ", signUpRequest); //
 * mailService.confirmationMail(request, user); return
 * UserResponse.builder().apiKey("").name(signUpRequest.getName()).build(); }
 * 
 * @Override public void activateUser(User user) { user.setIsActive(true);
 * userRepository.save(user);
 * log.debug("activateUser : User account activated {} ", user); }
 * 
 * @Override public void resetPassword(User user, @Valid ResetPasswordForm
 * resetPasswordForm) {
 * user.setPassword(encoder.encode(resetPasswordForm.getPassword()));
 * userRepository.save(user); log.debug("resetPassword : password reseted {} ",
 * resetPasswordForm); }
 * 
 * @Override public Optional<User> findByEmailToken(String emailToken) { return
 * userRepository.findByEmailToken(emailToken); }
 * 
 * @Override public Optional<User> findByResetToken(String resetToken) { return
 * userRepository.findByResetToken(resetToken); }
 * 
 * @Override
 * 
 * @Transactional public void forgotPasswordMail(User user, HttpServletRequest
 * request) { user.setResetToken(UUID.randomUUID().toString());
 * userRepository.save(user);
 * log.debug("forgotPasswordMail : Saved reset token {} ", user);
 * mailService.forgotPasswordMail(request, user); }
 * 
 * @Override public Optional<User> findByUsername(String username) { return
 * userRepository.findByUsername(username); }
 * 
 * @Override public Data getUserToken(UserDetails userDetails,
 * AuthenticationResult result) { Data userData = new Data(); if (null !=
 * result) {
 * userData.setUser(UserResponse.builder().name(userDetails.getUsername())
 * .apiKey("Bearer " +
 * result.getAccessToken()).refreshToken(result.getRefreshToken())
 * .role(userDetails.getAuthorities().toArray()[0].toString()).build()); return
 * userData; } else { throw new
 * ValidationException("Incorrect username or password !"); } }
 * 
 * @Override public AuthenticationResult authenticateUser(@Valid LoginForm
 * loginRequest) { try { return
 * jwtUtil.getTokenFromAzure(loginRequest.getUsername(),
 * loginRequest.getPassword()); } catch (BadCredentialsException e) { throw new
 * UsernameNotFoundException("Incorrect username or password !"); }
 * 
 * }
 * 
 * @Override
 * 
 * @Transactional public void updateUser(User user) { userRepository.save(user);
 * log.debug("Fcm id of user updated :  ", user); }
 * 
 * @Override
 * 
 * @Transactional public int updateFcm(String fcmId, String username) {
 * log.debug("Fcm id of user updated :  ", username); return
 * userRepository.updateFcm(fcmId, username); }
 * 
 * @Override public List<UserResponse> notReturnedUser() {
 * List<NotReturnedUsers> userListPage = userRepository.findNotReturnedUsers();
 * List<UserResponse> userResponseList = new ArrayList<>(); if
 * (!ObjectUtils.isEmpty(userListPage)) { userListPage.stream().forEach(user ->
 * {
 * userResponseList.add(UserResponse.builder().name(user.getName()).title(user.
 * getTitle()) .returnDate(Timestamp.valueOf(user.getReturnDt()).getTime())
 * .issuedAt(Timestamp.valueOf(user.getIssuedAt()).getTime()).daysToReturn(user.
 * getDaysToReturn()) .build()); });
 * 
 * } return userResponseList; }
 * 
 * @Override public Object getTokenFromRefreshToken(String refreshToken) throws
 * ServiceUnavailableException { try { return
 * jwtUtil.getTokenFromRefreshToken(refreshToken); } catch
 * (MalformedURLException | InterruptedException | ExecutionException e) { throw
 * new ValidationException(e.getMessage()); } catch (ServiceUnavailableException
 * se) { throw new ServiceUnavailableException(se.getMessage()); }
 * 
 * } }
 */