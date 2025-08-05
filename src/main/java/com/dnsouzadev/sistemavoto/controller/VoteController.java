package com.dnsouzadev.sistemavoto.controller;

import com.dnsouzadev.sistemavoto.dto.request.VoteRequest;
import com.dnsouzadev.sistemavoto.dto.response.GenericMessageResponse;
import com.dnsouzadev.sistemavoto.dto.response.PollResultResponse;
import com.dnsouzadev.sistemavoto.dto.response.VoteResultResponse;
import com.dnsouzadev.sistemavoto.model.User;
import com.dnsouzadev.sistemavoto.service.VoteService;
import com.dnsouzadev.sistemavoto.utils.AuthenticatedUser;
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

    @PostMapping
    public ResponseEntity<GenericMessageResponse> vote(
            @PathVariable UUID pollId,
            @RequestBody @Valid VoteRequest request
    ) {
        User user = AuthenticatedUser.get();

        voteService.registerVote(pollId, request.getOptionId(), user);

        return ResponseEntity.ok(new GenericMessageResponse("Voto registrado com sucesso."));
    }

    @GetMapping("/results")
    public ResponseEntity<PollResultResponse> getResults(@PathVariable UUID pollId) {
        User user = AuthenticatedUser.get();
        PollResultResponse results = voteService.getPollResults(pollId, user);
        return ResponseEntity.ok(results);
    }
}
