package com.vargas.leo.gerenciadorassembleia.validator;

import com.vargas.leo.gerenciadorassembleia.exception.BusinessException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class UserValidatorTest {

    @InjectMocks
    private UserValidator userValidator;

    @Test
    public void ShouldDoNothingWhenValidUserId() {
        String userId = "mockUserId";
        userValidator.validateUserId(userId);
    }

    @Test(expected = BusinessException.class)
    public void ShouldThrowExceptionWhenInvalidUserId() {
        String expectedMessage = userValidator.INVALID_USER;
        try {
            userValidator.validateUserId(null);
        } catch (BusinessException e) {
            assertEquals(expectedMessage, e.getMessage());
            throw e;
        }
    }
}