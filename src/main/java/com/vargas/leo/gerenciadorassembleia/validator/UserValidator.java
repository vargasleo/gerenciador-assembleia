package com.vargas.leo.gerenciadorassembleia.validator;

import com.vargas.leo.gerenciadorassembleia.exception.BusinessException;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {

    public final String INVALID_USER = "invalid.user.id";

    public void validateUserId(Integer userId) {
        if (userId == null) {
            throw new BusinessException(INVALID_USER);
        }
    }

}
