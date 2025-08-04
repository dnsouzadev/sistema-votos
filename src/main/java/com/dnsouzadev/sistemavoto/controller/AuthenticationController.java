package com.dnsouzadev.sistemavoto.controller;

import com.dnsouzadev.sistemavoto.dto.request.AuthRequest;
import com.dnsouzadev.sistemavoto.dto.request.RegisterRequest;
import com.dnsouzadev.sistemavoto.dto.response.AuthResponse;
import com.dnsouzadev.sistemavoto.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthRequest request) {
//        return ResponseEntity.ok(authService.login(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.login(request));
    }
}