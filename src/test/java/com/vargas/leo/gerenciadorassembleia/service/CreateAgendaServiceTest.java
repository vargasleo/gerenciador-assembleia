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
import org.modelmapper.internal.bytebuddy.utility.RandomString;

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

    private final String userId = "mockUserId";
    private final String mockSubject = "mockSubject";

    @Test
    public void shouldCreateAgendaWhenValidUser() {
        CreateAgendaRequest request = new CreateAgendaRequest(userId, mockSubject);
        Agenda agenda = new Agenda(mockSubject, AgendaStatus.open);
        CreateAgendaResponse response = new CreateAgendaResponse(mockSubject, AgendaStatus.created, RandomString.make());

        when(agendaRepository.save(any(Agenda.class))).thenReturn(agenda);
        when(modelMapper.map(agenda, CreateAgendaResponse.class)).thenReturn(response);

        createAgendaService.create(request);

        verify(userValidator).validateUserId(userId);
        verify(agendaRepository).save(any(Agenda.class));
        verify(modelMapper).map(agenda, CreateAgendaResponse.class);
    }

    @Test(expected = BusinessException.class)
    public void shouldNotCreateAgendaWhenInvalidUser() {
        CreateAgendaRequest request = new CreateAgendaRequest(userId, mockSubject);

        doThrow(new BusinessException(userValidator.INVALID_USER)).when(userValidator).validateUserId(userId);

        createAgendaService.create(request);

        verify(userValidator).validateUserId(userId);
        verify(agendaRepository, never()).save(any());
        verify(modelMapper, never()).map(any(), CreateAgendaResponse.class);
    }
}