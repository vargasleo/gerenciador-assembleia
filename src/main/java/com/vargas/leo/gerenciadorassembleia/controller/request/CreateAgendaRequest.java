package com.vargas.leo.gerenciadorassembleia.controller.request;

import lombok.*;

@Data
public class CreateAgendaRequest {

    private Integer userId;

    private String subject;

}
