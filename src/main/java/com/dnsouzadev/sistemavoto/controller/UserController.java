package com.dnsouzadev.sistemavoto.controller;

import com.dnsouzadev.sistemavoto.dto.request.ChangePasswordRequest;
import com.dnsouzadev.sistemavoto.dto.request.UpdateProfileRequest;
import com.dnsouzadev.sistemavoto.dto.response.GenericMessageResponse;
import com.dnsouzadev.sistemavoto.dto.response.PollResponse;
import com.dnsouzadev.sistemavoto.dto.response.UserProfileResponse;
import com.dnsouzadev.sistemavoto.dto.response.VoterResponse;
import com.dnsouzadev.sistemavoto.model.Poll;
import com.dnsouzadev.sistemavoto.model.User;
import com.dnsouzadev.sistemavoto.service.UserService;
import com.dnsouzadev.sistemavoto.utils.AuthenticatedUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getProfile() {
        User user = AuthenticatedUser.get();
        UserProfileResponse profile = userService.getUserProfile(user);
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/profile")
    public ResponseEntity<UserProfileResponse> updateProfile(@RequestBody @Valid UpdateProfileRequest request) {
        User user = AuthenticatedUser.get();
        userService.updateProfile(user, request);
        UserProfileResponse updatedProfile = userService.getUserProfile(user);
        return ResponseEntity.ok(updatedProfile);
    }

    @PutMapping("/password")
    public ResponseEntity<GenericMessageResponse> changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        User user = AuthenticatedUser.get();
        userService.changePassword(user, request);
        return ResponseEntity.ok(new GenericMessageResponse("Senha alterada com sucesso"));
    }

    @GetMapping("/polls/created")
    public ResponseEntity<Page<PollResponse>> getCreatedPolls(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        User user = AuthenticatedUser.get();
        Pageable pageable = PageRequest.of(page, size);
        Page<Poll> polls = userService.getUserCreatedPolls(user, pageable);
        
        Page<PollResponse> response = polls.map(this::toResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/polls/voted")
    public ResponseEntity<Page<PollResponse>> getVotedPolls(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        User user = AuthenticatedUser.get();
        Pageable pageable = PageRequest.of(page, size);
        Page<Poll> polls = userService.getUserVotedPolls(user, pageable);
        
        Page<PollResponse> response = polls.map(this::toResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/polls/favorites")
    public ResponseEntity<Page<PollResponse>> getFavoritePolls(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        User user = AuthenticatedUser.get();
        Pageable pageable = PageRequest.of(page, size);
        Page<Poll> polls = userService.getUserFavoritePolls(user, pageable);
        
        Page<PollResponse> response = polls.map(this::toResponse);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/polls/{pollId}/favorite")
    public ResponseEntity<GenericMessageResponse> addFavorite(@PathVariable UUID pollId) {
        User user = AuthenticatedUser.get();
        userService.addFavorite(user, pollId);
        return ResponseEntity.ok(new GenericMessageResponse("Votação adicionada aos favoritos"));
    }

    @DeleteMapping("/polls/{pollId}/favorite")
    public ResponseEntity<GenericMessageResponse> removeFavorite(@PathVariable UUID pollId) {
        User user = AuthenticatedUser.get();
        userService.removeFavorite(user, pollId);
        return ResponseEntity.ok(new GenericMessageResponse("Votação removida dos favoritos"));
    }

    @GetMapping("/polls/{pollId}/favorite/status")
    public ResponseEntity<Boolean> isFavorite(@PathVariable UUID pollId) {
        User user = AuthenticatedUser.get();
        boolean isFavorite = userService.isFavorite(user, pollId);
        return ResponseEntity.ok(isFavorite);
    }

    @GetMapping("/polls/{pollId}/voters")
    public ResponseEntity<List<VoterResponse>> getPollVoters(@PathVariable UUID pollId) {
        User user = AuthenticatedUser.get();
        List<VoterResponse> voters = userService.getPollVoters(pollId, user);
        return ResponseEntity.ok(voters);
    }

    private PollResponse toResponse(Poll poll) {
        return PollResponse.builder()
                .id(poll.getId())
                .title(poll.getTitle())
                .description(poll.getDescription())
                .anonymous(poll.isAnonymous())
                .publicResults(poll.isPublicResults())
                .featured(poll.isFeatured())
                .active(poll.isActive())
                .expirationDate(poll.getExpirationDate())
                .createdAt(poll.getCreatedAt())
                .options(
                        poll.getOptions().stream().map(opt ->
                                PollResponse.OptionResponse.builder()
                                        .id(opt.getId())
                                        .optionText(opt.getOptionText())
                                        .build()
                        ).collect(Collectors.toList())
                )
                .build();
    }
}