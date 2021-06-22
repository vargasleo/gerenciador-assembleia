package com.vargas.leo.gerenciadorassembleia.domain;

public enum VotingSessionStatus {

    opened("opened voting session"),
    closed("closed voting session");

    private final String status;

    VotingSessionStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return this.status;
    }
}
