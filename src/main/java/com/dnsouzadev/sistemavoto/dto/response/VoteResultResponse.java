package com.dnsouzadev.sistemavoto.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class VoteResultResponse {

    private Map<String, Long> results; // ex: "Sim" -> 10, "Não" -> 5
}
