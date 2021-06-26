package com.vargas.leo.gerenciadorassembleia.controller.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class VoteRequest {

    @NotNull(message = "must.not.be.null")
    @NotEmpty(message = "must.not.be.empty")
    private Integer userId;

    @NotNull(message = "must.not.be.null")
    @NotEmpty(message = "must.not.be.empty")
    private Integer votingSessionId;

    @NotNull(message = "must.not.be.null")
    @NotEmpty(message = "must.not.be.empty")
    private String vote;

}
