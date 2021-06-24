package com.vargas.leo.gerenciadorassembleia.controller;

import com.vargas.leo.gerenciadorassembleia.controller.request.CreateVotingSessionRequest;
import com.vargas.leo.gerenciadorassembleia.controller.request.FinishVotingRequest;
import com.vargas.leo.gerenciadorassembleia.controller.request.VoteRequest;
import com.vargas.leo.gerenciadorassembleia.controller.response.CreateVotingSessionResponse;
import com.vargas.leo.gerenciadorassembleia.controller.response.FinishVotingResponse;
import com.vargas.leo.gerenciadorassembleia.controller.response.VoteResponse;
import com.vargas.leo.gerenciadorassembleia.service.CreateVotingSessionService;
import com.vargas.leo.gerenciadorassembleia.service.FinishVotingService;
import com.vargas.leo.gerenciadorassembleia.service.RegisterVoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("voting-session")
@RequiredArgsConstructor
public class VotingSessionController {

    private final CreateVotingSessionService createVotingSessionService;
    private final RegisterVoteService registerVoteService;
    private final FinishVotingService finishVotingService;

    @PostMapping("create")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public CreateVotingSessionResponse createVotingSession(@RequestBody @Valid CreateVotingSessionRequest votingSessionRequest) {
        return createVotingSessionService.createVotingSession(votingSessionRequest);
    }

    @PostMapping("vote")
    @ResponseStatus(HttpStatus.OK)
    public VoteResponse registerVote(@RequestBody @Valid VoteRequest voteRequest) {
        return registerVoteService.registerVote(voteRequest);
    }

    @PostMapping("finish")
    @ResponseStatus(HttpStatus.OK)
    public FinishVotingResponse finishVoting(@RequestBody @Valid FinishVotingRequest finishVotingRequest) {
        return finishVotingService.finishVoting(finishVotingRequest);
    }

}
