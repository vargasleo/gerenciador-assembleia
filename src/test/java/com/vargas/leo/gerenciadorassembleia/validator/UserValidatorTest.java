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
    public void shouldDoNothingWhenValidUserId() {
        Integer userId = 1;
        userValidator.validateUserId(userId);
    }

    @Test(expected = BusinessException.class)
    public void shouldThrowExceptionWhenInvalidUserId() {
        try {
            userValidator.validateUserId(null);
        } catch (BusinessException e) {
            assertEquals(userValidator.INVALID_USER, e.getMessage());
            throw e;
        }
    }

}
