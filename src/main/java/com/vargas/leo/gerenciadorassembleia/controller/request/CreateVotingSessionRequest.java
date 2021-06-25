package com.vargas.leo.gerenciadorassembleia.controller.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateVotingSessionRequest {

    @NotNull(message = "must.not.be.null")
    @NotEmpty(message = "must.not.be.empty")
    private Integer agendaId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime finalDateTime;

}
