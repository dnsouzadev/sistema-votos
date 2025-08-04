package com.dnsouzadev.sistemavoto.service.impl;

import com.dnsouzadev.sistemavoto.dto.request.AuthRequest;
import com.dnsouzadev.sistemavoto.dto.request.RegisterRequest;
import com.dnsouzadev.sistemavoto.dto.response.AuthResponse;
import com.dnsouzadev.sistemavoto.model.User;
import com.dnsouzadev.sistemavoto.repository.UserRepository;
import com.dnsouzadev.sistemavoto.service.AuthService;
import com.dnsouzadev.sistemavoto.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("E-mail já cadastrado.");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);

        String token = jwtUtil.generateToken(user);
        return new AuthResponse(token);
    }

    @Override
    public AuthResponse login(AuthRequest request) {
        System.out.println("REQUEST IS: " + request.getEmail());
        System.out.println("PASSWORD: " +  request.getPassword());
        System.out.println("RESPONSE: " + new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        System.out.println("USER: " + user);

        String token = jwtUtil.generateToken(user);
        return new AuthResponse(token);
    }
}