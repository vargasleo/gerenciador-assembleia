package com.vargas.leo.gerenciadorassembleia.validator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class VotingSessionValidatorTest {

    @InjectMocks
    private VotingSessionValidator votingSessionValidator;

    @Test
    public void shouldReturnTrueWhenFinalDateTimeIsAfterNow() {
        LocalDateTime finalDateTime = LocalDateTime.of(LocalDate.of(2022,1,1), LocalTime.of(0,0));
        boolean result = votingSessionValidator.isValidDeadline(finalDateTime);
        assertTrue(result);
    }

    @Test
    public void shouldReturnFalseWhenFinalDateTimeIsBeforeNow() {
        LocalDateTime finalDateTime = LocalDateTime.of(LocalDate.of(1999,1,1), LocalTime.of(0,0));
        boolean result = votingSessionValidator.isValidDeadline(finalDateTime);
        assertFalse(result);
    }

}
