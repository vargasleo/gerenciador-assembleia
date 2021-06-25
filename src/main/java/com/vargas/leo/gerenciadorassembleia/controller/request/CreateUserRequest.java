package com.vargas.leo.gerenciadorassembleia.controller.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreateUserRequest {

    @NotNull(message = "must.not.be.null")
    @NotEmpty(message = "must.not.be.empty")
    private String name;

}
