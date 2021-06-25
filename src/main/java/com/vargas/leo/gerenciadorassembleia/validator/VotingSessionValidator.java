package com.vargas.leo.gerenciadorassembleia.validator;

import com.vargas.leo.gerenciadorassembleia.domain.VotingSession;
import com.vargas.leo.gerenciadorassembleia.domain.enums.VotingSessionStatus;
import com.vargas.leo.gerenciadorassembleia.exception.BusinessException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

@Component
public class VotingSessionValidator {

    public boolean validateFinalDateTime(LocalDateTime finalDateTime) {
        if (finalDateTime != null) {
            return LocalDateTime.now().isBefore(finalDateTime);
        }
        return false;
    }

    public void validateVotingSessionStatus(VotingSession votingSession) {
        if (Objects.isNull(votingSession.getStatus())) {
            throw new BusinessException("no.valid.voting.session.status");
        }
        if (VotingSessionStatus.closed.equals(votingSession.getStatus())) {
            throw new BusinessException("voting.session.already.closed");
        }
    }

}
