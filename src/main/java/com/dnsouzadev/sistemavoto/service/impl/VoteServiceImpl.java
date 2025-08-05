package com.dnsouzadev.sistemavoto.service.impl;

import com.dnsouzadev.sistemavoto.dto.response.PollOptionResultResponse;
import com.dnsouzadev.sistemavoto.dto.response.PollResultResponse;
import com.dnsouzadev.sistemavoto.model.Poll;
import com.dnsouzadev.sistemavoto.model.PollOption;
import com.dnsouzadev.sistemavoto.model.User;
import com.dnsouzadev.sistemavoto.model.Vote;
import com.dnsouzadev.sistemavoto.repository.PollOptionRepository;
import com.dnsouzadev.sistemavoto.repository.PollRepository;
import com.dnsouzadev.sistemavoto.repository.VoteRepository;
import com.dnsouzadev.sistemavoto.service.PollService;
import com.dnsouzadev.sistemavoto.service.VoteService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VoteServiceImpl implements VoteService {

    private final VoteRepository voteRepository;
    private final PollRepository pollRepository;
    private final PollOptionRepository optionRepository;
    private final PollService pollService;

    @Transactional
    @Override
    public Vote registerVote(UUID pollId, UUID optionId, User user) {
        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new RuntimeException("Votação não encontrada."));

        if (poll.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("A votação já expirou.");
        }

        PollOption option = optionRepository.findById(optionId)
                .orElseThrow(() -> new RuntimeException("Opção inválida."));

        if (!option.getPoll().getId().equals(pollId)) {
            throw new IllegalArgumentException("Essa opção não pertence à votação.");
        }

        if (!poll.isAnonymous() && voteRepository.findByPollAndVotedBy(poll, user).isPresent()) {
            throw new IllegalStateException("Você já votou nesta votação.");
        }

        Vote vote = Vote.builder()
                .poll(poll)
                .selectedOption(option)
                .votedBy(poll.isAnonymous() ? null : user)
                .build();

        return voteRepository.save(vote);
    }

    @Override
    public PollResultResponse getPollResults(UUID pollId, User requester) {
        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new RuntimeException("Votação não encontrada"));

        boolean isOwner = poll.getCreatedBy().getId().equals(requester.getId());
        if (!poll.isPublicResults() && !isOwner) {
            throw new RuntimeException("Resultados privados");
        }

        long totalVotes = voteRepository.countByPoll(poll);

        List<PollOptionResultResponse> options = poll.getOptions().stream().map(option -> {
            long voteCount = voteRepository.countBySelectedOption(option);
            double percentage = totalVotes > 0 ? (voteCount * 100.0) / totalVotes : 0;
            return PollOptionResultResponse.builder()
                    .optionId(option.getId())
                    .text(option.getOptionText())
                    .votes(voteCount)
                    .percentage(Math.round(percentage * 10.0) / 10.0)
                    .build();
        }).toList();

        return PollResultResponse.builder()
                .totalVotes(totalVotes)
                .options(options)
                .build();
    }

    @Override
    public boolean hasUserVoted(UUID pollId, User user) {
        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new RuntimeException("Votação não encontrada."));

        return voteRepository.findByPollAndVotedBy(poll, user).isPresent();
    }
}