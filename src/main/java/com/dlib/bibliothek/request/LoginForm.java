
package com.dlib.bibliothek.request;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LoginForm {

	@NotBlank
	@JsonAlias("email")
	private String username;

	@NotBlank
	private String password;
	
	private String fcmId;

}
