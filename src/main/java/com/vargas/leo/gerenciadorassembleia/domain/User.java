package com.vargas.leo.gerenciadorassembleia.domain;

import lombok.*;
import org.modelmapper.internal.bytebuddy.utility.RandomString;

@Getter
@Setter
@AllArgsConstructor
public class User {
    private final String id = RandomString.make();
    private final String name;
}