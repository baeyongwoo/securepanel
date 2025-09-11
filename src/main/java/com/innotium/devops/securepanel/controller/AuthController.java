package com.innotium.devops.securepanel.controller;

import com.innotium.devops.securepanel.dto.LoginRequest;
import com.innotium.devops.securepanel.entity.User;
import com.innotium.devops.securepanel.repository.UserRepository;
import com.innotium.devops.securepanel.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 현재 사용자 정보 반환
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("인증되지 않은 사용자입니다.");
        }

        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        return ResponseEntity.ok(Map.of("username", user.getUsername(), "role", user.getRole()));
    }
}
