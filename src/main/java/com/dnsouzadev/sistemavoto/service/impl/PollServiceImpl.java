package com.dnsouzadev.sistemavoto.service.impl;

import com.dnsouzadev.sistemavoto.model.Poll;
import com.dnsouzadev.sistemavoto.model.PollOption;
import com.dnsouzadev.sistemavoto.model.User;
import com.dnsouzadev.sistemavoto.repository.PollOptionRepository;
import com.dnsouzadev.sistemavoto.repository.PollRepository;
import com.dnsouzadev.sistemavoto.repository.VoteRepository;
import com.dnsouzadev.sistemavoto.service.PollService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PollServiceImpl implements PollService {
    private final PollRepository pollRepository;
    private final PollOptionRepository optionRepository;
    private final VoteRepository voteRepository;

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
    
    @Override
    public Page<Poll> listPublicPolls(Pageable pageable, boolean onlyActive) {
        if (onlyActive) {
            return pollRepository.findPublicActivePolls(LocalDateTime.now(), pageable);
        } else {
            return pollRepository.findAllPublicPolls(pageable);
        }
    }
    
    @Transactional
    @Override
    public Poll updatePoll(UUID pollId, Poll updatedPoll, User user) {
        Poll existingPoll = pollRepository.findById(pollId)
                .orElseThrow(() -> new RuntimeException("Votação não encontrada"));
        
        if (!existingPoll.getCreatedBy().getId().equals(user.getId())) {
            throw new RuntimeException("Você não tem permissão para editar esta votação");
        }
        
        // Verifica se já há votos
        long voteCount = voteRepository.countByPoll(existingPoll);
        if (voteCount > 0) {
            throw new RuntimeException("Não é possível editar uma votação que já possui votos");
        }
        
        // Atualiza os campos
        existingPoll.setTitle(updatedPoll.getTitle());
        existingPoll.setDescription(updatedPoll.getDescription());
        existingPoll.setAnonymous(updatedPoll.isAnonymous());
        existingPoll.setExpirationDate(updatedPoll.getExpirationDate());
        existingPoll.setPublicResults(updatedPoll.isPublicResults());
        
        // Remove opções antigas e adiciona as novas
        optionRepository.deleteAll(existingPoll.getOptions());
        existingPoll.getOptions().clear();
        
        for (PollOption option : updatedPoll.getOptions()) {
            option.setPoll(existingPoll);
            optionRepository.save(option);
            existingPoll.getOptions().add(option);
        }
        
        return pollRepository.save(existingPoll);
    }
    
    @Transactional
    @Override
    public void deletePoll(UUID pollId, User user) {
        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new RuntimeException("Votação não encontrada"));
        
        if (!poll.getCreatedBy().getId().equals(user.getId())) {
            throw new RuntimeException("Você não tem permissão para excluir esta votação");
        }
        
        pollRepository.delete(poll);
    }
    
    @Transactional
    @Override
    public Poll closePoll(UUID pollId, User user) {
        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new RuntimeException("Votação não encontrada"));
        
        if (!poll.getCreatedBy().getId().equals(user.getId())) {
            throw new RuntimeException("Você não tem permissão para encerrar esta votação");
        }
        
        poll.setActive(false);
        return pollRepository.save(poll);
    }
    
    @Transactional
    @Override
    public Poll toggleFeatured(UUID pollId, User user) {
        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new RuntimeException("Votação não encontrada"));
        
        if (!poll.getCreatedBy().getId().equals(user.getId())) {
            throw new RuntimeException("Você não tem permissão para destacar esta votação");
        }
        
        poll.setFeatured(!poll.isFeatured());
        return pollRepository.save(poll);
    }
    
    @Override
    public Page<Poll> listPollsByStatus(Pageable pageable, String status, User user) {
        LocalDateTime now = LocalDateTime.now();
        
        return switch (status.toLowerCase()) {
            case "active", "ativas" -> pollRepository.findActiveByCreatedBy(user, now, pageable);
            case "closed", "encerradas" -> pollRepository.findClosedByCreatedBy(user, now, pageable);
            default -> throw new IllegalArgumentException("Status inválido. Use 'active' ou 'closed'");
        };
    }
}
