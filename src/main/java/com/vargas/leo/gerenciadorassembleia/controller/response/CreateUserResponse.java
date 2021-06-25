package com.vargas.leo.gerenciadorassembleia.controller.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class CreateUserResponse {

    private final Integer id;
    private final String name;

}
