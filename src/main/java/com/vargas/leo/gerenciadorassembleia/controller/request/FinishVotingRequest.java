package com.vargas.leo.gerenciadorassembleia.controller.request;

import lombok.*;

@Data
public class FinishVotingRequest {

    private Integer userId;

    private Integer votingSessionId;

}
