package com.vargas.leo.gerenciadorassembleia.service;

import com.vargas.leo.gerenciadorassembleia.controller.request.FinishVotingRequest;
import com.vargas.leo.gerenciadorassembleia.controller.response.FinishVotingResponse;
import com.vargas.leo.gerenciadorassembleia.domain.*;
import com.vargas.leo.gerenciadorassembleia.domain.enums.AgendaStatus;
import com.vargas.leo.gerenciadorassembleia.domain.enums.VotingResult;
import com.vargas.leo.gerenciadorassembleia.domain.enums.VotingSessionStatus;
import com.vargas.leo.gerenciadorassembleia.exception.NotFoundException;
import com.vargas.leo.gerenciadorassembleia.repository.UserRepository;
import com.vargas.leo.gerenciadorassembleia.repository.VotingSessionRepository;
import com.vargas.leo.gerenciadorassembleia.validator.VotingSessionValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FinishVotingService {

    private final UserRepository userRepository;
    private final VotingSessionRepository votingSessionRepository;
    private final VotingSessionValidator votingSessionValidator;

    public FinishVotingResponse finishVoting(FinishVotingRequest finishVotingRequest) {
        VotingSession votingSession = this.finishVotingAndCountVotes(finishVotingRequest);

        return FinishVotingResponse.builder()
                .endedAt(LocalDateTime.now())
                .winnerOption(votingSession.getResult())
                .noVotes(votingSession.getNoVotes())
                .yesVotes(votingSession.getYesVotes())
                .build();
    }

    protected VotingSession finishVotingAndCountVotes(FinishVotingRequest finishVotingRequest) {

        userRepository.findById(finishVotingRequest.getUserId())
                .orElseThrow(() -> new NotFoundException("user.not.found"));

        VotingSession votingSession = votingSessionRepository.findById(finishVotingRequest.getVotingSessionId())
                .orElseThrow(() -> new NotFoundException("voting.session.not.found"));

        votingSessionValidator.validateVotingSessionStatus(votingSession);

        this.defineVotingSessionResult(votingSession);

        this.closeVotingSession(votingSession);

        votingSessionRepository.save(votingSession);

        return votingSession;
    }

    private void closeVotingSession(VotingSession votingSession) {
        votingSession.setStatus(VotingSessionStatus.close);

        this.closeAgenda(votingSession.getAgenda());
    }

    private void closeAgenda(Agenda agenda) {
        agenda.setStatus(AgendaStatus.closed);
    }

    private void defineVotingSessionResult(VotingSession votingSession) {
        if (votingSession.getYesVotes() > votingSession.getNoVotes()) {
            votingSession.setResult(VotingResult.yes);
        } else if (votingSession.getNoVotes() > votingSession.getYesVotes()) {
            votingSession.setResult(VotingResult.no);
        } else {
            votingSession.setResult(VotingResult.draw);
        }
    }

}
