package com.vargas.leo.gerenciadorassembleia.service;

import com.vargas.leo.gerenciadorassembleia.controller.request.CreateVotingSessionRequest;
import com.vargas.leo.gerenciadorassembleia.domain.Agenda;
import com.vargas.leo.gerenciadorassembleia.domain.enums.AgendaStatus;
import com.vargas.leo.gerenciadorassembleia.domain.VotingSession;
import com.vargas.leo.gerenciadorassembleia.exception.BusinessException;
import com.vargas.leo.gerenciadorassembleia.exception.NotFoundException;
import com.vargas.leo.gerenciadorassembleia.repository.AgendaRepository;
import com.vargas.leo.gerenciadorassembleia.repository.VotingSessionRepository;
import com.vargas.leo.gerenciadorassembleia.validator.VotingSessionValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CreatingVotingSessionServiceTest {

    @InjectMocks
    private CreateVotingSessionService createVotingSessionService;

    @Mock
    private VotingSessionRepository votingSessionRepository;

    @Mock
    private AgendaRepository agendaRepository;

    @Mock
    private VotingSessionValidator votingSessionValidator;

    private final String agendaId = "mockAgendaId";

    @Test(expected = NotFoundException.class)
    public void shouldThrowExceptionWhenInvalidAgendaId() {
        CreateVotingSessionRequest request = new CreateVotingSessionRequest(null, null);

        when(agendaRepository.findById(null)).thenReturn(Optional.empty());

        try {
            createVotingSessionService.create(request);
        } catch (NotFoundException e) {
            assertEquals("agenda.not.found", e.getMessage());

            throw e;
        }
    }

    @Test(expected = BusinessException.class)
    public void shouldThrowExceptionWhenAgendaAlreadyHasVotingSession() {
        CreateVotingSessionRequest request = new CreateVotingSessionRequest(agendaId, null);
        Agenda agenda = new Agenda();

        when(agendaRepository.findById(agendaId)).thenReturn(Optional.of(agenda));

        try {
            createVotingSessionService.create(request);
        } catch (BusinessException e) {
            assertEquals("agenda.already.has.voting.session", e.getMessage());

            verify(agendaRepository).findById(agendaId);
            verify(agendaRepository, never()).save(agenda);

            throw e;
        }
    }

    @Test
    public void shouldSetTimeLimitCorrectlyWhenExistsAndIsValid() {
        LocalDateTime validTimeLimit = LocalDateTime.now();

        CreateVotingSessionRequest request = new CreateVotingSessionRequest(agendaId, validTimeLimit);
        Agenda agenda = new Agenda();
        agenda.setStatus(AgendaStatus.created);

        when(agendaRepository.findById(agendaId)).thenReturn(Optional.of(agenda));
        when(votingSessionValidator.validateFinalDateTime(validTimeLimit)).thenReturn(true);

        VotingSession result = createVotingSessionService.create(request);

        assertEquals(AgendaStatus.open, result.getAgenda().getStatus());
        assertEquals(validTimeLimit, result.getFinalDateTime());

        verify(agendaRepository).findById(agendaId);
        verify(agendaRepository).save(agenda);
        verify(votingSessionValidator).validateFinalDateTime(validTimeLimit);
        verify(votingSessionRepository).save(result);
    }

    @Test
    public void shouldntSetTimeLimitWhenDoesntExists() {
        CreateVotingSessionRequest request = new CreateVotingSessionRequest(agendaId, null);
        Agenda agenda = new Agenda();
        agenda.setStatus(AgendaStatus.created);

        when(agendaRepository.findById(agendaId)).thenReturn(Optional.of(agenda));
        when(votingSessionValidator.validateFinalDateTime(null)).thenReturn(false);

        VotingSession result = createVotingSessionService.create(request);

        assertEquals(AgendaStatus.open, result.getAgenda().getStatus());
        assertEquals(VotingSession.DEFAULT_FINAL_DATE_TIME, result.getFinalDateTime());

        verify(agendaRepository).findById(agendaId);
        verify(agendaRepository).save(agenda);
        verify(votingSessionValidator).validateFinalDateTime(null);
        verify(votingSessionRepository).save(result);
    }

    @Test
    public void shouldntSetTimeLimitWhenExistsAndIsInvalid() {
        LocalDateTime invalidFinalDateTime = LocalDateTime.of(LocalDate.of(1999,1,1), LocalTime.now());

        CreateVotingSessionRequest request = new CreateVotingSessionRequest(agendaId, invalidFinalDateTime);
        Agenda agenda = new Agenda();
        agenda.setStatus(AgendaStatus.created);

        when(agendaRepository.findById(agendaId)).thenReturn(Optional.of(agenda));
        when(votingSessionValidator.validateFinalDateTime(invalidFinalDateTime)).thenReturn(false);

        VotingSession result = createVotingSessionService.create(request);

        assertEquals(AgendaStatus.open, result.getAgenda().getStatus());
        assertEquals(VotingSession.DEFAULT_FINAL_DATE_TIME, result.getFinalDateTime());

        verify(agendaRepository).findById(agendaId);
        verify(agendaRepository).save(agenda);
        verify(votingSessionValidator).validateFinalDateTime(invalidFinalDateTime);
        verify(votingSessionRepository).save(result);
    }

}
