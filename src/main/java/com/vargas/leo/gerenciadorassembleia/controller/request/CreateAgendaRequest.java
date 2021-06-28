package com.vargas.leo.gerenciadorassembleia.controller.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CreateAgendaRequest {

    @NotNull(message = "user.id.must.not.be.null")
    private Integer userId;

    @NotNull(message = "subject.must.not.be.null")
    @NotEmpty(message = "subject.must.not.be.empty")
    private String subject;

}
