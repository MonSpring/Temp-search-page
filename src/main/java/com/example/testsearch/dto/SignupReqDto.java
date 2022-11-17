package com.example.testsearch.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class SignupReqDto {

    @Pattern(regexp = "^[a-zA-Z0-9]{4,12}$", message = "닉네임은 최소 4자 이상, 12자 이하 알파벳 대소문자(a-z, A-Z), 숫자(0-9)로 구성됩니다.")
    @NotBlank(message = "이름을 입력해주세요")
    private String username;

    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$", message = "비밀번호는 최소 8자 이상, 20자 이하 알파벳 대소문자, 숫자(0-9), 특수문자로 구성됩니다.\"")
    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;

    @NotBlank
    private String passwordConfirm;

    @NotBlank
    private String nickname;

}
