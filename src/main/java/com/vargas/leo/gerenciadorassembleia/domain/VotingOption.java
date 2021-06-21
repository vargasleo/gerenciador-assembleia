package com.vargas.leo.gerenciadorassembleia.domain;

public enum VotingOption {

    yes("agree with the subject of agenda"),
    no("disagree with the subject of agenda");

    private final String option;

    VotingOption(String option) {
        this.option = option;
    }

    public String getDescription() {
        return this.option;
    }
}
