package com.dnsouzadev.sistemavoto.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class PollResponse {

    private UUID id;
    private String title;
    private String description;
    private boolean anonymous;
    private boolean publicResults;
    private LocalDateTime expirationDate;
    private LocalDateTime createdAt;
    private List<OptionResponse> options;

    @Data
    @Builder
    public static class OptionResponse {
        private UUID id;
        private String optionText;
    }
}