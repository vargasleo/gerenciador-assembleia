package com.vargas.leo.gerenciadorassembleia.service;

import com.vargas.leo.gerenciadorassembleia.controller.request.CreateAgendaRequest;
import com.vargas.leo.gerenciadorassembleia.controller.response.CreateAgendaResponse;
import com.vargas.leo.gerenciadorassembleia.domain.Agenda;
import com.vargas.leo.gerenciadorassembleia.domain.AgendaStatus;
import com.vargas.leo.gerenciadorassembleia.repository.AgendaRepository;
import com.vargas.leo.gerenciadorassembleia.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateAgendaService {

    private final AgendaRepository agendaRepository;
    private final UserValidator userValidator;
    private final ModelMapper mapper;

    public CreateAgendaResponse create(CreateAgendaRequest createAgendaRequest) {
        userValidator.validateUserId(createAgendaRequest.getUserId());
        Agenda agenda = new Agenda(createAgendaRequest.getSubject(), AgendaStatus.created);
        return mapper.map(agendaRepository.save(agenda), CreateAgendaResponse.class);
    }
}