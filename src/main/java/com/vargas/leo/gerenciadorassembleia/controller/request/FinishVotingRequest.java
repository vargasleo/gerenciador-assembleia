package com.vargas.leo.gerenciadorassembleia.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FinishVotingRequest {

    @NotNull(message = "user.id.must.not.be.null")
    private Integer userId;

    @NotNull(message = "voting.session.id.must.not.be.null")
    private Integer votingSessionId;

}
