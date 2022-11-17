package com.example.testsearch.config;

import com.example.testsearch.jwt.JwtAccessDeniedHandler;
import com.example.testsearch.jwt.JwtAuthenticationEntryPoint;
import com.example.testsearch.jwt.JwtSecurityConfig;
import com.example.testsearch.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.ConditionalOnDefaultWebSecurity;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsUtils;

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
@ConditionalOnDefaultWebSecurity
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class WebSecurityConfig {
    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    //  DB 비밀번호 암호화용
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //  h2 database 접속용
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web)->web.ignoring()
                .antMatchers("/h2-console/**", "/favicon.ico");
    }

    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .cors().disable()

                .exceptionHandling()
                // 익셉션 제어
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                // 유효한 자격증명을 제공하지 않고 접근하려 할때 401
                .accessDeniedHandler(jwtAccessDeniedHandler)
                // 필요한 권한이 없이 접근하려 할때 403

                // h2-console 설정 추가, 웹소캣 위한 frameOption disabled
                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                //  로그인 폼 쓸거야
                .and()
                .formLogin()

                // 세션 방식 끄기
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // 토큰 없을 때 들어오는 요청 허가
                .and()
                .authorizeRequests()
                // 홈 회원가입, 로그인 허용
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers("/**").permitAll()
//                // CORS PUT, DELETE simple Request 제외한 요청 허용
//                .antMatchers(HttpMethod.POST, "/api/auth/**").permitAll()
//                // 채팅 API 요청 허용
//                .antMatchers(HttpMethod.POST, "/chat/**").permitAll()
//                // 테스트용 S3 이미지 업로드 허용
//                .antMatchers(HttpMethod.POST, "/api/images/**").permitAll()
//                .antMatchers(HttpMethod.POST, "/api/videos/**").permitAll()
//                // 소셜 로그인 허용
//                .antMatchers(HttpMethod.POST, "/user/**").permitAll()
//                // 웹 소캣 채팅 허용
//                .antMatchers(HttpMethod.POST, "ws://localhost:8080/gs-guide-websocket/**").permitAll()
//                .antMatchers("/hello/**").permitAll()
//                .antMatchers("/topic/**").permitAll()
//                .antMatchers("/gs-guide-websocket/**").permitAll()
//                .antMatchers("/app/**").permitAll()
//                // 아래로는 다 인증, 인가 체크
//                .antMatchers(HttpMethod.POST).authenticated()
//                .antMatchers(HttpMethod.PUT).authenticated()
//                .antMatchers(HttpMethod.DELETE).authenticated()
//                .antMatchers("/api/mypage/**").authenticated()
//                // 그외 어떤 요청이 오든 허용
//                .anyRequest()
//                .permitAll()

                //  jwtFilter를 jwt 커스텀 설정을 만들어 UsernamePasswordAuthenticationFilter 보다 앞으로 오게 배치
                .and()
                .apply(new JwtSecurityConfig(tokenProvider));
                // 여기서 addFilterBefore 안하고 jwtConfig 커스텀 클래스에서 설정함

        return http.build();

    }

}
