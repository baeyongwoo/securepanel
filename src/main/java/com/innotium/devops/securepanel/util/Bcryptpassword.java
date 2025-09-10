package com.innotium.devops.securepanel.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Bcryptpassword {
    public static void main(String[] args) {
        System.out.println("bcrypt : " + new BCryptPasswordEncoder().encode("1234"));

    }
}
