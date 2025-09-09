package com.innotium.devops.securepanel.controller;

import com.innotium.devops.securepanel.dto.LoginRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        System.out.println("요청 들어옴: " + request.getUsername());
        if ("admin".equals(request.getUsername()) && "1234".equals(request.getPassword())) {
            return "로그인 성공";
        } else {
            return "로그인 실패";
        }
    }
}