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

    private final String mockUserId = "mockUserId";
    private final String mockVotingSessionId = "mockVotingSessionId";

    @Test(expected = NotFoundException.class)
    public void shouldThrowExceptionWhenUserNotFound() {
        FinishVotingRequest finishVotingRequest = new FinishVotingRequest(mockUserId, null);

        when(userRepository.findById(mockUserId)).thenReturn(Optional.empty());

        try {
            finishVotingService.finishVotingAndCountVotes(finishVotingRequest);
        } catch (NotFoundException e) {
            assertEquals("user.not.found", e.getMessage());

            verify(userRepository).findById(mockUserId);
            verify(votingSessionRepository, never()).save(any(VotingSession.class));

            throw e;
        }
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowExceptionWhenVotingSessionNotFound() {
        FinishVotingRequest finishVotingRequest = new FinishVotingRequest(mockUserId, mockVotingSessionId);

        User user = new User("mockUserName");

        when(userRepository.findById(mockUserId)).thenReturn(Optional.of(user));
        when(votingSessionRepository.findById(mockVotingSessionId)).thenReturn(Optional.empty());

        try {
            finishVotingService.finishVotingAndCountVotes(finishVotingRequest);
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
        FinishVotingRequest finishVotingRequest = new FinishVotingRequest(mockUserId, mockVotingSessionId);

        User user = new User("mockUserName");
        Agenda agenda = new Agenda();

        VotingSession votingSession = new VotingSession();
        votingSession.setAgenda(agenda);
        when(userRepository.findById(mockUserId)).thenReturn(Optional.of(user));
        when(votingSessionRepository.findById(mockVotingSessionId)).thenReturn(Optional.of(votingSession));
        doThrow(BusinessException.class).when(votingSessionValidator).validateVotingSessionStatus(votingSession);

        try {
            finishVotingService.finishVotingAndCountVotes(finishVotingRequest);
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
        FinishVotingRequest finishVotingRequest = new FinishVotingRequest(mockUserId, mockVotingSessionId);

        User user = new User("mockUserName");
        Agenda agenda = new Agenda();

        VotingSession votingSession = new VotingSession();
        votingSession.setAgenda(agenda);
        votingSession.setYesVotes(1);

        when(userRepository.findById(mockUserId)).thenReturn(Optional.of(user));
        when(votingSessionRepository.findById(mockVotingSessionId)).thenReturn(Optional.of(votingSession));

        VotingSession result = finishVotingService.finishVotingAndCountVotes(finishVotingRequest);

        assertEquals(AgendaStatus.closed, result.getAgenda().getStatus());
        assertEquals(VotingSessionStatus.closed, result.getStatus());
        assertEquals(VotingResult.yes, result.getWinnerOption());
        assertEquals(VotingResult.no, result.getLooserOption());

        verify(userRepository).findById(mockUserId);
        verify(votingSessionRepository).findById(mockVotingSessionId);
        verify(votingSessionValidator).validateVotingSessionStatus(votingSession);
        verify(votingSessionRepository).save(votingSession);

    }

    @Test
    public void shouldDefineNoAsWinner() {
        FinishVotingRequest finishVotingRequest = new FinishVotingRequest(mockUserId, mockVotingSessionId);

        User user = new User("mockUserName");
        Agenda agenda = new Agenda();

        VotingSession votingSession = new VotingSession();
        votingSession.setAgenda(agenda);
        votingSession.setNoVotes(1);

        when(userRepository.findById(mockUserId)).thenReturn(Optional.of(user));
        when(votingSessionRepository.findById(mockVotingSessionId)).thenReturn(Optional.of(votingSession));

        VotingSession result = finishVotingService.finishVotingAndCountVotes(finishVotingRequest);

        assertEquals(AgendaStatus.closed, result.getAgenda().getStatus());
        assertEquals(VotingSessionStatus.closed, result.getStatus());
        assertEquals(VotingResult.no, result.getWinnerOption());
        assertEquals(VotingResult.yes, result.getLooserOption());

        verify(userRepository).findById(mockUserId);
        verify(votingSessionRepository).findById(mockVotingSessionId);
        verify(votingSessionValidator).validateVotingSessionStatus(votingSession);
        verify(votingSessionRepository).save(votingSession);

    }

    @Test
    public void shouldDefineResultAsDraw() {
        FinishVotingRequest finishVotingRequest = new FinishVotingRequest(mockUserId, mockVotingSessionId);

        User user = new User("mockUserName");
        Agenda agenda = new Agenda();

        VotingSession votingSession = new VotingSession();
        votingSession.setAgenda(agenda);

        when(userRepository.findById(mockUserId)).thenReturn(Optional.of(user));
        when(votingSessionRepository.findById(mockVotingSessionId)).thenReturn(Optional.of(votingSession));

        VotingSession result = finishVotingService.finishVotingAndCountVotes(finishVotingRequest);

        assertEquals(AgendaStatus.closed, result.getAgenda().getStatus());
        assertEquals(VotingSessionStatus.closed, result.getStatus());
        assertEquals(VotingResult.draw, result.getWinnerOption());
        assertEquals(VotingResult.draw, result.getLooserOption());

        verify(userRepository).findById(mockUserId);
        verify(votingSessionRepository).findById(mockVotingSessionId);
        verify(votingSessionValidator).validateVotingSessionStatus(votingSession);
        verify(votingSessionRepository).save(votingSession);

    }

}
