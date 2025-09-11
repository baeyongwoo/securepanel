package com.innotium.devops.securepanel.controller;

import com.innotium.devops.securepanel.config.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SecureController {

    @GetMapping("/home")
    public String home(Model model, @AuthenticationPrincipal CustomUserDetails user) {
        model.addAttribute("username", user.getUsername());
        model.addAttribute("role", user.getRole());
        return "home";
    }
}