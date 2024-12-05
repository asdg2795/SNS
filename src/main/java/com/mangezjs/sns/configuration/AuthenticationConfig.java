package com.mangezjs.sns.configuration;

import com.mangezjs.sns.configuration.filter.JwtTokenFilter;
import com.mangezjs.sns.exception.CustomAuthenticationEntryPoint;
import com.mangezjs.sns.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AuthenticationConfig{


    private final UserService userService;
    @Value("${jwt.secret-key}")
    private String key;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() throws Exception{
        return (web) -> web.ignoring().requestMatchers(new RegexRequestMatcher("^(?!/api/).*", null));
        // "/api로 시작하는 path들만 통과를 시킨다."
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf->csrf
                    .disable()
                )
                .authorizeHttpRequests(authorize -> authorize
                        // 회원 가입 및 로그인은 모두 허용
                        .requestMatchers("/api/*/users/join", "/api/*/users/login").permitAll()
                        // 정적 리소스는 인증 필요 없음
                        .requestMatchers("/js/**", "/html/**", "/jpg/**", "/png/**", "/api/**").permitAll()
                        // 기타 모든 요청은 인증 필요
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(new JwtTokenFilter(key, userService), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception
                    .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                );

        return http.build();
    }

}
