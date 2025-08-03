package com.dnsouzadev.sistemavoto.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class VoteRequest {

    @NotNull
    private UUID optionId;
}