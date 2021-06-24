package com.vargas.leo.gerenciadorassembleia.domain;

import lombok.*;
import org.modelmapper.internal.bytebuddy.utility.RandomString;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
public class Vote {

    private final String id = RandomString.make();
    private final User user;
    private final VotingSession votingSession;
    private final LocalDateTime votedAt = LocalDateTime.now();
    private VotingOption vote;

}
