package com.vargas.leo.gerenciadorassembleia.controller.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vargas.leo.gerenciadorassembleia.domain.enums.VotingResult;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FinishVotingResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime endedAt;
    private VotingResult result;
    private int yesVotes;
    private int noVotes;
    private Integer id;
    private String agendaSubject;

}
