package com.dnsouzadev.sistemavoto.service;

import com.dnsouzadev.sistemavoto.dto.request.ChangePasswordRequest;
import com.dnsouzadev.sistemavoto.dto.request.UpdateProfileRequest;
import com.dnsouzadev.sistemavoto.dto.response.UserProfileResponse;
import com.dnsouzadev.sistemavoto.dto.response.VoterResponse;
import com.dnsouzadev.sistemavoto.model.Poll;
import com.dnsouzadev.sistemavoto.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    Optional<User> findByEmail(String email);
    User findById(UUID id);
    
    UserProfileResponse getUserProfile(User user);
    
    User updateProfile(User user, UpdateProfileRequest request);
    
    void changePassword(User user, ChangePasswordRequest request);
    
    Page<Poll> getUserCreatedPolls(User user, Pageable pageable);
    
    Page<Poll> getUserVotedPolls(User user, Pageable pageable);
    
    Page<Poll> getUserFavoritePolls(User user, Pageable pageable);
    
    void addFavorite(User user, UUID pollId);
    
    void removeFavorite(User user, UUID pollId);
    
    boolean isFavorite(User user, UUID pollId);
    
    List<VoterResponse> getPollVoters(UUID pollId, User requester);
}
