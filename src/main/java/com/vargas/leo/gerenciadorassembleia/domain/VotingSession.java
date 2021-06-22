package com.vargas.leo.gerenciadorassembleia.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.modelmapper.internal.bytebuddy.utility.RandomString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@RequiredArgsConstructor
public class VotingSession {

    public static final LocalDateTime DEFAULT_FINAL_DATE_TIME = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 3, 0));

    private final String id = RandomString.make();
    private final LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime finalDateTime = DEFAULT_FINAL_DATE_TIME;
    private VotingOption winnerOption;
    private VotingSessionStatus status;
    private final Agenda Agenda;
    private int yesVotes = 0;
    private int noVotes = 0;

}
