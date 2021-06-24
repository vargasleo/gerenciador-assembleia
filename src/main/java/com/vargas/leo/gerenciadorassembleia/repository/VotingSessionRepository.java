package com.vargas.leo.gerenciadorassembleia.repository;

import com.vargas.leo.gerenciadorassembleia.domain.VotingSession;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface VotingSessionRepository extends Repository<VotingSession, Integer> {

    VotingSession save(VotingSession votingSession);

    Optional<VotingSession> findById(String votingSessionId);

}
