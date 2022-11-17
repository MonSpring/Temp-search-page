package com.example.testsearch.controller;

import com.example.testsearch.dto.*;
import com.example.testsearch.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth/")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 회원 가입
    @PostMapping("/signup")
    public ResponseDto<?> signup(@RequestBody @Valid SignupReqDto signupReqDto) {
        return memberService.createAccount(signupReqDto);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginReqDto loginReqDto) {
        return memberService.loginAccount(loginReqDto);
    }

    // 토큰 재발급
    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return ResponseEntity.ok(memberService.reissue(tokenRequestDto));
    }

    // 아이디 중복확인  /check-id?username=뭐쥬?
    @GetMapping("/check-id")
    public ResponseDto<?> checkId(@RequestParam String username) {
        return memberService.duplicateCheckId(username);
    }

    // 닉네임 중복확인 /check-id?nickname=뭐쥬?
    @GetMapping("/check-nickname")
    public ResponseDto<?> checkNickname(@RequestParam String nickname) {
        return memberService.duplicateCheckNickname(nickname);
    }
}
