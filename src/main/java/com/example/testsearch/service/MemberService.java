package com.example.testsearch.service;

import com.example.testsearch.entity.MemberLoginInfo;
import com.example.testsearch.oauth.SignupRequestDto;
import com.example.testsearch.dto.*;
import com.example.testsearch.entity.Authority;
import com.example.testsearch.entity.Member;
import com.example.testsearch.dto.TokenDto;
import com.example.testsearch.jwt.JwtFilter;
import com.example.testsearch.jwt.TokenProvider;
import com.example.testsearch.repository.MemberLoginInfoRepository;
import com.example.testsearch.repository.MemberRepository;
import com.example.testsearch.security.MemberDetails;
import com.example.testsearch.util.BackOfficeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
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

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final StringRedisTemplate stringRedisTemplate;
    private final MemberLoginInfoRepository memberLoginInfoRepository;

    private static final String ADMIN_TOKEN = "AAABnv/xRVklrnYxKZ0aHgTBcXukeZygoC";

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
    public void createAccount(SignupRequestDto signupReqDto) {
        String username = signupReqDto.getUsername();
        String password = signupReqDto.getPassword();
        String email = signupReqDto.getEmail();
        if(memberRepository.existsByUsername(username)) {
            ResponseDto.fail("이미 존재하는 아이디 입니다.");
            return;
        }

        Authority role = Authority.ROLE_USER;
        if (signupReqDto.isAdmin()) {
            if (!signupReqDto.getAdminToken().equals(ADMIN_TOKEN)) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = Authority.ROLE_ADMIN;
        }

        Member member = new Member(username, passwordEncoder.encode(password), email, role);
        ResponseDto.success(memberRepository.save(member));
    }

    // 로그인
    @Transactional
    public ResponseEntity<?> loginAccount(LoginReqDto loginReqDto, HttpServletRequest request) {

        Member member;

        if (memberRepository.existsByUsername(loginReqDto.getUsername())) {
            Optional<Member> memberOptinal = memberRepository.findByUsername(loginReqDto.getUsername());
            member = memberOptinal.get();
        } else {
            throw new RuntimeException("유저 정보를 찾을 수 없습니다");
        }

        UsernamePasswordAuthenticationToken authenticationToken = loginReqDto.toAuthentication();
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        //refreshTokenRepository.save(refreshToken);

        ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();
        // 1일
        long LIMIT_TIME = 1000 * 60 * 60 * 24;
        stringStringValueOperations.set(authentication.getName(), tokenDto.getRefreshToken(),LIMIT_TIME, TimeUnit.MILLISECONDS);

        String value = stringStringValueOperations.get(authentication.getName());

        log.info("value =" +value);


        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, JwtFilter.BEARER_PREFIX + tokenDto.getAccessToken());
        httpHeaders.add("Refresh-Token", tokenDto.getRefreshToken());

        MemberDto memberDto = new MemberDto(member);

        // 접속 유저 로그인 정보 저장 (vm arguments option으로 -Djava.net.preferIPv4Stack=true 옵션 줘야 IPv6가 아닌 IPv4 값 불러옴)
        String memberIp = BackOfficeUtil.getClientIP(request);
        LocalDateTime now = LocalDateTime.now();
        // 존드 데이트 타임

        MemberLoginInfo memberLoginInfo = MemberLoginInfo.builder()
                .username(loginReqDto.getUsername())
                .memberIp(memberIp)
                .loginTime(now)
                .build();

        memberLoginInfoRepository.save(memberLoginInfo);

        return new ResponseEntity<>(ResponseDto.success(memberDto), httpHeaders, HttpStatus.OK);
    }

//    // 토큰 재발급
//    @Transactional
//    public TokenDto reissue(TokenRequestDto tokenRequestDto) {
//        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
//            throw new RuntimeException(("Refresh Token 유효하지 않습니다"));
//        }
//
//        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());
//
//        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
//            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다");
//        }
//
//        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
//
//        RefreshToken refreshRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
//        refreshTokenRepository.save(refreshRefreshToken);
//
//        return tokenDto;
//    }

}
