package com.dnsouzadev.sistemavoto.service;

import com.dnsouzadev.sistemavoto.model.Poll;
import com.dnsouzadev.sistemavoto.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface PollService {

    Poll createPoll(Poll poll, User creator);

    Poll getPollById(UUID pollId);

    List<Poll> listPollsByUser(User user);

    boolean canUserSeeResults(Poll poll, User user);
    
    Page<Poll> listPublicPolls(Pageable pageable, boolean onlyActive);
    
    Poll updatePoll(UUID pollId, Poll updatedPoll, User user);
    
    void deletePoll(UUID pollId, User user);
    
    Poll closePoll(UUID pollId, User user);
    
    Poll toggleFeatured(UUID pollId, User user);
    
    Page<Poll> listPollsByStatus(Pageable pageable, String status, User user);
}
