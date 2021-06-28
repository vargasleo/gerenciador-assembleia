package com.vargas.leo.gerenciadorassembleia.validator;

import com.vargas.leo.gerenciadorassembleia.exception.BusinessException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class AgendaValidatorTest {

    @InjectMocks
    private AgendaValidator agendaValidator;

    @Test
    public void shouldDoNothingWhenValidUserId() {
        String subject = "mockHasTextAgenda";
        agendaValidator.validateAgendaSubject(subject);
    }

    @Test(expected = BusinessException.class)
    public void shouldThrowExceptionWhenInvalidUserId() {
        try {
            agendaValidator.validateAgendaSubject(null);
        } catch (BusinessException e) {
            assertEquals(agendaValidator.INVALID_SUBJECT, e.getMessage());
            throw e;
        }
    }

}
