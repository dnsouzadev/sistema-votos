package com.dnsouzadev.sistemavoto.service;

import com.dnsouzadev.sistemavoto.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    Optional<User> findByEmail(String email);
    User findById(UUID id);
}
