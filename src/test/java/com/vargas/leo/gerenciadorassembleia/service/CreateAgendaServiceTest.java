package com.vargas.leo.gerenciadorassembleia.service;

import com.vargas.leo.gerenciadorassembleia.controller.request.CreateAgendaRequest;
import com.vargas.leo.gerenciadorassembleia.domain.Agenda;
import com.vargas.leo.gerenciadorassembleia.exception.BusinessException;
import com.vargas.leo.gerenciadorassembleia.repository.AgendaRepository;
import com.vargas.leo.gerenciadorassembleia.validator.AgendaValidator;
import com.vargas.leo.gerenciadorassembleia.validator.UserValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import static org.junit.Assert.assertEquals;
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
    private AgendaValidator agendaValidator;

    @Mock
    private ModelMapper modelMapper;

    private final String userId = "mockUserId";
    private final String mockSubject = "mockSubject";

    @Test(expected = BusinessException.class)
    public void shouldNotCreateAgendaWhenHasNoSubject() {
        CreateAgendaRequest request = new CreateAgendaRequest(userId, null);

        doThrow(new BusinessException(agendaValidator.INVALID_SUBJECT))
                .when(agendaValidator).validateAgendaSubject(null);

        try {
            createAgendaService.create(request);
        } catch (BusinessException e) {
            assertEquals(agendaValidator.INVALID_SUBJECT, e.getMessage());

            verify(agendaValidator).validateAgendaSubject(null);
            verify(userValidator, never()).validateUserId(userId);
            verify(agendaRepository, never()).save(any(Agenda.class));

            throw e;
        }

    }

    @Test(expected = BusinessException.class)
    public void shouldNotCreateAgendaWhenInvalidUser() {
        CreateAgendaRequest request = new CreateAgendaRequest(userId, mockSubject);

        doThrow(new BusinessException(userValidator.INVALID_USER)).when(userValidator).validateUserId(userId);

        try {
            createAgendaService.create(request);
        } catch (BusinessException e) {
            assertEquals(userValidator.INVALID_USER, e.getMessage());

            verify(agendaValidator).validateAgendaSubject(mockSubject);
            verify(userValidator).validateUserId(userId);
            verify(agendaRepository, never()).save(any());

            throw e;
        }

    }

    @Test
    public void shouldCreateAgendaWhenValidUserAndValidSubject() {
        CreateAgendaRequest request = new CreateAgendaRequest(userId, mockSubject);

        Agenda result = createAgendaService.create(request);

        assertEquals(mockSubject, result.getSubject());

        verify(userValidator).validateUserId(userId);
        verify(agendaRepository).save(result);
    }

}
