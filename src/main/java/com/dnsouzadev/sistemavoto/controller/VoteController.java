package com.dnsouzadev.sistemavoto.controller;

import com.dnsouzadev.sistemavoto.dto.request.VoteRequest;
import com.dnsouzadev.sistemavoto.dto.response.GenericMessageResponse;
import com.dnsouzadev.sistemavoto.dto.response.VoteResultResponse;
import com.dnsouzadev.sistemavoto.model.User;
import com.dnsouzadev.sistemavoto.service.VoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/polls/{pollId}/vote")
@RequiredArgsConstructor
public class VoteController {

    private final VoteService voteService;

    private User getAuthenticatedUser() {
        return User.builder()
                .id(UUID.fromString("11111111-1111-1111-1111-111111111111"))
                .name("Usuário Fictício")
                .email("teste@exemplo.com")
                .build();
    }

    @PostMapping
    public ResponseEntity<GenericMessageResponse> vote(
            @PathVariable UUID pollId,
            @RequestBody @Valid VoteRequest request
    ) {
        User user = getAuthenticatedUser();

        voteService.registerVote(pollId, request.getOptionId(), user);

        return ResponseEntity.ok(new GenericMessageResponse("Voto registrado com sucesso."));
    }

    @GetMapping("/results")
    public ResponseEntity<VoteResultResponse> getResults(@PathVariable UUID pollId) {
        User user = getAuthenticatedUser();

        var results = voteService.getPollResults(pollId, user);
        return ResponseEntity.ok(VoteResultResponse.builder().results(results).build());
    }
}
