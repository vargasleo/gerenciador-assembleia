package com.vargas.leo.gerenciadorassembleia.domain;

import lombok.*;
import org.modelmapper.internal.bytebuddy.utility.RandomString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Agenda {

    private final String id = RandomString.make();
    private String subject;
    private AgendaStatus status;
}