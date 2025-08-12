package com.dnsouzadev.sistemavoto.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private boolean featured;
    private boolean active;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime expirationDate;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    
    private List<OptionResponse> options;

    @Data
    @Builder
    public static class OptionResponse {
        private UUID id;
        private String optionText;
    }
}