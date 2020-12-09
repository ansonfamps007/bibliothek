
package com.dlib.bibliothek.request;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AuthorForm {

	@NotBlank(message = "Author name should not be blank")
	private String name;
	
	private int id;

}
