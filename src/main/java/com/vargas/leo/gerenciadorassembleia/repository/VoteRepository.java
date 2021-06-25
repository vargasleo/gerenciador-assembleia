package com.vargas.leo.gerenciadorassembleia.repository;

import com.vargas.leo.gerenciadorassembleia.domain.Vote;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface VoteRepository extends Repository<Vote, Integer> {

    Vote save(Vote agenda);

    Optional<Vote> findByUserIdAndVotingSessionId(Integer userId, Integer votingSession);

}
