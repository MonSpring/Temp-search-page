package com.example.testsearch.controller;

import com.example.testsearch.oauth.KakaoUserService;
import com.example.testsearch.oauth.SignupRequestDto;
import com.example.testsearch.dto.*;
import com.example.testsearch.service.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
    public String loginProc(LoginReqDto loginReqDto, Model model, HttpServletRequest request) {
        ResponseEntity<?> responseEntity = memberService.loginAccount(loginReqDto, request);
        model.addAttribute(responseEntity);
        return "search";
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
    public String kakaoLogin(@RequestParam String code) throws JsonProcessingException {
        kakaoUserService.kakaoLogin(code);
        return "search";
    }

//    // 토큰 재발급
//    @PostMapping("/reissue")
//    public ResponseEntity<TokenDto> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
//        return ResponseEntity.ok(memberService.reissue(tokenRequestDto));
//    }
}
