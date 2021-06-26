package com.vargas.leo.gerenciadorassembleia.service;

import com.vargas.leo.gerenciadorassembleia.controller.request.VoteRequest;
import com.vargas.leo.gerenciadorassembleia.domain.User;
import com.vargas.leo.gerenciadorassembleia.domain.Vote;
import com.vargas.leo.gerenciadorassembleia.domain.VotingPowerDTO;
import com.vargas.leo.gerenciadorassembleia.domain.VotingSession;
import com.vargas.leo.gerenciadorassembleia.domain.enums.VotingOption;
import com.vargas.leo.gerenciadorassembleia.domain.enums.VotingPower;
import com.vargas.leo.gerenciadorassembleia.domain.enums.VotingSessionStatus;
import com.vargas.leo.gerenciadorassembleia.exception.BusinessException;
import com.vargas.leo.gerenciadorassembleia.exception.NotFoundException;
import com.vargas.leo.gerenciadorassembleia.repository.UserRepository;
import com.vargas.leo.gerenciadorassembleia.repository.VoteRepository;
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

    @Mock
    private CpfValidationService cpfValidationService;

    private final Integer mockUserId = 1;
    private final Integer mockVotingSessionId = 1;
    private final String mockVotingOptionYes = VotingOption.yes.name();
    private final String mockVotingOptionNo = VotingOption.no.name();
    private VoteRequest request;

    @Before
    public void setUp() {
        request = new VoteRequest();
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowExceptionWhenUserNotFound() {
        request.setUserId(mockUserId);

        when(userRepository.findById(mockUserId)).thenReturn(Optional.empty());

        try {
            registerVoteService.register(request);
        } catch (NotFoundException e) {
            assertEquals("user.not.found", e.getMessage());

            verify(userRepository).findById(mockUserId);
            verify(voteRepository, never()).save(any(Vote.class));

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
            registerVoteService.register(request);
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
        request.setUserId(mockUserId);
        request.setVotingSessionId(mockVotingSessionId);
        request.setVote(mockVotingOptionYes);

        User user = User.builder().build();

        VotingSession votingSession = VotingSession.builder().build();

        Vote vote = Vote.builder()
                .user(user)
                .votingSession(votingSession)
                .build();

        when(userRepository.findById(mockUserId))
                .thenReturn(Optional.of(user));
        when(votingSessionRepository.findById(mockVotingSessionId))
                .thenReturn(Optional.of(votingSession));
        when(voteRepository.findByUserIdAndVotingSessionId(mockUserId, mockVotingSessionId))
                .thenReturn(Optional.of(vote));
        doThrow(BusinessException.class).when(votingSessionValidator).validateVotingSessionStatus(votingSession);

        try {
            registerVoteService.register(request);
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
        request.setUserId(mockUserId);
        request.setVotingSessionId(mockVotingSessionId);
        request.setVote(mockVotingOptionYes);

        User user = User.builder().build();

        VotingSession votingSession = VotingSession.builder()
                .status(VotingSessionStatus.close)
                .build();

        Vote vote = Vote.builder()
                .user(user)
                .votingSession(votingSession)
                .build();

        when(userRepository.findById(mockUserId))
                .thenReturn(Optional.of(user));
        when(votingSessionRepository.findById(mockVotingSessionId))
                .thenReturn(Optional.of(votingSession));
        when(voteRepository.findByUserIdAndVotingSessionId(mockUserId, mockVotingSessionId))
                .thenReturn(Optional.of(vote));
        doThrow(BusinessException.class).when(votingSessionValidator).validateVotingSessionStatus(votingSession);

        try {
            registerVoteService.register(request);
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
        request.setUserId(mockUserId);
        request.setVotingSessionId(mockVotingSessionId);

        User user = User.builder().build();

        VotingSession votingSession = VotingSession.builder()
                .status(VotingSessionStatus.open)
                .build();

        Vote vote = Vote.builder().build();

        when(userRepository.findById(mockUserId))
                .thenReturn(Optional.of(user));
        when(votingSessionRepository.findById(mockVotingSessionId))
                .thenReturn(Optional.of(votingSession));
        when(voteRepository.findByUserIdAndVotingSessionId(mockUserId, mockVotingSessionId))
                .thenReturn(Optional.of(vote));

        try {
            registerVoteService.register(request);
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

        request.setUserId(mockUserId);
        request.setVotingSessionId(mockVotingSessionId);
        request.setVote(invalidVotingOption);

        User user = User.builder().build();

        VotingSession votingSession = VotingSession.builder()
                .status(VotingSessionStatus.open)
                .build();

        Vote vote = Vote.builder().build();

        when(userRepository.findById(mockUserId))
                .thenReturn(Optional.of(user));
        when(votingSessionRepository.findById(mockVotingSessionId))
                .thenReturn(Optional.of(votingSession));
        when(voteRepository.findByUserIdAndVotingSessionId(mockUserId, mockVotingSessionId))
                .thenReturn(Optional.of(vote));

        try {
            registerVoteService.register(request);
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
        request.setUserId(mockUserId);
        request.setVotingSessionId(mockVotingSessionId);

        User user = User.builder().build();

        VotingSession votingSession = VotingSession.builder()
                .status(VotingSessionStatus.open)
                .build();

        Vote vote = Vote.builder()
                .vote(VotingOption.yes)
                .build();

        when(userRepository.findById(mockUserId))
                .thenReturn(Optional.of(user));
        when(votingSessionRepository.findById(mockVotingSessionId))
                .thenReturn(Optional.of(votingSession));
        when(voteRepository.findByUserIdAndVotingSessionId(mockUserId, mockVotingSessionId))
                .thenReturn(Optional.of(vote));

        try {
            registerVoteService.register(request);
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
        request.setUserId(mockUserId);
        request.setVotingSessionId(mockVotingSessionId);
        request.setVote(mockVotingOptionYes);

        User user = User.builder().build();

        VotingSession votingSession = VotingSession.builder()
                .status(VotingSessionStatus.open)
                .build();

        VotingPowerDTO votingPowerDTO = VotingPowerDTO.builder().status(VotingPower.ABLE_TO_VOTE).build();

        when(userRepository.findById(mockUserId))
                .thenReturn(Optional.of(user));
        when(votingSessionRepository.findById(mockVotingSessionId))
                .thenReturn(Optional.of(votingSession));
        when(voteRepository.findByUserIdAndVotingSessionId(mockUserId, mockVotingSessionId))
                .thenReturn(Optional.empty());
        when(cpfValidationService.isAllowedToVote(any())).thenReturn(votingPowerDTO);

        Vote result = registerVoteService.register(request);

        assertEquals(VotingOption.yes ,result.getVote());
        assertEquals(user, result.getUser());
        assertEquals(votingSession, result.getVotingSession());
        assertEquals(1, result.getVotingSession().getYesVotes());

        verify(userRepository).findById(mockUserId);
        verify(votingSessionRepository).findById(mockVotingSessionId);
        verify(voteRepository).findByUserIdAndVotingSessionId(mockUserId, mockVotingSessionId);
        verify(cpfValidationService).isAllowedToVote(any());
        verify(voteRepository).save(any(Vote.class));
    }

    @Test
    public void shouldRegisterVoteWhenValidRequestAndVoteNoAndValidVotingSessionAndValidUser() {
        request.setUserId(mockUserId);
        request.setVotingSessionId(mockVotingSessionId);
        request.setVote(mockVotingOptionNo);


        User user = User.builder().build();

        VotingSession votingSession = VotingSession.builder()
                .status(VotingSessionStatus.open)
                .build();

        votingSession.setStatus(VotingSessionStatus.open);

        VotingPowerDTO votingPowerDTO = VotingPowerDTO.builder().status(VotingPower.ABLE_TO_VOTE).build();

        when(userRepository.findById(mockUserId))
                .thenReturn(Optional.of(user));
        when(votingSessionRepository.findById(mockVotingSessionId))
                .thenReturn(Optional.of(votingSession));
        when(voteRepository.findByUserIdAndVotingSessionId(mockUserId, mockVotingSessionId))
                .thenReturn(Optional.empty());
        when(cpfValidationService.isAllowedToVote(any())).thenReturn(votingPowerDTO);

        Vote result = registerVoteService.register(request);

        assertEquals(VotingOption.no ,result.getVote());
        assertEquals(user, result.getUser());
        assertEquals(votingSession, result.getVotingSession());
        assertEquals(1, result.getVotingSession().getNoVotes());

        verify(userRepository).findById(mockUserId);
        verify(votingSessionRepository).findById(mockVotingSessionId);
        verify(voteRepository).findByUserIdAndVotingSessionId(mockUserId, mockVotingSessionId);
        verify(cpfValidationService).isAllowedToVote(any());
        verify(voteRepository).save(any(Vote.class));
    }

    @Test(expected = BusinessException.class)
    public void shouldThrowExceptionWhenAllValidButCpfNotAllowedToVote() {
        request.setUserId(mockUserId);
        request.setVotingSessionId(mockVotingSessionId);
        request.setVote(mockVotingOptionNo);


        User user = User.builder().build();

        VotingSession votingSession = VotingSession.builder()
                .status(VotingSessionStatus.open)
                .build();

        votingSession.setStatus(VotingSessionStatus.open);

        VotingPowerDTO votingPowerDTO = VotingPowerDTO.builder().build();

        when(userRepository.findById(mockUserId))
                .thenReturn(Optional.of(user));
        when(votingSessionRepository.findById(mockVotingSessionId))
                .thenReturn(Optional.of(votingSession));
        when(voteRepository.findByUserIdAndVotingSessionId(mockUserId, mockVotingSessionId))
                .thenReturn(Optional.empty());
        when(cpfValidationService.isAllowedToVote(any())).thenReturn(votingPowerDTO);

        try {
            registerVoteService.register(request);
        } catch (BusinessException e) {
            assertEquals("cpf.is.unable.to.vote", e.getMessage());

            verify(userRepository).findById(mockUserId);
            verify(votingSessionRepository).findById(mockVotingSessionId);
            verify(voteRepository).findByUserIdAndVotingSessionId(mockUserId, mockVotingSessionId);
            verify(cpfValidationService).isAllowedToVote(any());
            verify(voteRepository, never()).save(any(Vote.class));

            throw e;
        }



    }

}
