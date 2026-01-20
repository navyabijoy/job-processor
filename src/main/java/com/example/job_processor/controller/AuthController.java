package com.example.job_processor.controller;

import com.example.job_processor.security.JwtUtil;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/token")
    public Map<String, String> token(
            @RequestParam String username,
            @RequestParam String password
    ) {
        if (!"admin".equals(username) || !"admin123".equals(password)) {
            throw new RuntimeException("Invalid Credentials");
        }

        String jwt = jwtUtil.generateToken(username);
        return Map.of("token", jwt);
    }
}