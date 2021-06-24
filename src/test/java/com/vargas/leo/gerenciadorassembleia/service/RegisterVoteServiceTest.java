package com.vargas.leo.gerenciadorassembleia.service;

import com.vargas.leo.gerenciadorassembleia.controller.request.VoteRequest;
import com.vargas.leo.gerenciadorassembleia.domain.*;
import com.vargas.leo.gerenciadorassembleia.domain.enums.VotingOption;
import com.vargas.leo.gerenciadorassembleia.domain.enums.VotingSessionStatus;
import com.vargas.leo.gerenciadorassembleia.exception.BusinessException;
import com.vargas.leo.gerenciadorassembleia.exception.NotFoundException;
import com.vargas.leo.gerenciadorassembleia.repository.UserRepository;
import com.vargas.leo.gerenciadorassembleia.repository.VoteRepository;
import com.vargas.leo.gerenciadorassembleia.repository.VotingSessionRepository;
import com.vargas.leo.gerenciadorassembleia.validator.VotingSessionValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RegisterVoteServiceTest {

    @InjectMocks
    private RegisterVoteService registerVoteService;

    @Mock
    private VoteRepository voteRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private VotingSessionRepository votingSessionRepository;

    @Mock
    private VotingSessionValidator votingSessionValidator;

    private final String mockUserId = "mockUserId";
    private final String mockVotingSessionId = "mockVotingSessionId";
    private final String validVotingOptionYes = "yes";

    @Test(expected = NotFoundException.class)
    public void shouldThrowExceptionWhenUserNotFound() {
        VoteRequest voteRequest = new VoteRequest(mockUserId, null, null);

        when(userRepository.findById(mockUserId)).thenReturn(Optional.empty());

        try {
            registerVoteService.register(voteRequest);
        } catch (NotFoundException e) {
            assertEquals("user.not.found", e.getMessage());

            verify(userRepository).findById(mockUserId);
            verify(voteRepository, never()).save(any(Vote.class));

            throw e;
        }
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowExceptionWhenVotingSessionNotFound() {
        User user = new User("mockUserName");

        VoteRequest voteRequest = new VoteRequest(mockUserId, mockVotingSessionId, null);

        when(userRepository.findById(mockUserId)).thenReturn(Optional.of(user));
        when(votingSessionRepository.findById(mockVotingSessionId)).thenReturn(Optional.empty());

        try {
            registerVoteService.register(voteRequest);
        } catch (NotFoundException e) {
            assertEquals("voting.session.not.found", e.getMessage());

            verify(userRepository).findById(mockUserId);
            verify(votingSessionRepository).findById(mockVotingSessionId);
            verify(voteRepository, never()).save(any(Vote.class));

            throw e;
        }
    }

    @Test(expected = BusinessException.class)
    public void shouldThrowExceptionWhenVotingSessionStatusIsNull() {
        VoteRequest voteRequest = new VoteRequest(mockUserId, mockVotingSessionId, validVotingOptionYes);

        User user = new User("mockUserName");

        VotingSession votingSession = new VotingSession(null);

        Vote vote = new Vote(user, votingSession);


        when(userRepository.findById(mockUserId))
                .thenReturn(Optional.of(user));
        when(votingSessionRepository.findById(mockVotingSessionId))
                .thenReturn(Optional.of(votingSession));
        when(voteRepository.findByUserIdAndVotingSessionId(mockUserId, mockVotingSessionId))
                .thenReturn(Optional.of(vote));
        doThrow(BusinessException.class).when(votingSessionValidator).validateVotingSessionStatus(votingSession);

        try {
            registerVoteService.register(voteRequest);
        } catch (BusinessException e) {
            verify(userRepository).findById(mockUserId);
            verify(votingSessionRepository).findById(mockVotingSessionId);
            verify(votingSessionValidator).validateVotingSessionStatus(votingSession);
            verify(voteRepository).findByUserIdAndVotingSessionId(mockUserId, mockVotingSessionId);
            verify(voteRepository, never()).save(any(Vote.class));

            throw e;
        }
    }

    @Test(expected = BusinessException.class)
    public void shouldThrowExceptionWhenVotingSessionStatusIsntOpen() {
        VoteRequest voteRequest = new VoteRequest(mockUserId, mockVotingSessionId, validVotingOptionYes);

        User user = new User("mockUserName");

        VotingSession votingSession = new VotingSession(null);
        votingSession.setStatus(VotingSessionStatus.closed);

        Vote vote = new Vote(user, votingSession);

        when(userRepository.findById(mockUserId))
                .thenReturn(Optional.of(user));
        when(votingSessionRepository.findById(mockVotingSessionId))
                .thenReturn(Optional.of(votingSession));
        when(voteRepository.findByUserIdAndVotingSessionId(mockUserId, mockVotingSessionId))
                .thenReturn(Optional.of(vote));
        doThrow(BusinessException.class).when(votingSessionValidator).validateVotingSessionStatus(votingSession);

        try {
            registerVoteService.register(voteRequest);
        } catch (BusinessException e) {
            verify(userRepository).findById(mockUserId);
            verify(votingSessionRepository).findById(mockVotingSessionId);
            verify(votingSessionValidator).validateVotingSessionStatus(votingSession);
            verify(voteRepository).findByUserIdAndVotingSessionId(mockUserId, mockVotingSessionId);
            verify(voteRepository, never()).save(any(Vote.class));

            throw e;
        }
    }

    @Test(expected = BusinessException.class)
    public void shouldThrowExceptionWhenVotingOptionIsNull() {
        VoteRequest voteRequest = new VoteRequest(mockUserId, mockVotingSessionId, null);

        User user = new User("mockUserName");

        VotingSession votingSession = new VotingSession(null);
        votingSession.setStatus(VotingSessionStatus.opened);

        Vote vote = new Vote(user, votingSession);

        when(userRepository.findById(mockUserId))
                .thenReturn(Optional.of(user));
        when(votingSessionRepository.findById(mockVotingSessionId))
                .thenReturn(Optional.of(votingSession));
        when(voteRepository.findByUserIdAndVotingSessionId(mockUserId, mockVotingSessionId))
                .thenReturn(Optional.of(vote));

        try {
            registerVoteService.register(voteRequest);
        } catch (BusinessException e) {
            assertEquals("invalid.voting.option", e.getMessage());

            verify(userRepository).findById(mockUserId);
            verify(votingSessionRepository).findById(mockVotingSessionId);
            verify(voteRepository).findByUserIdAndVotingSessionId(mockUserId, mockVotingSessionId);
            verify(voteRepository, never()).save(any(Vote.class));

            throw e;
        }
    }

    @Test(expected = BusinessException.class)
    public void shouldThrowExceptionWhenVotingOptionIsntValid() {
        String invalidVotingOption = "mockInvalidVotingOption";
        VoteRequest voteRequest = new VoteRequest(mockUserId, mockVotingSessionId, invalidVotingOption);

        User user = new User("mockUserName");

        VotingSession votingSession = new VotingSession(null);
        votingSession.setStatus(VotingSessionStatus.opened);

        Vote vote = new Vote(user, votingSession);

        when(userRepository.findById(mockUserId))
                .thenReturn(Optional.of(user));
        when(votingSessionRepository.findById(mockVotingSessionId))
                .thenReturn(Optional.of(votingSession));
        when(voteRepository.findByUserIdAndVotingSessionId(mockUserId, mockVotingSessionId))
                .thenReturn(Optional.of(vote));

        try {
            registerVoteService.register(voteRequest);
        } catch (BusinessException e) {
            assertEquals("invalid.voting.option", e.getMessage());

            verify(userRepository).findById(mockUserId);
            verify(votingSessionRepository).findById(mockVotingSessionId);
            verify(voteRepository).findByUserIdAndVotingSessionId(mockUserId, mockVotingSessionId);
            verify(voteRepository, never()).save(any(Vote.class));

            throw e;
        }
    }

    @Test(expected = BusinessException.class)
    public void shouldThrowExceptionWhenUserAlreadyVoted() {
        VoteRequest voteRequest = new VoteRequest(mockUserId, mockVotingSessionId, null);

        User user = new User("mockUserName");

        VotingSession votingSession = new VotingSession(null);
        votingSession.setStatus(VotingSessionStatus.opened);

        Vote vote = new Vote(user, votingSession);
        vote.setVote(VotingOption.yes);

        when(userRepository.findById(mockUserId))
                .thenReturn(Optional.of(user));
        when(votingSessionRepository.findById(mockVotingSessionId))
                .thenReturn(Optional.of(votingSession));
        when(voteRepository.findByUserIdAndVotingSessionId(mockUserId, mockVotingSessionId))
                .thenReturn(Optional.of(vote));

        try {
            registerVoteService.register(voteRequest);
        } catch (BusinessException e) {
            assertEquals("user.already.voted.in.this.session", e.getMessage());

            verify(userRepository).findById(mockUserId);
            verify(votingSessionRepository).findById(mockVotingSessionId);
            verify(voteRepository).findByUserIdAndVotingSessionId(mockUserId, mockVotingSessionId);
            verify(voteRepository, never()).save(any(Vote.class));

            throw e;
        }
    }

    @Test
    public void shouldRegisterVoteWhenValidRequestAndVoteYesAndValidVotingSessionAndValidUser() {
        VoteRequest voteRequest = new VoteRequest(mockUserId, mockVotingSessionId, validVotingOptionYes);

        User user = new User("mockUserName");

        VotingSession votingSession = new VotingSession(null);
        votingSession.setStatus(VotingSessionStatus.opened);

        when(userRepository.findById(mockUserId))
                .thenReturn(Optional.of(user));
        when(votingSessionRepository.findById(mockVotingSessionId))
                .thenReturn(Optional.of(votingSession));
        when(voteRepository.findByUserIdAndVotingSessionId(mockUserId, mockVotingSessionId))
                .thenReturn(Optional.empty());

        Vote result = registerVoteService.register(voteRequest);

        assertEquals(VotingOption.yes ,result.getVote());
        assertEquals(user, result.getUser());
        assertEquals(votingSession, result.getVotingSession());
        assertEquals(1, result.getVotingSession().getYesVotes());

        verify(userRepository).findById(mockUserId);
        verify(votingSessionRepository).findById(mockVotingSessionId);
        verify(voteRepository).findByUserIdAndVotingSessionId(mockUserId, mockVotingSessionId);
        verify(voteRepository).save(any(Vote.class));
    }

    @Test
    public void shouldRegisterVoteWhenValidRequestAndVoteNoAndValidVotingSessionAndValidUser() {
        String validVotingOptionNo = "no";
        VoteRequest voteRequest = new VoteRequest(mockUserId, mockVotingSessionId, validVotingOptionNo);

        User user = new User("mockUserName");

        VotingSession votingSession = new VotingSession(null);
        votingSession.setStatus(VotingSessionStatus.opened);

        when(userRepository.findById(mockUserId))
                .thenReturn(Optional.of(user));
        when(votingSessionRepository.findById(mockVotingSessionId))
                .thenReturn(Optional.of(votingSession));
        when(voteRepository.findByUserIdAndVotingSessionId(mockUserId, mockVotingSessionId))
                .thenReturn(Optional.empty());

        Vote result = registerVoteService.register(voteRequest);

        assertEquals(VotingOption.no ,result.getVote());
        assertEquals(user, result.getUser());
        assertEquals(votingSession, result.getVotingSession());
        assertEquals(1, result.getVotingSession().getNoVotes());

        verify(userRepository).findById(mockUserId);
        verify(votingSessionRepository).findById(mockVotingSessionId);
        verify(voteRepository).findByUserIdAndVotingSessionId(mockUserId, mockVotingSessionId);
        verify(voteRepository).save(any(Vote.class));
    }

}
