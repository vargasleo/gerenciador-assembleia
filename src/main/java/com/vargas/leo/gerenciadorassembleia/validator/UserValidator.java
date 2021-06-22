package com.vargas.leo.gerenciadorassembleia.validator;

import com.vargas.leo.gerenciadorassembleia.exception.BusinessException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class UserValidator {

    public final String INVALID_USER = "invalid.user.id";

    public void validateUserId(String userId) {
        if (!StringUtils.hasText(userId)) {
            throw new BusinessException(INVALID_USER);
        }
    }

}
