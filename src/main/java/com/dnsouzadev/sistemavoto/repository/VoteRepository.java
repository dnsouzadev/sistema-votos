package com.dnsouzadev.sistemavoto.repository;

import com.dnsouzadev.sistemavoto.model.Poll;
import com.dnsouzadev.sistemavoto.model.PollOption;
import com.dnsouzadev.sistemavoto.model.User;
import com.dnsouzadev.sistemavoto.model.Vote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VoteRepository extends JpaRepository<Vote, UUID> {
    long countByPollAndSelectedOptionId(Poll poll, UUID optionId);
    Optional<Vote> findByPollAndVotedBy(Poll poll, User user);
    long countByPoll(Poll poll);
    long countBySelectedOption(PollOption option);
    
    @Query("SELECT DISTINCT v.poll FROM Vote v WHERE v.votedBy = :user ORDER BY v.votedAt DESC")
    Page<Poll> findPollsVotedByUser(User user, Pageable pageable);
    
    @Query("SELECT v FROM Vote v WHERE v.poll = :poll AND v.votedBy IS NOT NULL ORDER BY v.votedAt DESC")
    List<Vote> findVotersByPoll(Poll poll);
    
    long countByVotedBy(User user);
}
