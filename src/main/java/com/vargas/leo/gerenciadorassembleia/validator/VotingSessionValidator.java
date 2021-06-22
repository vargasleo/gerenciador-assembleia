package com.vargas.leo.gerenciadorassembleia.validator;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class VotingSessionValidator {

    public boolean validateFinalDateTime(LocalDateTime finalDateTime) {
        if (finalDateTime != null) {
            return LocalDateTime.now().isBefore(finalDateTime);
        }
        return false;
    }

}
