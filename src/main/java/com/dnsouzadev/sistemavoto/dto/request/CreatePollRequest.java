package com.dnsouzadev.sistemavoto.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CreatePollRequest {

    @NotBlank
    private String title;

    private String description;

    private boolean anonymous;

    private Boolean publicResults = true; // opcional, default true

    @Future(message = "A data de expiração deve ser futura")
    private LocalDateTime expirationDate;

    @NotEmpty(message = "A votação precisa de pelo menos duas opções")
    private List<@NotBlank String> options;
}
