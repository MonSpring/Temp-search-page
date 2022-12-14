package com.example.testsearch.controller;

import com.example.testsearch.dto.LoginReqDto;
import com.example.testsearch.oauth.KakaoUserService;
import com.example.testsearch.oauth.SignupRequestDto;
import com.example.testsearch.service.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    private final KakaoUserService kakaoUserService;

    // 회원 로그인 페이지
    @GetMapping("/login")
    public String login() {

        return "login";
    }

    // 로그인 요청 처리
    @PostMapping("/login")
    public String loginProc(LoginReqDto loginReqDto, HttpServletRequest request, HttpServletResponse response) {
        ResponseEntity<?> responseEntity = memberService.loginAccount(loginReqDto, request);

        // username 쿠키 1시간
        Cookie cookie = new Cookie("username",loginReqDto.getUsername());
        cookie.setMaxAge(3600);
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:/search";
    }

    // 회원 가입 페이지
    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    // 회원 가입 요청 처리
    @PostMapping("/signup")
    public String registerUser(SignupRequestDto signupReqDto) {
        memberService.createAccount(signupReqDto);
        return "redirect:/user/login";
    }

    // 카카오 로그인
    @GetMapping("/kakao/callback")
    public String kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        kakaoUserService.kakaoLogin(code);
        kakaoUserService.setCookie(response);
        return "redirect:/search";
    }

//    // 토큰 재발급
//    @PostMapping("/reissue")
//    public ResponseEntity<TokenDto> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
//        return ResponseEntity.ok(memberService.reissue(tokenRequestDto));
//    }
}
