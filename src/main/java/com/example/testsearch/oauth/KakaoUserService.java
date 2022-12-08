package com.example.testsearch.oauth;

import com.example.testsearch.entity.Authority;
import com.example.testsearch.entity.Member;
import com.example.testsearch.repository.MemberRepository;
import com.example.testsearch.security.MemberDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Service
public class KakaoUserService {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Value("#{environment['kakao.Key']}")
    private String key;

    private String nickname = "";

    @Autowired
    public KakaoUserService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void kakaoLogin(String code) throws JsonProcessingException {
        // 1. "인가 코드"로 "액세스 토큰" 요청
        String accessToken = getAccessToken(code);

        // 2. 토큰으로 카카오 API 호출
        KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(accessToken);

        // 3. 필요시에 회원가입
        Member kakaoUsers = registerKakaoUserIfNeeded(kakaoUserInfo);

        // 4. 강제 로그인 처리
        forceLogin(kakaoUsers);
    }

    private String getAccessToken(String code) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        // 카카오 RestAPI
        body.add("client_id", key);
        body.add("redirect_uri", "http://localhost:8080/user/kakao/callback");
        body.add("code", code);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    private KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        // 여기를 통해 카카오 JSON 데이터를 꺼내옴 = 네이버 구글도 여기만 바꾸면 정보 가져올 수 있음
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        Long id = jsonNode.get("id").asLong();
        nickname = jsonNode.get("properties")
                .get("nickname").asText();
        String email = jsonNode.get("kakao_account")
                .get("email").asText();

        System.out.println("카카오 사용자 정보: " + id + ", " + nickname + ", " + email);

        return new KakaoUserInfoDto(id, nickname, email);
    }

    private Member registerKakaoUserIfNeeded(KakaoUserInfoDto kakaoUserInfo) {
        // DB 에 중복된 Kakao Id 가 있는지 확인
        Long kakaoId = kakaoUserInfo.getId();
        Member kakaoMember = memberRepository.findByKakaoId(kakaoId)
                .orElse(null);
        if (kakaoMember == null) {
            // 카카오 사용자 이메일과 동일한 이메일을 가진 회원이 있는지 확인
            String kakaoEmail = kakaoUserInfo.getEmail();
            Member sameEmailMember = memberRepository.findByEmail(kakaoEmail).orElse(null);
            if (sameEmailMember != null) {
                kakaoMember = sameEmailMember;
                // 기존 회원정보에 카카오 Id 추가
                kakaoMember.setKakaoId(kakaoId);
            } else {
                // 신규 회원가입
                // username: kakao nickname
                String nickname = kakaoUserInfo.getNickname();

                // password: random UUID
                String password = UUID.randomUUID().toString();
                String encodedPassword = passwordEncoder.encode(password);

                // email: kakao email
                String email = kakaoUserInfo.getEmail();
                // role: 일반 사용자
                Authority role = Authority.ROLE_USER;

                kakaoMember = new Member(nickname, encodedPassword, email, role, kakaoId);
            }

            memberRepository.save(kakaoMember);
        }
        return kakaoMember;
    }

    private void forceLogin(Member kakaoMember) {
        MemberDetails memberDetails = new MemberDetails(kakaoMember);
        Authentication authentication = new UsernamePasswordAuthenticationToken(memberDetails, null, memberDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public void setCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("username",nickname);
        cookie.setMaxAge(3600);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
