package com.vargas.leo.gerenciadorassembleia.controller.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CreateUserRequest {

    @NotEmpty(message = "name.must.not.be.empty")
    @NotNull(message = "name.must.not.be.null")
    private String name;

    @NotNull(message = "cpf.must.not.be.null")
    @NotEmpty(message = "cpf.must.not.be.empty")
    private String cpf;

}
