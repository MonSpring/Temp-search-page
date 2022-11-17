package com.example.testsearch.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
public class LoginReqDto {

    @NotBlank
    private String username;
    @NotBlank
    private String password;

    // Security 인증용 토큰 만들거임
    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken( username , password );
    }

}
