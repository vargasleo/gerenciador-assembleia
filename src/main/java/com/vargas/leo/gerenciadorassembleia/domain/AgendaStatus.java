package com.vargas.leo.gerenciadorassembleia.domain;

public enum AgendaStatus {

    open("opened agenda"),
    closed("closed agenda");

    private final String status;

    AgendaStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return this.status;
    }
}
