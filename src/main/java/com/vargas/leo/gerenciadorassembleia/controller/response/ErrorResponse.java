package com.vargas.leo.gerenciadorassembleia.controller.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {

    private String message;

}
