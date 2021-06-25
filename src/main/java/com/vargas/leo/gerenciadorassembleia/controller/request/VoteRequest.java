package com.vargas.leo.gerenciadorassembleia.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
