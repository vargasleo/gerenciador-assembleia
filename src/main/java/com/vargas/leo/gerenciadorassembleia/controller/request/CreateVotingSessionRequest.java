package com.vargas.leo.gerenciadorassembleia.controller.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
public class CreateVotingSessionRequest {

    @NotNull(message = "must.not.be.null")
    @NotEmpty(message = "must.not.be.empty")
    private final String agendaId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime finalDateTime;
}
