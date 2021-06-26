package com.vargas.leo.gerenciadorassembleia.validator;

import com.vargas.leo.gerenciadorassembleia.exception.BusinessException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class UserValidator {

    public final String INVALID_USER_ID = "invalid.user.id";
    public final String INVALID_USER_NAME = "invalid.user.name";
    public final String INVALID_USER_CPF = "invalid.user.cpf";

    public void validateUserRegister(String name, String cpf) {
        this.validateUserName(name);
        this.validateCpf(cpf);
    }

    public void validateUserName(String name) {
        if (!StringUtils.hasText(name)) {
            throw new BusinessException(INVALID_USER_NAME);
        }
    }

    public void validateCpf(String cpf) {
        if (!StringUtils.hasText(cpf)) {
            throw new BusinessException(INVALID_USER_CPF);
        }

        if (cpf.length()!=11) {
            throw new BusinessException(INVALID_USER_CPF);
        }
    }

    public void validateUserId(Integer userId) {
        if (userId == null) {
            throw new BusinessException(INVALID_USER_ID);
        }
    }

}
