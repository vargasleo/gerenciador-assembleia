package com.vargas.leo.gerenciadorassembleia.controller.response;

import com.vargas.leo.gerenciadorassembleia.domain.enums.VotingOption;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
public class VoteResponse {

    private final VotingOption votingOption;
    private final LocalDateTime votedAt;

}
