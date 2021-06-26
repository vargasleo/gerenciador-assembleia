package com.vargas.leo.gerenciadorassembleia.controller.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Data
public class CreateVotingSessionRequest {

    private Integer agendaId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime finalDateTime;

}
