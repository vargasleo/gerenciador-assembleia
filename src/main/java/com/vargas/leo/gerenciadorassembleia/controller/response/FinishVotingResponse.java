package com.vargas.leo.gerenciadorassembleia.controller.response;

import com.vargas.leo.gerenciadorassembleia.domain.enums.VotingResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
public class FinishVotingResponse {

    private final LocalDateTime endedAt;
    private final VotingResult winnerOption;
    private final VotingResult looserOption;
    private final int yesVotes;
    private final int noVotes;

}
