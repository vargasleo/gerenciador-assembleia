package com.vargas.leo.gerenciadorassembleia.repository;

import com.vargas.leo.gerenciadorassembleia.domain.VotingSession;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class VotingSessionRepository {

    private final List<VotingSession> votingSessionRepository = new ArrayList<>();

    public VotingSession save(VotingSession votingSession) {
        votingSessionRepository.add(votingSession);
        return votingSession;
    }
}
