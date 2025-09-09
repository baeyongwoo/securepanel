package com.innotium.devops.securepanel.controller;

import com.innotium.devops.securepanel.config.JwtUtil;
import com.innotium.devops.securepanel.dto.LoginRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        if ("admin".equals(request.getUsername()) && "1234".equals(request.getPassword())) {
            String token = jwtUtil.generateToken(request.getUsername(), "ADMIN");

            Map<String, Object> response = new HashMap<>();
            response.put("message", "로그인 성공");
            response.put("token", token);

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "로그인 실패"));
        }
    }
}