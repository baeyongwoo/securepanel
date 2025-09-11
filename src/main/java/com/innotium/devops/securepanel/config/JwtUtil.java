package com.innotium.devops.securepanel.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private final String SECRET = "securepanel-secret-key-very-secret-and-long"; // 최소 32바이트 이상
    private Key key;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Base64.getEncoder().encode(SECRET.getBytes());
        key = Keys.hmacShaKeyFor(keyBytes);
    }

    //  토큰 생성
    public String generateToken(String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1시간 유효
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    //  토큰에서 사용자 이름 추출
    public String validateTokenAndGetUsername(String token) {
        return extractClaims(token).getSubject();
    }

    //  토큰에서 역할(Role) 추출
    public String getRoleFromToken(String token) {
        return extractClaims(token).get("role", String.class);
    }

    //  공통 Claims 추출 메서드
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}