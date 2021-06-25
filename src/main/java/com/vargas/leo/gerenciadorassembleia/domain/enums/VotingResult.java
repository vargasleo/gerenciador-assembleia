package com.vargas.leo.gerenciadorassembleia.domain.enums;

public enum VotingResult {

    yes("agree with the subject of agenda"),
    no("disagree with the subject of agenda"),
    draw("there are no winner");

    VotingResult(String result) {
    }
}
