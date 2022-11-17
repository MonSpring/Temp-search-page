package com.example.testsearch.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class TokenRequestDto {
    @NotBlank(message = "토큰 주세요")
    private String accessToken;
    @NotBlank(message = "토큰 주세요")
    private String refreshToken;
}
