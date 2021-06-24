package com.vargas.leo.gerenciadorassembleia.controller.response;

import com.vargas.leo.gerenciadorassembleia.domain.VotingOption;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
public class VoteResponse {

    private final VotingOption votingOption;
    private final LocalDateTime votedAt;

}
