package com.vargas.leo.gerenciadorassembleia.service;

import com.vargas.leo.gerenciadorassembleia.controller.request.FinishVotingRequest;
import com.vargas.leo.gerenciadorassembleia.controller.response.FinishVotingResponse;
import com.vargas.leo.gerenciadorassembleia.domain.*;
import com.vargas.leo.gerenciadorassembleia.domain.enums.AgendaStatus;
import com.vargas.leo.gerenciadorassembleia.domain.enums.VotingResult;
import com.vargas.leo.gerenciadorassembleia.domain.enums.VotingSessionStatus;
import com.vargas.leo.gerenciadorassembleia.exception.BusinessException;
import com.vargas.leo.gerenciadorassembleia.exception.NotFoundException;
import com.vargas.leo.gerenciadorassembleia.messaging.PublisherService;
import com.vargas.leo.gerenciadorassembleia.repository.UserRepository;
import com.vargas.leo.gerenciadorassembleia.repository.VotingSessionRepository;
import com.vargas.leo.gerenciadorassembleia.validator.VotingSessionValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class FinishVotingService {

    private final UserRepository userRepository;
    private final VotingSessionRepository votingSessionRepository;
    private final VotingSessionValidator votingSessionValidator;
    private final PublisherService publisherService;

    public FinishVotingResponse finishVoting(FinishVotingRequest finishVotingRequest) {
        VotingSession votingSession = this.finishVotingAndCountVotes(finishVotingRequest);

        FinishVotingResponse response = FinishVotingResponse.builder()
                .endedAt(LocalDateTime.now())
                .result(votingSession.getResult())
                .noVotes(votingSession.getNoVotes())
                .yesVotes(votingSession.getYesVotes())
                .id(votingSession.getId())
                .agendaSubject(votingSession.getAgenda().getSubject())
                .build();

        publisherService.insertOnQueue(response);

        return response;
    }

    @Transactional
    protected VotingSession finishVotingAndCountVotes(FinishVotingRequest finishVotingRequest) {
        userRepository.findById(finishVotingRequest.getUserId())
                .orElseThrow(() -> new NotFoundException("user.not.found"));

        VotingSession votingSession = votingSessionRepository.findById(finishVotingRequest.getVotingSessionId())
                .orElseThrow(() -> new NotFoundException("voting.session.not.found"));

        try {
            votingSessionValidator.validateVotingSessionStatus(votingSession);
        } catch (BusinessException e) {
            log.error(e.getMessage());
            throw e;
        }

        this.defineVotingSessionResult(votingSession);

        this.closeVotingSession(votingSession);

        votingSessionRepository.save(votingSession);

        return votingSession;
    }

    private void closeVotingSession(VotingSession votingSession) {
        votingSession.setStatus(VotingSessionStatus.closed);

        this.closeAgenda(votingSession.getAgenda());
    }

    private void closeAgenda(Agenda agenda) {
        agenda.setStatus(AgendaStatus.closed);
        log.info("closing.agenda");
    }

    private void defineVotingSessionResult(VotingSession votingSession) {
        if (votingSession.getYesVotes() > votingSession.getNoVotes()) {
            votingSession.setResult(VotingResult.yes);
            log.info("voting.session.result.is.yes");
        } else if (votingSession.getNoVotes() > votingSession.getYesVotes()) {
            votingSession.setResult(VotingResult.no);
            log.info("voting.session.result.is.no");
        } else {
            votingSession.setResult(VotingResult.draw);
            log.info("voting.session.result.is.draw");
        }
    }

}
