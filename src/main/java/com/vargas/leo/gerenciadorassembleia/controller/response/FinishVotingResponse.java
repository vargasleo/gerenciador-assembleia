package com.vargas.leo.gerenciadorassembleia.controller.response;

import com.vargas.leo.gerenciadorassembleia.domain.enums.VotingResult;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
public class FinishVotingResponse {

    private final LocalDateTime endedAt;
    private final VotingResult winnerOption;
    private final int yesVotes;
    private final int noVotes;

}
