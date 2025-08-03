package com.dnsouzadev.sistemavoto.service.impl;

import com.dnsouzadev.sistemavoto.model.Poll;
import com.dnsouzadev.sistemavoto.model.PollOption;
import com.dnsouzadev.sistemavoto.model.User;
import com.dnsouzadev.sistemavoto.repository.PollOptionRepository;
import com.dnsouzadev.sistemavoto.repository.PollRepository;
import com.dnsouzadev.sistemavoto.service.PollService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PollServiceImpl implements PollService {
    private final PollRepository pollRepository;
    private final PollOptionRepository optionRepository;

    @Transactional
    @Override
    public Poll createPoll(Poll poll, User creator) {
        if (poll.getOptions() == null || poll.getOptions().size() < 2) {
            throw new IllegalArgumentException("Uma votação precisa ter ao menos duas opções.");
        }

        if (poll.getExpirationDate() == null || poll.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("A data de expiração deve ser futura.");
        }

        poll.setCreatedBy(creator);
        Poll savedPoll = pollRepository.save(poll);

        for (PollOption option : poll.getOptions()) {
            option.setPoll(savedPoll);
            optionRepository.save(option);
        }

        return savedPoll;
    }

    @Override
    public Poll getPollById(UUID pollId) {
        return pollRepository.findById(pollId)
                .orElseThrow(() -> new RuntimeException("Votação não encontrada."));
    }

    @Override
    public List<Poll> listPollsByUser(User user) {
        return pollRepository.findByCreatedBy(user);
    }

    @Override
    public boolean canUserSeeResults(Poll poll, User user) {
        if (poll.isPublicResults()) return true;
        if (user == null) return false;
        return poll.getCreatedBy().getId().equals(user.getId());
    }
}
