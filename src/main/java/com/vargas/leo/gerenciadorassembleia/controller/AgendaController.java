package com.vargas.leo.gerenciadorassembleia.controller;

import com.vargas.leo.gerenciadorassembleia.controller.request.CreateAgendaRequest;
import com.vargas.leo.gerenciadorassembleia.controller.response.CreateAgendaResponse;
import com.vargas.leo.gerenciadorassembleia.service.CreateAgendaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("agenda")
@RequiredArgsConstructor
public class AgendaController {

    private final CreateAgendaService createAgendaService;

    @PostMapping("create")
    @ResponseStatus(HttpStatus.CREATED)
    public CreateAgendaResponse createAgenda(@RequestBody @Valid CreateAgendaRequest agendaRequest) {
        return createAgendaService.createAgenda(agendaRequest);
    }

}
