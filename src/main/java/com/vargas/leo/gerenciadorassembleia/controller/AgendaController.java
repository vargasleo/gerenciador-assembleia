package com.vargas.leo.gerenciadorassembleia.controller;

import com.vargas.leo.gerenciadorassembleia.controller.request.CreateAgendaRequest;
import com.vargas.leo.gerenciadorassembleia.controller.request.CreateVotingSessionRequest;
import com.vargas.leo.gerenciadorassembleia.controller.request.VoteRequest;
import com.vargas.leo.gerenciadorassembleia.controller.response.CreateAgendaResponse;
import com.vargas.leo.gerenciadorassembleia.controller.response.CreateVotingSessionResponse;
import com.vargas.leo.gerenciadorassembleia.controller.response.VoteResponse;
import com.vargas.leo.gerenciadorassembleia.service.CreateAgendaService;
import com.vargas.leo.gerenciadorassembleia.service.CreateVotingSessionService;
import com.vargas.leo.gerenciadorassembleia.service.RegisterVoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("agenda")
@RequiredArgsConstructor
public class AgendaController {

    private final CreateAgendaService createAgendaService;
    private final CreateVotingSessionService createVotingSessionService;
    private final RegisterVoteService registerVoteService;

    @PostMapping("create/agenda")
    @ResponseStatus(HttpStatus.CREATED)
    public CreateAgendaResponse createAgenda(@RequestBody @Valid CreateAgendaRequest agendaRequest) {
        return createAgendaService.createAgenda(agendaRequest);
    }

    @PostMapping("create/voting-session")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public CreateVotingSessionResponse createVotingSession(@RequestBody @Valid CreateVotingSessionRequest votingSessionRequest) {
        return createVotingSessionService.createVotingSession(votingSessionRequest);
    }

    @PostMapping("vote")
    @ResponseStatus(HttpStatus.OK)
    public VoteResponse registerVote(@RequestBody @Valid VoteRequest voteRequest) {
        return registerVoteService.registerVote(voteRequest);
    }

}
