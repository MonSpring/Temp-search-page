package com.example.testsearch.service;

import com.example.testsearch.dto.*;
import com.example.testsearch.entity.Authority;
import com.example.testsearch.entity.Member;
import com.example.testsearch.entity.RefreshToken;
import com.example.testsearch.dto.TokenDto;
import com.example.testsearch.jwt.JwtFilter;
import com.example.testsearch.jwt.TokenProvider;
import com.example.testsearch.repository.MemberRepository;
import com.example.testsearch.repository.RefreshTokenRepository;
import com.example.testsearch.security.MemberDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository
                .findByUsername(username)
                .orElseThrow(
                        ()->new UsernameNotFoundException(username+"을 찾을 수 없습니다")
                );
        return new MemberDetails(member);
    }

    // 회원가입
    @Transactional
    public ResponseDto<?> createAccount(SignupReqDto signupReqDto) {
        String username = signupReqDto.getUsername();
        String password = signupReqDto.getPassword();
        String passwordConfirm = signupReqDto.getPasswordConfirm();
        String nickname = signupReqDto.getNickname();
        if(memberRepository.existsByUsername(username)) {
            return ResponseDto.fail("이미 존재하는 아이디 입니다.");
        }
        if(!password.equals(passwordConfirm)) {
            return ResponseDto.fail("비밀번호와 비밀번호 확인이 일치하지 않습니다");
        }
        Member member = new Member(username, passwordEncoder.encode(password), nickname, Authority.ROLE_USER);
        return ResponseDto.success(memberRepository.save(member));
    }

    // 로그인
    @Transactional
    public ResponseEntity<?> loginAccount(LoginReqDto loginReqDto) {

        UsernamePasswordAuthenticationToken authenticationToken = loginReqDto.toAuthentication();
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        Member member = memberRepository.findByUsername(loginReqDto.getUsername()).orElse(null);
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();
        refreshTokenRepository.save(refreshToken);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, JwtFilter.BEARER_PREFIX + tokenDto.getAccessToken());
        httpHeaders.add("Refresh-Token", tokenDto.getRefreshToken());

        assert member != null;
        MemberDto memberDto = new MemberDto(member);

        return new ResponseEntity<>(ResponseDto.success(memberDto), httpHeaders, HttpStatus.OK);
    }

    // 토큰 재발급
    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto) {
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException(("Refresh Token 유효하지 않습니다"));
        }

        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(()->new RuntimeException("로그아웃 된 사용자입니다"));

        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다");
        }

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        RefreshToken refreshRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(refreshRefreshToken);

        return tokenDto;
    }

    // 아이디 중복 체크
    public ResponseDto<?> duplicateCheckId(String username) {
        if (memberRepository.existsByUsername(username))
        return ResponseDto.fail("중복된 아이디 값입니다");
        else {
            return ResponseDto.success("사용할 수 있는 아이디 입니다");
        }
    }

    // 닉네임 중복 체크
    public ResponseDto<?> duplicateCheckNickname(String nickname) {
        if (memberRepository.existsByNickname(nickname))
            return ResponseDto.fail("중복된 닉네임입니다");
        else {
            return ResponseDto.success("사용할 수 있는 닉네임 입니다");
        }
    }
}
