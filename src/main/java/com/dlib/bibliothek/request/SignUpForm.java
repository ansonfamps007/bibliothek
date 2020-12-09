
package com.dlib.bibliothek.request;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpForm {

    @NotBlank
    private String name;
 
    @NotBlank
    @JsonAlias("email")
    private String username; 
    
    @NotBlank
    private String password;
    
    @NotBlank
    private String role;
    
    private String fcmId;
    
    private String refreshToken;
}
