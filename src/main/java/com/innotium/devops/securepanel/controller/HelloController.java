package com.innotium.devops.securepanel.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    @GetMapping("/version")
    public String version() {
        return "SecurePanel v0.1";
    }
}