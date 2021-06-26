package com.vargas.leo.gerenciadorassembleia.service;

import com.vargas.leo.gerenciadorassembleia.controller.request.FinishVotingRequest;
import com.vargas.leo.gerenciadorassembleia.domain.*;
import com.vargas.leo.gerenciadorassembleia.domain.enums.AgendaStatus;
import com.vargas.leo.gerenciadorassembleia.domain.enums.VotingResult;
import com.vargas.leo.gerenciadorassembleia.domain.enums.VotingSessionStatus;
import com.vargas.leo.gerenciadorassembleia.exception.BusinessException;
import com.vargas.leo.gerenciadorassembleia.exception.NotFoundException;
import com.vargas.leo.gerenciadorassembleia.repository.UserRepository;
import com.vargas.leo.gerenciadorassembleia.repository.VotingSessionRepository;
import com.vargas.leo.gerenciadorassembleia.validator.VotingSessionValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FinishVotingServiceTest {

    @InjectMocks
    private FinishVotingService finishVotingService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private VotingSessionRepository votingSessionRepository;

    @Mock
    private VotingSessionValidator votingSessionValidator;

    private final Integer mockUserId = 1;
    private final Integer mockVotingSessionId = 1;
    private FinishVotingRequest request;

    @Before
    public void setUp() {
        request = new FinishVotingRequest();
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowExceptionWhenUserNotFound() {
        request.setUserId(mockUserId);

        when(userRepository.findById(mockUserId)).thenReturn(Optional.empty());

        try {
            finishVotingService.finishVotingAndCountVotes(request);
        } catch (NotFoundException e) {
            assertEquals("user.not.found", e.getMessage());

            verify(userRepository).findById(mockUserId);
            verify(votingSessionRepository, never()).save(any(VotingSession.class));

            throw e;
        }
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowExceptionWhenVotingSessionNotFound() {
        request.setUserId(mockUserId);
        request.setVotingSessionId(mockVotingSessionId);

        User user = User.builder().build();

        when(userRepository.findById(mockUserId)).thenReturn(Optional.of(user));
        when(votingSessionRepository.findById(mockVotingSessionId)).thenReturn(Optional.empty());

        try {
            finishVotingService.finishVotingAndCountVotes(request);
        } catch (NotFoundException e) {
            assertEquals("voting.session.not.found", e.getMessage());

            verify(userRepository).findById(mockUserId);
            verify(votingSessionRepository).findById(mockVotingSessionId);
            verify(votingSessionRepository, never()).save(any(VotingSession.class));

            throw e;
        }
    }

    @Test(expected = BusinessException.class)
    public void shouldThrowExceptionWhenVotingSessionStatusIsInvalid() {
        request.setUserId(mockUserId);
        request.setVotingSessionId(mockVotingSessionId);

        User user = User.builder().build();

        Agenda agenda = Agenda.builder().build();

        VotingSession votingSession = VotingSession.builder()
                .agenda(agenda)
                .build();

        when(userRepository.findById(mockUserId)).thenReturn(Optional.of(user));
        when(votingSessionRepository.findById(mockVotingSessionId)).thenReturn(Optional.of(votingSession));
        doThrow(BusinessException.class).when(votingSessionValidator).validateVotingSessionStatus(votingSession);

        try {
            finishVotingService.finishVotingAndCountVotes(request);
        } catch (BusinessException e) {

            verify(userRepository).findById(mockUserId);
            verify(votingSessionRepository).findById(mockVotingSessionId);
            verify(votingSessionValidator).validateVotingSessionStatus(votingSession);
            verify(votingSessionRepository, never()).save(any(VotingSession.class));

            throw e;
        }
    }

    @Test
    public void shouldDefineYesAsWinner() {
        request.setUserId(mockUserId);
        request.setVotingSessionId(mockVotingSessionId);

        User user = User.builder().build();

        Agenda agenda = Agenda.builder().build();

        VotingSession votingSession = VotingSession.builder()
                .agenda(agenda)
                .yesVotes(1)
                .build();

        when(userRepository.findById(mockUserId)).thenReturn(Optional.of(user));
        when(votingSessionRepository.findById(mockVotingSessionId)).thenReturn(Optional.of(votingSession));

        VotingSession result = finishVotingService.finishVotingAndCountVotes(request);

        assertEquals(AgendaStatus.closed, result.getAgenda().getStatus());
        assertEquals(VotingSessionStatus.close, result.getStatus());
        assertEquals(VotingResult.yes, result.getResult());

        verify(userRepository).findById(mockUserId);
        verify(votingSessionRepository).findById(mockVotingSessionId);
        verify(votingSessionValidator).validateVotingSessionStatus(votingSession);
        verify(votingSessionRepository).save(votingSession);

    }

    @Test
    public void shouldDefineNoAsWinner() {
        request.setUserId(mockUserId);
        request.setVotingSessionId(mockVotingSessionId);

        User user = User.builder().build();

        Agenda agenda = Agenda.builder().build();

        VotingSession votingSession = VotingSession.builder()
                .agenda(agenda)
                .noVotes(1)
                .build();

        when(userRepository.findById(mockUserId)).thenReturn(Optional.of(user));
        when(votingSessionRepository.findById(mockVotingSessionId)).thenReturn(Optional.of(votingSession));

        VotingSession result = finishVotingService.finishVotingAndCountVotes(request);

        assertEquals(AgendaStatus.closed, result.getAgenda().getStatus());
        assertEquals(VotingSessionStatus.close, result.getStatus());
        assertEquals(VotingResult.no, result.getResult());

        verify(userRepository).findById(mockUserId);
        verify(votingSessionRepository).findById(mockVotingSessionId);
        verify(votingSessionValidator).validateVotingSessionStatus(votingSession);
        verify(votingSessionRepository).save(votingSession);

    }

    @Test
    public void shouldDefineResultAsDraw() {
        request.setUserId(mockUserId);
        request.setVotingSessionId(mockVotingSessionId);

        User user = User.builder().build();

        Agenda agenda = Agenda.builder().build();

        VotingSession votingSession = VotingSession.builder()
                .agenda(agenda)
                .build();

        when(userRepository.findById(mockUserId)).thenReturn(Optional.of(user));
        when(votingSessionRepository.findById(mockVotingSessionId)).thenReturn(Optional.of(votingSession));

        VotingSession result = finishVotingService.finishVotingAndCountVotes(request);

        assertEquals(AgendaStatus.closed, result.getAgenda().getStatus());
        assertEquals(VotingSessionStatus.close, result.getStatus());
        assertEquals(VotingResult.draw, result.getResult());

        verify(userRepository).findById(mockUserId);
        verify(votingSessionRepository).findById(mockVotingSessionId);
        verify(votingSessionValidator).validateVotingSessionStatus(votingSession);
        verify(votingSessionRepository).save(votingSession);

    }

}
