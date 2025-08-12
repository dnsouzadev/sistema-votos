package com.dnsouzadev.sistemavoto.controller;


import com.dnsouzadev.sistemavoto.dto.request.CreatePollRequest;
import com.dnsouzadev.sistemavoto.dto.request.UpdatePollRequest;
import com.dnsouzadev.sistemavoto.dto.response.GenericMessageResponse;
import com.dnsouzadev.sistemavoto.dto.response.PollResponse;
import com.dnsouzadev.sistemavoto.model.Poll;
import com.dnsouzadev.sistemavoto.model.PollOption;
import com.dnsouzadev.sistemavoto.model.User;
import com.dnsouzadev.sistemavoto.service.PollService;
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
@RequestMapping("/polls")
@RequiredArgsConstructor
public class PollController {

    private final PollService pollService;

    @PostMapping
    public ResponseEntity<PollResponse> createPoll(@RequestBody @Valid CreatePollRequest request) {
        User creator = AuthenticatedUser.get();

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
        User user = AuthenticatedUser.get();
        List<Poll> polls = pollService.listPollsByUser(user);

        return ResponseEntity.ok(
                polls.stream().map(this::toResponse).collect(Collectors.toList())
        );
    }
    
    @GetMapping("/public")
    public ResponseEntity<Page<PollResponse>> getPublicPolls(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "false") boolean onlyActive
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Poll> polls = pollService.listPublicPolls(pageable, onlyActive);
        
        Page<PollResponse> response = polls.map(this::toResponse);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<PollResponse>> getPollsByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        User user = AuthenticatedUser.get();
        Pageable pageable = PageRequest.of(page, size);
        Page<Poll> polls = pollService.listPollsByStatus(pageable, status, user);
        
        Page<PollResponse> response = polls.map(this::toResponse);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<PollResponse> updatePoll(
            @PathVariable UUID id,
            @RequestBody @Valid UpdatePollRequest request
    ) {
        User user = AuthenticatedUser.get();

        Poll updatedPoll = Poll.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .anonymous(request.getAnonymous())
                .publicResults(request.getPublicResults() != null && request.getPublicResults())
                .expirationDate(request.getExpirationDate())
                .options(
                        request.getOptions().stream()
                                .map(opt -> PollOption.builder().optionText(opt).build())
                                .collect(Collectors.toList())
                )
                .build();

        Poll updated = pollService.updatePoll(id, updatedPoll, user);
        return ResponseEntity.ok(toResponse(updated));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<GenericMessageResponse> deletePoll(@PathVariable UUID id) {
        User user = AuthenticatedUser.get();
        pollService.deletePoll(id, user);
        return ResponseEntity.ok(new GenericMessageResponse("Votação excluída com sucesso"));
    }
    
    @PutMapping("/{id}/close")
    public ResponseEntity<PollResponse> closePoll(@PathVariable UUID id) {
        User user = AuthenticatedUser.get();
        Poll closedPoll = pollService.closePoll(id, user);
        return ResponseEntity.ok(toResponse(closedPoll));
    }
    
    @PutMapping("/{id}/featured")
    public ResponseEntity<PollResponse> toggleFeatured(@PathVariable UUID id) {
        User user = AuthenticatedUser.get();
        Poll poll = pollService.toggleFeatured(id, user);
        return ResponseEntity.ok(toResponse(poll));
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
