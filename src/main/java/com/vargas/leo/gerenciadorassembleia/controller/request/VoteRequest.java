package com.vargas.leo.gerenciadorassembleia.controller.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@RequiredArgsConstructor
public class VoteRequest {

    @NotNull(message = "must.not.be.null")
    @NotEmpty(message = "must.not.be.empty")
    private final String userId;

    @NotNull(message = "must.not.be.null")
    @NotEmpty(message = "must.not.be.empty")
    private final String votingSessionId;

    @NotNull(message = "must.not.be.null")
    @NotEmpty(message = "must.not.be.empty")
    private final String vote;

}
