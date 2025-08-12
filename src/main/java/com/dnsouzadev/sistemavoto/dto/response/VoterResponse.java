package com.dnsouzadev.sistemavoto.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VoterResponse {
    
    private UUID userId;
    private String username;
    private String selectedOption;
    private UUID selectedOptionId;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime votedAt;
}