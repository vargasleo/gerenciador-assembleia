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
        userValidator.validateUserId(1);
    }

    @Test(expected = BusinessException.class)
    public void shouldThrowExceptionWhenInvalidUserId() {
        try {
            userValidator.validateUserId(null);
        } catch (BusinessException e) {
            assertEquals(userValidator.INVALID_USER_ID, e.getMessage());
            throw e;
        }
    }

    @Test(expected = BusinessException.class)
    public void shouldThrowExceptionWhenUserNameIsNull() {
        try {
            userValidator.validateUserName(null);
        } catch (BusinessException e) {
            assertEquals(userValidator.INVALID_USER_NAME, e.getMessage());
            throw e;
        }
    }

    @Test(expected = BusinessException.class)
    public void shouldThrowExceptionWhenUserNameIsEmpty() {
        try {
            userValidator.validateUserName("");
        } catch (BusinessException e) {
            assertEquals(userValidator.INVALID_USER_NAME, e.getMessage());
            throw e;
        }
    }

    public void shouldDoNothingWhenValidUserName() {
        userValidator.validateUserName("validUserName");
    }

    @Test(expected = BusinessException.class)
    public void shouldThrowExceptionWhenNullUserCpf() {
        try {
            userValidator.validateCpf(null);
        } catch (BusinessException e) {
            assertEquals(userValidator.INVALID_USER_CPF, e.getMessage());
            throw e;
        }
    }

    @Test(expected = BusinessException.class)
    public void shouldThrowExceptionWhenEmptyUserCpf() {
        try {
            userValidator.validateCpf("");
        } catch (BusinessException e) {
            assertEquals(userValidator.INVALID_USER_CPF, e.getMessage());
            throw e;
        }
    }

    @Test(expected = BusinessException.class)
    public void shouldThrowExceptionWhenEmptyInvalidCpf() {
        try {
            userValidator.validateCpf("123");
        } catch (BusinessException e) {
            assertEquals(userValidator.INVALID_USER_CPF, e.getMessage());
            throw e;
        }
    }

    @Test
    public void shouldDoNothingWhenValidCpf() {
        userValidator.validateCpf("04430454027");
    }

}
