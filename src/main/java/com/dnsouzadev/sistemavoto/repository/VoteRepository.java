package com.dnsouzadev.sistemavoto.repository;

import com.dnsouzadev.sistemavoto.model.Poll;
import com.dnsouzadev.sistemavoto.model.User;
import com.dnsouzadev.sistemavoto.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VoteRepository extends JpaRepository<Vote, UUID> {
    long countByPoll(Poll poll);
    long countByPollAndSelectedOptionId(Poll poll, UUID optionId);
    Optional<Vote> findByPollAndVotedBy(Poll poll, User user);
}
