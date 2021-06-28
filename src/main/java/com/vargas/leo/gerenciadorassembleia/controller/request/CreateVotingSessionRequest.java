package com.vargas.leo.gerenciadorassembleia.controller.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class CreateVotingSessionRequest {

    @NotNull(message = "agenda.id.must.not.be.null")
    private Integer agendaId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime finalDateTime;

}
