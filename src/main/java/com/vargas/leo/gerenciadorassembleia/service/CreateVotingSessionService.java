package com.vargas.leo.gerenciadorassembleia.service;

import com.vargas.leo.gerenciadorassembleia.controller.request.CreateVotingSessionRequest;
import com.vargas.leo.gerenciadorassembleia.controller.response.CreateVotingSessionResponse;
import com.vargas.leo.gerenciadorassembleia.domain.Agenda;
import com.vargas.leo.gerenciadorassembleia.domain.AgendaStatus;
import com.vargas.leo.gerenciadorassembleia.domain.VotingSession;
import com.vargas.leo.gerenciadorassembleia.domain.VotingSessionStatus;
import com.vargas.leo.gerenciadorassembleia.exception.BusinessException;
import com.vargas.leo.gerenciadorassembleia.exception.NotFoundException;
import com.vargas.leo.gerenciadorassembleia.repository.AgendaRepository;
import com.vargas.leo.gerenciadorassembleia.repository.VotingSessionRepository;
import com.vargas.leo.gerenciadorassembleia.validator.VotingSessionValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateVotingSessionService {

    private final VotingSessionRepository votingSessionRepository;
    private final AgendaRepository agendaRepository;
    private final VotingSessionValidator votingSessionValidator;
    private final ModelMapper mapper;

    public CreateVotingSessionResponse createVotingSession(CreateVotingSessionRequest votingSessionRequest) {
        return mapper.map(this.create(votingSessionRequest), CreateVotingSessionResponse.class);
    }

    protected VotingSession create(CreateVotingSessionRequest votingSessionRequest) {
        Agenda agenda = agendaRepository.findById(votingSessionRequest.getAgendaId());
        if (agenda == null) {
            throw new NotFoundException("agenda.not.found");
        }

        this.validateAgendaStatus(agenda);
        agendaRepository.save(agenda);

        VotingSession votingSession = new VotingSession(agenda);
        this.adjustVotingSessionTimeLimit(votingSession, votingSessionRequest.getFinalDateTime());
        votingSession.setStatus(VotingSessionStatus.opened);
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

    private void adjustVotingSessionTimeLimit(VotingSession votingSession, LocalDateTime finalTimeLimit) {
        boolean validRequestTimeLimit = votingSessionValidator.validateFinalDateTime(finalTimeLimit);
        if (validRequestTimeLimit) {
            votingSession.setFinalDateTime(finalTimeLimit);
        } else {
            log.info("request.invalid.time.limit.for.voting.session");
        }
    }

}
