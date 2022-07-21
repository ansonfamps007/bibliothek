/*
 * package com.dlib.bibliothek.service.impl;
 * 
 * import javax.servlet.http.HttpServletRequest;
 * 
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.beans.factory.annotation.Value; import
 * org.springframework.mail.SimpleMailMessage; import
 * org.springframework.mail.javamail.JavaMailSender; import
 * org.springframework.scheduling.annotation.Async; import
 * org.springframework.stereotype.Service;
 * 
 * import com.dlib.bibliothek.model.User; import
 * com.dlib.bibliothek.service.MailService; import
 * com.dlib.bibliothek.util.ApiConstants;
 * 
 * import lombok.extern.slf4j.Slf4j;
 * 
 * @Service
 * 
 * @Slf4j public class MailServiceImpl implements MailService {
 * 
 * @Value("${app.mail-from}") private String mailFrom;
 * 
 * @Value("${app.api-host}") private String host;
 * 
 * @Autowired private JavaMailSender mailSender;
 * 
 * @Async
 * 
 * @Override public void confirmationMail(HttpServletRequest request, User user)
 * { String appUrl = request.getScheme() + "://" + host +
 * ApiConstants.VERIFY_MAIL_URL; SimpleMailMessage registrationEmail = new
 * SimpleMailMessage(); registrationEmail.setTo(user.getUsername());
 * registrationEmail.setSubject("Registration Confirmation"); registrationEmail
 * .setText("To activate your account, please click the link below:\n" + appUrl
 * + user.getConfirmToken()); registrationEmail.setFrom(mailFrom);
 * mailSender.send(registrationEmail);
 * log.debug("confirmationMail : Confirmation mail sent{} ", registrationEmail);
 * }
 * 
 * @Async
 * 
 * @Override public void forgotPasswordMail(HttpServletRequest request, User
 * user) { String appUrl = request.getScheme() + "://" + host +
 * ApiConstants.RESET_MAIL_URL; SimpleMailMessage forgotEmail = new
 * SimpleMailMessage(); forgotEmail.setTo(user.getUsername());
 * forgotEmail.setSubject("Forgot Password");
 * forgotEmail.setText("To reset your password, please click the link below:\n"
 * + appUrl + user.getResetToken()); forgotEmail.setFrom(mailFrom);
 * mailSender.send(forgotEmail);
 * log.debug("forgotPasswordMail : Forgot mail sent{} ", forgotEmail); }
 * 
 * }
 */