package com.dlib.bibliothek.service;

import javax.servlet.http.HttpServletRequest;

import com.dlib.bibliothek.model.User;

public interface MailService {

	void confirmationMail(HttpServletRequest request, User user);

	void forgotPasswordMail(HttpServletRequest request, User user);
}
