package com.dnsouzadev.sistemavoto.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatePollRequest {
    
    @NotBlank(message = "Título é obrigatório")
    private String title;
    
    private String description;
    
    @NotNull(message = "Campo 'anonymous' é obrigatório")
    private Boolean anonymous;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime expirationDate;
    
    private Boolean publicResults;
    
    @NotEmpty(message = "Pelo menos duas opções são obrigatórias")
    private List<String> options;
}