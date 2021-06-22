package com.vargas.leo.gerenciadorassembleia.domain;

public enum AgendaStatus {

    created("created agenda"),
    open("opened agenda"),
    closed("closed agenda");

    private final String status;

    AgendaStatus(String status) {
        this.status = status;
    }

}
