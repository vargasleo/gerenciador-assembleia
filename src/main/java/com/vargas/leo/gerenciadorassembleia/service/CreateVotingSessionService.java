package com.vargas.leo.gerenciadorassembleia.service;

import com.vargas.leo.gerenciadorassembleia.controller.request.CreateVotingSessionRequest;
import com.vargas.leo.gerenciadorassembleia.controller.response.CreateVotingSessionResponse;
import com.vargas.leo.gerenciadorassembleia.domain.Agenda;
import com.vargas.leo.gerenciadorassembleia.domain.VotingSession;
import com.vargas.leo.gerenciadorassembleia.domain.enums.AgendaStatus;
import com.vargas.leo.gerenciadorassembleia.domain.enums.VotingSessionStatus;
import com.vargas.leo.gerenciadorassembleia.exception.BusinessException;
import com.vargas.leo.gerenciadorassembleia.exception.NotFoundException;
import com.vargas.leo.gerenciadorassembleia.repository.AgendaRepository;
import com.vargas.leo.gerenciadorassembleia.repository.VotingSessionRepository;
import com.vargas.leo.gerenciadorassembleia.validator.VotingSessionValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CreateVotingSessionService {

    private final VotingSessionRepository votingSessionRepository;
    private final AgendaRepository agendaRepository;
    private final VotingSessionValidator votingSessionValidator;

    public CreateVotingSessionResponse createVotingSession(CreateVotingSessionRequest votingSessionRequest) {
        VotingSession votingSession = this.create(votingSessionRequest);

        return CreateVotingSessionResponse.builder()
                .id(votingSession.getId())
                .createdAt(votingSession.getCreatedAt())
                .deadline(votingSession.getDeadline())
                .build();
    }

    protected VotingSession create(CreateVotingSessionRequest votingSessionRequest) {
        Agenda agenda = agendaRepository.findById(votingSessionRequest.getAgendaId())
                .orElseThrow(() -> new NotFoundException("agenda.not.found"));

        this.validateAgendaStatus(agenda);

        agendaRepository.save(agenda);

        VotingSession votingSession = VotingSession.builder()
                .agenda(agenda)
                .build();

        this.adjustVotingSessionTimeLimit(votingSession, votingSessionRequest.getFinalDateTime());

        votingSession.setStatus(VotingSessionStatus.open);

        votingSessionRepository.save(votingSession);

        return votingSession;
    }

    private void validateAgendaStatus(Agenda agenda) {
        if (AgendaStatus.created.equals(agenda.getStatus())) {
            agenda.setStatus(AgendaStatus.open);
        } else {
            throw new BusinessException("agenda.already.has.voting.session");
        }
    }

    private void adjustVotingSessionTimeLimit(VotingSession votingSession, LocalDateTime deadline) {
        boolean validRequestTimeLimit = votingSessionValidator.isValidRequestDeadline(deadline);

        if (validRequestTimeLimit) {
            votingSession.setDeadline(deadline);
        } else {
            votingSession.setDeadline(VotingSession.DEFAULT_FINAL_DATE_TIME);
        }
    }

}
