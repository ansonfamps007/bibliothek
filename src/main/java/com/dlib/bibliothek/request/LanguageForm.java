package com.dlib.bibliothek.request;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LanguageForm {
	
	
	private int id;
	
	@NotBlank(message = "Language name should not be blank")
	private String name;

}
