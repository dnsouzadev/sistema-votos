package com.dnsouzadev.sistemavoto.service.impl;

import com.dnsouzadev.sistemavoto.dto.request.ChangePasswordRequest;
import com.dnsouzadev.sistemavoto.dto.request.UpdateProfileRequest;
import com.dnsouzadev.sistemavoto.dto.response.UserProfileResponse;
import com.dnsouzadev.sistemavoto.dto.response.VoterResponse;
import com.dnsouzadev.sistemavoto.model.Poll;
import com.dnsouzadev.sistemavoto.model.User;
import com.dnsouzadev.sistemavoto.model.UserFavoritePoll;
import com.dnsouzadev.sistemavoto.model.Vote;
import com.dnsouzadev.sistemavoto.repository.PollRepository;
import com.dnsouzadev.sistemavoto.repository.UserFavoritePollRepository;
import com.dnsouzadev.sistemavoto.repository.UserRepository;
import com.dnsouzadev.sistemavoto.repository.VoteRepository;
import com.dnsouzadev.sistemavoto.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PollRepository pollRepository;
    private final VoteRepository voteRepository;
    private final UserFavoritePollRepository favoritePollRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
    }
    
    @Override
    public UserProfileResponse getUserProfile(User user) {
        long totalPolls = pollRepository.findByCreatedBy(user).size();
        long totalVotes = voteRepository.countByVotedBy(user);
        long totalFavorites = favoritePollRepository.countByUser(user);
        
        return UserProfileResponse.builder()
                .id(user.getId())
                .username(user.getName())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .totalPollsCreated(totalPolls)
                .totalVotesCast(totalVotes)
                .totalFavorites(totalFavorites)
                .build();
    }
    
    @Transactional
    @Override
    public User updateProfile(User user, UpdateProfileRequest request) {
        // Verificar se email já existe (se mudou)
        if (!user.getEmail().equals(request.getEmail())) {
            Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
            if (existingUser.isPresent()) {
                throw new RuntimeException("Email já está em uso");
            }
        }
        
        // Verificar se username já existe (se mudou)
        if (!user.getName().equals(request.getUsername())) {
            Optional<User> existingUser = userRepository.findByUsername(request.getUsername());
            if (existingUser.isPresent()) {
                throw new RuntimeException("Username já está em uso");
            }
        }
        
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        
        return userRepository.save(user);
    }
    
    @Transactional
    @Override
    public void changePassword(User user, ChangePasswordRequest request) {
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Senha atual incorreta");
        }
        
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Nova senha e confirmação não coincidem");
        }
        
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
    
    @Override
    public Page<Poll> getUserCreatedPolls(User user, Pageable pageable) {
        return pollRepository.findByCreatedByOrderByCreatedAtDesc(user, pageable);
    }
    
    @Override
    public Page<Poll> getUserVotedPolls(User user, Pageable pageable) {
        return voteRepository.findPollsVotedByUser(user, pageable);
    }
    
    @Override
    public Page<Poll> getUserFavoritePolls(User user, Pageable pageable) {
        return favoritePollRepository.findFavoritesPollsByUser(user, pageable);
    }
    
    @Transactional
    @Override
    public void addFavorite(User user, UUID pollId) {
        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new RuntimeException("Votação não encontrada"));
        
        if (favoritePollRepository.existsByUserAndPoll(user, poll)) {
            throw new RuntimeException("Votação já está nos favoritos");
        }
        
        UserFavoritePoll favorite = UserFavoritePoll.builder()
                .user(user)
                .poll(poll)
                .build();
        
        favoritePollRepository.save(favorite);
    }
    
    @Transactional
    @Override
    public void removeFavorite(User user, UUID pollId) {
        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new RuntimeException("Votação não encontrada"));
        
        favoritePollRepository.deleteByUserAndPoll(user, poll);
    }
    
    @Override
    public boolean isFavorite(User user, UUID pollId) {
        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new RuntimeException("Votação não encontrada"));
        
        return favoritePollRepository.existsByUserAndPoll(user, poll);
    }
    
    @Override
    public List<VoterResponse> getPollVoters(UUID pollId, User requester) {
        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new RuntimeException("Votação não encontrada"));
        
        // Verificar se a votação é anônima
        if (poll.isAnonymous()) {
            throw new RuntimeException("Esta votação é anônima");
        }
        
        // Verificar se o usuário pode ver os resultados
        boolean canSee = poll.isPublicResults() || 
                        poll.getCreatedBy().getId().equals(requester.getId());
        
        if (!canSee) {
            throw new RuntimeException("Você não tem permissão para ver quem votou");
        }
        
        List<Vote> votes = voteRepository.findVotersByPoll(poll);
        
        return votes.stream()
                .map(vote -> VoterResponse.builder()
                        .userId(vote.getVotedBy().getId())
                        .username(vote.getVotedBy().getName())
                        .selectedOption(vote.getSelectedOption().getOptionText())
                        .selectedOptionId(vote.getSelectedOption().getId())
                        .votedAt(vote.getVotedAt())
                        .build())
                .collect(Collectors.toList());
    }
}

