package com.vargas.leo.gerenciadorassembleia.service;

import com.vargas.leo.gerenciadorassembleia.controller.request.FinishVotingRequest;
import com.vargas.leo.gerenciadorassembleia.controller.request.VoteRequest;
import com.vargas.leo.gerenciadorassembleia.controller.response.VoteResponse;
import com.vargas.leo.gerenciadorassembleia.domain.User;
import com.vargas.leo.gerenciadorassembleia.domain.Vote;
import com.vargas.leo.gerenciadorassembleia.domain.VotingPowerDTO;
import com.vargas.leo.gerenciadorassembleia.domain.VotingSession;
import com.vargas.leo.gerenciadorassembleia.domain.enums.VotingOption;
import com.vargas.leo.gerenciadorassembleia.domain.enums.VotingPower;
import com.vargas.leo.gerenciadorassembleia.exception.BusinessException;
import com.vargas.leo.gerenciadorassembleia.exception.NotFoundException;
import com.vargas.leo.gerenciadorassembleia.repository.UserRepository;
import com.vargas.leo.gerenciadorassembleia.repository.VoteRepository;
import com.vargas.leo.gerenciadorassembleia.repository.VotingSessionRepository;
import com.vargas.leo.gerenciadorassembleia.validator.UserValidator;
import com.vargas.leo.gerenciadorassembleia.validator.VotingSessionValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterVoteService {

    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final VotingSessionRepository votingSessionRepository;
    private final VotingSessionValidator votingSessionValidator;
    private final CpfValidationService cpfValidationService;
    private final FinishVotingService finishVotingService;
    private final UserValidator userValidator;

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
            log.error("user.already.voted.in.this.session");
            throw new BusinessException("user.already.voted.in.this.session");
        }

        votingSessionValidator.validateVotingSessionStatus(vote.getVotingSession());

        if (votingSessionValidator.isNotValidDeadline(votingSession.getDeadline())) {
            FinishVotingRequest finishVotingRequest = FinishVotingRequest.builder()
                    .userId(user.getId())
                    .votingSessionId(votingSession.getId())
                    .build();

            finishVotingService.finishVotingAndCountVotes(finishVotingRequest);

            throw new BusinessException("voting.session.reached.deadline");
        }

        VotingOption votingOption = this.validateVote(voteRequest.getVote());

        userValidator.validateCpf(user.getCpf());

        if (!this.isUserAbleToVote(user.getCpf())) {
            log.error("cpf.is.unable.to.vote");
            throw new BusinessException("cpf.is.unable.to.vote");
        }

        this.registerVote(vote, votingOption);

        voteRepository.save(vote);
        return vote;
    }

    private boolean isUserAbleToVote(String cpf) {
        try {
            VotingPowerDTO votingPowerDTO = cpfValidationService.isAllowedToVote(cpf);
            return VotingPower.ABLE_TO_VOTE.equals(votingPowerDTO.getStatus());
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            throw new BusinessException("fail.to.connect.to.cpf.validaton.service");
        }
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
        log.info("registering.voting.option");
    }

    private VotingOption validateVote(String vote) {
        try {
            return VotingOption.valueOf(vote);
        } catch (Exception e) {
            log.error("invalid.voting.option");
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
