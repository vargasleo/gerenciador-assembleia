package com.vargas.leo.gerenciadorassembleia.service;

import com.vargas.leo.gerenciadorassembleia.controller.request.CreateAgendaRequest;
import com.vargas.leo.gerenciadorassembleia.controller.response.CreateAgendaResponse;
import com.vargas.leo.gerenciadorassembleia.domain.Agenda;
import com.vargas.leo.gerenciadorassembleia.domain.enums.AgendaStatus;
import com.vargas.leo.gerenciadorassembleia.exception.NotFoundException;
import com.vargas.leo.gerenciadorassembleia.repository.AgendaRepository;
import com.vargas.leo.gerenciadorassembleia.repository.UserRepository;
import com.vargas.leo.gerenciadorassembleia.validator.AgendaValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateAgendaService {

    private final AgendaRepository agendaRepository;
    private final UserRepository userRepository;
    private final AgendaValidator agendaValidator;

    public CreateAgendaResponse createAgenda(CreateAgendaRequest createAgendaRequest) {
        Agenda agenda = this.create(createAgendaRequest);

        return CreateAgendaResponse.builder()
                .id(agenda.getId())
                .status(agenda.getStatus())
                .subject(agenda.getSubject())
                .build();
    }

    protected Agenda create(CreateAgendaRequest createAgendaRequest) {
        agendaValidator.validateAgendaSubject(createAgendaRequest.getSubject());

        userRepository.findById(createAgendaRequest.getUserId())
                .orElseThrow(() -> new NotFoundException("user.not.found"));

        Agenda agenda = Agenda.builder()
                .subject(createAgendaRequest.getSubject())
                .status(AgendaStatus.created)
                .build();

        agendaRepository.save(agenda);

        return agenda;
    }

}
