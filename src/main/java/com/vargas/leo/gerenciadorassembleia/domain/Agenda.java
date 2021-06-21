package com.vargas.leo.gerenciadorassembleia.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Agenda {

    private String subject;
    private AgendaStatus status;
}