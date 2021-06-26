package com.vargas.leo.gerenciadorassembleia.validator;

import com.vargas.leo.gerenciadorassembleia.domain.VotingSession;
import com.vargas.leo.gerenciadorassembleia.domain.enums.VotingSessionStatus;
import com.vargas.leo.gerenciadorassembleia.exception.BusinessException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

@Component
public class VotingSessionValidator {

    public void validateVotingSessionStatus(VotingSession votingSession) {
        if (Objects.isNull(votingSession.getStatus())) {
            throw new BusinessException("null.voting.session.status");
        }
        if (VotingSessionStatus.close.equals(votingSession.getStatus())) {
            throw new BusinessException("voting.session.already.closed");
        }
    }

    public boolean isNotValidDeadline(LocalDateTime deadline) {
        return LocalDateTime.now().isBefore(deadline);
    }

    public boolean isValidRequestDeadline(LocalDateTime deadline) {
        return LocalDateTime.now().isBefore(deadline);
    }
}
