package com.vargas.leo.gerenciadorassembleia.repository;

import com.vargas.leo.gerenciadorassembleia.domain.Vote;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class VoteRepository {

    private final List<Vote> voteRepository = new ArrayList<>();
    private final VotingSessionRepository votingSessionRepository;
    private final UserRepository userRepository;

    public Vote save(Vote agenda) {
        voteRepository.add(agenda);
        return agenda;
    }

    public Vote findById(String voteId) {
        for (Vote v : voteRepository) {
            if (v.getId().equals(voteId)) {
                return v;
            }
        }
        return null;
    }

    public Optional<Vote> findByUserIdAndVotingSessionId(String userId, String votingSession) {
        for (Vote v : voteRepository) {
            if (v.getUser().getId().equals(userId)) {
                if (v.getVotingSession().getId().equals(votingSession)) {
                    return Optional.of(v);
                }
            }
        }
        return Optional.empty();
    }

}
