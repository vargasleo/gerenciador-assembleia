package com.vargas.leo.gerenciadorassembleia.service;

import com.vargas.leo.gerenciadorassembleia.controller.request.VoteRequest;
import com.vargas.leo.gerenciadorassembleia.controller.response.VoteResponse;
import com.vargas.leo.gerenciadorassembleia.domain.*;
import com.vargas.leo.gerenciadorassembleia.domain.enums.AgendaStatus;
import com.vargas.leo.gerenciadorassembleia.domain.enums.VotingOption;
import com.vargas.leo.gerenciadorassembleia.domain.enums.VotingPower;
import com.vargas.leo.gerenciadorassembleia.domain.enums.VotingSessionStatus;
import com.vargas.leo.gerenciadorassembleia.exception.BusinessException;
import com.vargas.leo.gerenciadorassembleia.exception.NotFoundException;
import com.vargas.leo.gerenciadorassembleia.repository.UserRepository;
import com.vargas.leo.gerenciadorassembleia.repository.VoteRepository;
import com.vargas.leo.gerenciadorassembleia.repository.VotingSessionRepository;
import com.vargas.leo.gerenciadorassembleia.validator.VotingSessionValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RegisterVoteService {

    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final VotingSessionRepository votingSessionRepository;
    private final VotingSessionValidator votingSessionValidator;
    private final CpfValidationService cpfValidationService;

    public VoteResponse registerVote(VoteRequest voteRequest) {
        Vote vote = this.register(voteRequest);

        return VoteResponse.builder()
                .votingOption(vote.getVote())
                .votedAt(vote.getVotedAt())
                .build();
    }

    protected Vote register(VoteRequest voteRequest) {
        User user = userRepository.findById(voteRequest.getUserId())
                .orElseThrow(() -> new NotFoundException("user.not.found"));

        VotingSession votingSession = votingSessionRepository.findById(voteRequest.getVotingSessionId())
                .orElseThrow(() -> new NotFoundException("voting.session.not.found"));

        Vote vote = voteRepository.findByUserIdAndVotingSessionId(voteRequest.getUserId(), voteRequest.getVotingSessionId())
                .orElseGet(() -> this.createNewVote(user, votingSession));

        if (this.isVoteAlreadyRegistered(vote)) {
            throw new BusinessException("user.already.voted.in.this.session");
        }

        votingSessionValidator.validateVotingSessionStatus(vote.getVotingSession());

        if (votingSessionValidator.isNotValidDeadline(votingSession.getDeadline())) {
            votingSession.setStatus(VotingSessionStatus.close);
            votingSession.getAgenda().setStatus(AgendaStatus.closed);
            votingSessionRepository.save(votingSession);
            throw new BusinessException("voting.session.reached.deadline");
        }

        VotingOption votingOption = this.validateVote(voteRequest.getVote());

        if (!this.isUserAbleToVote(user.getCpf())) {
            throw new BusinessException("cpf.is.unable.to.vote");
        }

        this.registerVote(vote, votingOption);

        voteRepository.save(vote);
        return vote;
    }

    private boolean isUserAbleToVote(String cpf) {
        return VotingPower.ABLE_TO_VOTE.equals(cpfValidationService.isAllowedToVote(cpf).getStatus());
    }

    private Vote createNewVote(User user, VotingSession votingSession) {
        return Vote.builder()
                .user(user)
                .votingSession(votingSession)
                .build();
    }

    private void registerVote(Vote vote, VotingOption votingOption) {
        vote.setVote(votingOption);
        this.incrementVoteCounter(vote, votingOption);
    }

    private VotingOption validateVote(String vote) {
        try {
            return VotingOption.valueOf(vote);
        } catch (Exception e) {
            throw new BusinessException("invalid.voting.option");
        }
    }

    private boolean isVoteAlreadyRegistered(Vote vote) {
        return Objects.nonNull(vote.getVote());
    }

    private void incrementVoteCounter(Vote vote, VotingOption votingOption) {
        if (vote.getVote() != null) {
            if (VotingOption.yes.equals(votingOption)) {
                this.incrementYesOnVote(vote.getVotingSession());
            } else if (VotingOption.no.equals(votingOption)) {
                this.incrementNoOnVote(vote.getVotingSession());
            }
        }
    }

    private void incrementYesOnVote(VotingSession votingSession) {
        votingSession.setYesVotes(votingSession.getYesVotes() + 1);
    }

    private void incrementNoOnVote(VotingSession votingSession) {
        votingSession.setNoVotes(votingSession.getNoVotes() + 1);
    }

}
