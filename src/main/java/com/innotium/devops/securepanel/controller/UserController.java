//package com.innotium.devops.securepanel.controller;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/user")
//public class UserController {
//
//    @GetMapping("/profile")
//    public ResponseEntity<?> getProfile(Authentication authentication) {
//        if (authentication == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body(Map.of("error", "인증 정보 없음"));
//        }
//
//        String username = authentication.getName();
//
//        Map<String, Object> profile = new HashMap<>();
//        profile.put("username", username);
//        profile.put("role", "USER");
//        profile.put("message", "인증된 사용자만 볼 수 있는 프로필입니다");
//
//        return ResponseEntity.ok(profile);
//    }
//}
