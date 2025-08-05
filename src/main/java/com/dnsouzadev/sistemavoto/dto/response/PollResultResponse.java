package com.dnsouzadev.sistemavoto.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PollResultResponse {
    private long totalVotes;
    private List<PollOptionResultResponse> options;
}