package com.vargas.leo.gerenciadorassembleia.service;

import com.vargas.leo.gerenciadorassembleia.controller.request.CreateAgendaRequest;
import com.vargas.leo.gerenciadorassembleia.controller.response.CreateAgendaResponse;
import com.vargas.leo.gerenciadorassembleia.domain.Agenda;
import com.vargas.leo.gerenciadorassembleia.domain.AgendaStatus;
import com.vargas.leo.gerenciadorassembleia.exception.BusinessException;
import com.vargas.leo.gerenciadorassembleia.repository.AgendaRepository;
import com.vargas.leo.gerenciadorassembleia.validator.UserValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CreateAgendaServiceTest {

    @InjectMocks
    private CreateAgendaService createAgendaService;

    @Mock
    private UserValidator userValidator;

    @Mock
    private AgendaRepository agendaRepository;

    @Mock
    private ModelMapper modelMapper;

    @Test
    public void ShouldCreateAgendaWhenValidUser() {
        String userId = "mockUserId";
        String subject = "mockSubject";
        CreateAgendaRequest request = new CreateAgendaRequest(userId, subject);
        Agenda agenda = new Agenda(subject, AgendaStatus.open);
        CreateAgendaResponse response = new CreateAgendaResponse(subject, AgendaStatus.open);

        when(agendaRepository.save(any(Agenda.class))).thenReturn(agenda);
        when(modelMapper.map(agenda, CreateAgendaResponse.class)).thenReturn(response);

        createAgendaService.create(request);

        verify(userValidator).validateUserId(userId);
        verify(agendaRepository).save(any(Agenda.class));
        verify(modelMapper).map(agenda, CreateAgendaResponse.class);
    }

    @Test(expected = BusinessException.class)
    public void ShouldNotCreateAgendaWhenInvalidUser() {
        String userId = "mockUserId";
        String subject = "mockSubject";
        CreateAgendaRequest request = new CreateAgendaRequest(userId, subject);

        doThrow(new BusinessException(userValidator.INVALID_USER)).when(userValidator).validateUserId(userId);

        createAgendaService.create(request);

        verify(userValidator).validateUserId(userId);
        verify(agendaRepository, never()).save(any());
        verify(modelMapper, never()).map(any(), CreateAgendaResponse.class);
    }
}