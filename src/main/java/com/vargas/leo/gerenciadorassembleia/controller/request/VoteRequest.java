package com.vargas.leo.gerenciadorassembleia.controller.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class VoteRequest {

    @NotNull(message = "user.id.must.not.be.null")
    private Integer userId;

    @NotNull(message = "voting.session.id.must.not.be.null")
    private Integer votingSessionId;

    @NotNull(message = "voting.option.must.not.be.null")
    @NotEmpty(message = "voting.option.must.not.be.empty")
    private String vote;

}
