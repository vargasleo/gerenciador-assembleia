package com.vargas.leo.gerenciadorassembleia.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@RequiredArgsConstructor
public class VotingSession {

    public static final LocalDateTime DEFAULT_FINAL_DATE_TIME = LocalDateTime.of(LocalDate.now(), LocalTime.of(0,3,0));

    private final LocalDateTime createdAt = LocalDateTime.now();
    private final Agenda Agenda;
    private LocalDateTime finalDateTime = DEFAULT_FINAL_DATE_TIME;
    private VotingOption winnerOption;
    private int yesVotes;
    private int noVotes;
}
