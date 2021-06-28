package com.vargas.leo.gerenciadorassembleia.controller.response;

import lombok.*;

@Data
@Builder
public class CreateUserResponse {

    private final Integer id;
    private final String name;
    private final String cpf;

}
