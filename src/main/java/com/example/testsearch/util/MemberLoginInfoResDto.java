package com.example.testsearch.util;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter @Setter
@NoArgsConstructor
public class MemberLoginInfoResDto {
    private String username;
    private String memberIp;
    private String loginTime;

    @Builder
    public MemberLoginInfoResDto(String username, String memberIp, LocalDateTime loginTime) {
        this.username = username;
        this.memberIp = memberIp;
        this.loginTime = loginTime.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초"));
    }
}
