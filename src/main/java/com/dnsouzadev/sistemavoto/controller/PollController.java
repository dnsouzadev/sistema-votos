package com.dnsouzadev.sistemavoto.controller;


import com.dnsouzadev.sistemavoto.dto.request.CreatePollRequest;
import com.dnsouzadev.sistemavoto.dto.response.PollResponse;
import com.dnsouzadev.sistemavoto.model.Poll;
import com.dnsouzadev.sistemavoto.model.PollOption;
import com.dnsouzadev.sistemavoto.model.User;
import com.dnsouzadev.sistemavoto.service.PollService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/polls")
@RequiredArgsConstructor
public class PollController {

    private final PollService pollService;

    // Simulando usuário autenticado (ajuste para Spring Security real depois)
    private User getAuthenticatedUser() {
        return User.builder()
                .id(UUID.fromString("11111111-1111-1131-1111-111111111111"))
                .name("Usuário Fictício")
                .email("teste@exemplo.com")
                .build();
    }

    @PostMapping
    public ResponseEntity<PollResponse> createPoll(@RequestBody @Valid CreatePollRequest request) {
        User creator = getAuthenticatedUser();

        Poll poll = Poll.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .anonymous(request.isAnonymous())
                .publicResults(request.getPublicResults() != null && request.getPublicResults())
                .expirationDate(request.getExpirationDate())
                .options(
                        request.getOptions().stream()
                                .map(opt -> PollOption.builder().optionText(opt).build())
                                .collect(Collectors.toList())
                )
                .build();

        Poll created = pollService.createPoll(poll, creator);

        return ResponseEntity.ok(toResponse(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PollResponse> getPoll(@PathVariable UUID id) {
        Poll poll = pollService.getPollById(id);
        return ResponseEntity.ok(toResponse(poll));
    }

    @GetMapping("/user")
    public ResponseEntity<List<PollResponse>> getPollsByUser() {
        User user = getAuthenticatedUser();
        List<Poll> polls = pollService.listPollsByUser(user);

        return ResponseEntity.ok(
                polls.stream().map(this::toResponse).collect(Collectors.toList())
        );
    }

    private PollResponse toResponse(Poll poll) {
        return PollResponse.builder()
                .id(poll.getId())
                .title(poll.getTitle())
                .description(poll.getDescription())
                .anonymous(poll.isAnonymous())
                .publicResults(poll.isPublicResults())
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
