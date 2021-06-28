package com.vargas.leo.gerenciadorassembleia.service;

import com.vargas.leo.gerenciadorassembleia.controller.request.CreateAgendaRequest;
import com.vargas.leo.gerenciadorassembleia.domain.Agenda;
import com.vargas.leo.gerenciadorassembleia.domain.User;
import com.vargas.leo.gerenciadorassembleia.exception.BusinessException;
import com.vargas.leo.gerenciadorassembleia.exception.NotFoundException;
import com.vargas.leo.gerenciadorassembleia.repository.AgendaRepository;
import com.vargas.leo.gerenciadorassembleia.repository.UserRepository;
import com.vargas.leo.gerenciadorassembleia.validator.AgendaValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CreateAgendaServiceTest {

    @InjectMocks
    private CreateAgendaService createAgendaService;

    @Mock
    private AgendaRepository agendaRepository;

    @Mock
    private AgendaValidator agendaValidator;

    @Mock
    private UserRepository userRepository;

    private final Integer userId = 1;
    private final String mockSubject = "mockSubject";

    @Test(expected = BusinessException.class)
    public void shouldNotCreateAgendaWhenHasNoSubject() {
        CreateAgendaRequest request = new CreateAgendaRequest();
        request.setUserId(userId);

        doThrow(new BusinessException(agendaValidator.INVALID_SUBJECT))
                .when(agendaValidator).validateAgendaSubject(null);

        try {
            createAgendaService.create(request);
        } catch (BusinessException e) {
            assertEquals(agendaValidator.INVALID_SUBJECT, e.getMessage());

            verify(agendaValidator).validateAgendaSubject(null);
            verify(agendaRepository, never()).save(any(Agenda.class));

            throw e;
        }

    }

    @Test(expected = NotFoundException.class)
    public void shouldNotCreateAgendaWhenInvalidUser() {
        CreateAgendaRequest request = new CreateAgendaRequest();
        request.setSubject(mockSubject);

        when(userRepository.findById(null)).thenReturn(Optional.empty());

        try {
            createAgendaService.create(request);
        } catch (NotFoundException e) {
            assertEquals("user.not.found", e.getMessage());

            verify(agendaValidator).validateAgendaSubject(mockSubject);
            verify(userRepository).findById(null);
            verify(agendaRepository, never()).save(any());

            throw e;
        }

    }

    @Test
    public void shouldCreateAgendaWhenValidUserAndValidSubject() {
        CreateAgendaRequest request = new CreateAgendaRequest();
        request.setUserId(userId);
        request.setSubject(mockSubject);

        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));

        Agenda result = createAgendaService.create(request);

        assertEquals(mockSubject, result.getSubject());

        verify(agendaValidator).validateAgendaSubject(mockSubject);
        verify(userRepository).findById(userId);
        verify(agendaRepository).save(result);
    }

}
