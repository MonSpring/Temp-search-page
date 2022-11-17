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

                // 세션 방식 끄기
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                //  로그인 폼
                .and()
                // [로그인 기능]
                .formLogin()
                // 로그인 View 제공 (GET /user/login)
                .loginPage("/user/login")
                // 로그인 처리 (POST /user/login)
                .loginProcessingUrl("/user/login")
                // 로그인 처리 후 성공 시 URL
                .defaultSuccessUrl("/search")
                // 로그인 처리 후 실패 시 URL
                .failureUrl("/user/login?error")
                .permitAll()

                .and()
                // [로그아웃 기능]
                .logout()
                // 로그아웃 요청 처리 URL
                .logoutUrl("/user/logout")
                .permitAll()
                .and()
                .exceptionHandling()
                // "접근 불가" 페이지 URL 설정
                .accessDeniedPage("/forbidden.html")

                // h2-console 설정 추가, 웹소캣 위한 frameOption disabled
                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                // 토큰 없을 때 들어오는 요청 허가
                .and()
                .authorizeRequests()
                // 홈 회원가입, 로그인 허용
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
//                .antMatchers("/**").permitAll()
                // image 폴더를 login 없이 허용
                .antMatchers("/images/**").permitAll()
                // css 폴더를 login 없이 허용
                .antMatchers("/css/**").permitAll()
                // 회원 관리 처리 API 전부를 login 없이 허용
                .antMatchers("/user/**").permitAll()
                // 인덱스 페이지 허용
                .antMatchers("/**").permitAll()
                // 그외 어떤 요청이 오든 체크
                .anyRequest().authenticated()

                //  jwtFilter를 jwt 커스텀 설정을 만들어 UsernamePasswordAuthenticationFilter 보다 앞으로 오게 배치
                .and()
                .apply(new JwtSecurityConfig(tokenProvider));
                // 여기서 addFilterBefore 안하고 jwtConfig 커스텀 클래스에서 설정함

        return http.build();

    }

}
