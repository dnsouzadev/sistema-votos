package com.dnsouzadev.sistemavoto.service;


import com.dnsouzadev.sistemavoto.dto.request.AuthRequest;
import com.dnsouzadev.sistemavoto.dto.request.RegisterRequest;
import com.dnsouzadev.sistemavoto.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse login(AuthRequest request);
    AuthResponse register(RegisterRequest request);
}
