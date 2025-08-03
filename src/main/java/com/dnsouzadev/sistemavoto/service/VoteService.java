package com.dnsouzadev.sistemavoto.service;

import com.dnsouzadev.sistemavoto.model.User;
import com.dnsouzadev.sistemavoto.model.Vote;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface VoteService {

    Vote registerVote(UUID pollId, UUID optionId, User user);

    Map<String, Long> getPollResults(UUID pollId, User user);

    boolean hasUserVoted(UUID pollId, User user);
}
