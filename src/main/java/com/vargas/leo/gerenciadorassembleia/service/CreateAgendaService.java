package com.vargas.leo.gerenciadorassembleia.service;

import com.vargas.leo.gerenciadorassembleia.controller.request.CreateAgendaRequest;
import com.vargas.leo.gerenciadorassembleia.controller.response.CreateAgendaResponse;
import com.vargas.leo.gerenciadorassembleia.domain.Agenda;
import com.vargas.leo.gerenciadorassembleia.domain.enums.AgendaStatus;
import com.vargas.leo.gerenciadorassembleia.repository.AgendaRepository;
import com.vargas.leo.gerenciadorassembleia.validator.AgendaValidator;
import com.vargas.leo.gerenciadorassembleia.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateAgendaService {

    private final AgendaRepository agendaRepository;
    private final UserValidator userValidator;
    private final AgendaValidator agendaValidator;
    private final ModelMapper mapper;

    public CreateAgendaResponse createAgenda(CreateAgendaRequest createAgendaRequest) {
        return mapper.map(this.create(createAgendaRequest), CreateAgendaResponse.class);
    }

    protected Agenda create(CreateAgendaRequest createAgendaRequest) {
        agendaValidator.validateAgendaSubject(createAgendaRequest.getSubject());
        userValidator.validateUserId(createAgendaRequest.getUserId());
        Agenda agenda = new Agenda();
        agenda.setSubject(createAgendaRequest.getSubject());
        agenda.setStatus(AgendaStatus.created);
        agendaRepository.save(agenda);
        return agenda;
    }

}
