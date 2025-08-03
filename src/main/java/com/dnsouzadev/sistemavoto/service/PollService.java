package com.dnsouzadev.sistemavoto.service;

import com.dnsouzadev.sistemavoto.model.Poll;
import com.dnsouzadev.sistemavoto.model.User;

import java.util.List;
import java.util.UUID;

public interface PollService {

    Poll createPoll(Poll poll, User creator);

    Poll getPollById(UUID pollId);

    List<Poll> listPollsByUser(User user);

    boolean canUserSeeResults(Poll poll, User user);
}
