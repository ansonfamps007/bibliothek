
package com.dlib.bibliothek.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordForm {
 
    @NotBlank
    @Email
    @JsonAlias("email")
    private String username; 
    
    @NotBlank
    private String password;
    
    @NotBlank
    private String resetToken;
}
