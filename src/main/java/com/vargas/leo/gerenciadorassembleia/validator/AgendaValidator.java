package com.vargas.leo.gerenciadorassembleia.validator;

import com.vargas.leo.gerenciadorassembleia.exception.BusinessException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class AgendaValidator {

    public final String INVALID_SUBJECT = "invalid.agenda.subject";

    public void validateAgendaSubject(String subject) {
        if (!StringUtils.hasText(subject)) {
            throw new BusinessException(INVALID_SUBJECT);
        }
    }
}
