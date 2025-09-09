package com.innotium.devops.securepanel.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**", "/ping", "/version").permitAll() // 인증 없이 허용
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화 (API 테스트용)
                .formLogin(form -> form.disable()); // 기본 로그인 페이지 제거

        return http.build();
    }
}