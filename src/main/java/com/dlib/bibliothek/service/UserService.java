/*
 * package com.dlib.bibliothek.service;
 * 
 * import java.util.List; import java.util.Optional;
 * 
 * import javax.naming.ServiceUnavailableException; import
 * javax.servlet.http.HttpServletRequest; import javax.validation.Valid;
 * 
 * //import org.springframework.security.core.userdetails.UserDetails;
 * 
 * import com.dlib.bibliothek.model.User; import
 * com.dlib.bibliothek.request.LoginForm; import
 * com.dlib.bibliothek.request.ResetPasswordForm; import
 * com.dlib.bibliothek.request.SignUpForm; import
 * com.dlib.bibliothek.response.Data; import
 * com.dlib.bibliothek.response.UserResponse; //import
 * com.microsoft.aad.adal4j.AuthenticationResult;
 * 
 * public interface UserService {
 * 
 * List<UserResponse> fetchAllUsers();
 * 
 * boolean existsByUsername(String username);
 * 
 * Optional<User> findByEmailToken(String emailToken);
 * 
 * Optional<User> findByResetToken(String resetToken);
 * 
 * UserResponse save(SignUpForm signUpRequest, HttpServletRequest request);
 * 
 * void activateUser(User user);
 * 
 * Optional<User> findByUsername(String username);
 * 
 * void forgotPasswordMail(User user, HttpServletRequest request);
 * 
 * void resetPassword(User user, @Valid ResetPasswordForm resetPasswordForm);
 * 
 * //AuthenticationResult authenticateUser(@Valid LoginForm loginRequest);
 * 
 * void updateUser(User user);
 * 
 * List<UserResponse> notReturnedUser();
 * 
 * //Data getUserToken(UserDetails userDetails, AuthenticationResult result);
 * 
 * Object getTokenFromRefreshToken(String refreshToken) throws
 * ServiceUnavailableException;
 * 
 * int updateFcm(String fcmId, String username); }
 */