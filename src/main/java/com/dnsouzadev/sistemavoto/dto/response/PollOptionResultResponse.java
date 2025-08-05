package com.dnsouzadev.sistemavoto.dto.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PollOptionResultResponse {
    private UUID optionId;
    private String text;
    private long votes;
    private double percentage;
}