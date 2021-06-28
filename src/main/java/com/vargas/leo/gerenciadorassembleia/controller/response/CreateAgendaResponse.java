package com.vargas.leo.gerenciadorassembleia.controller.response;

import com.vargas.leo.gerenciadorassembleia.domain.enums.AgendaStatus;
import lombok.*;

@Data
@Builder
public class CreateAgendaResponse {

    private String subject;
    private AgendaStatus status;
    private Integer id;

}
