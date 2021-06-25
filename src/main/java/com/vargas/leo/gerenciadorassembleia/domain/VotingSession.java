package com.vargas.leo.gerenciadorassembleia.domain;

import com.vargas.leo.gerenciadorassembleia.domain.enums.VotingResult;
import com.vargas.leo.gerenciadorassembleia.domain.enums.VotingSessionStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "voting-session")
@Getter
@Setter
@NoArgsConstructor
public class VotingSession {

    public static final LocalDateTime DEFAULT_FINAL_DATE_TIME = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 3, 0));

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "id_voting_session")
    private String id;

    @Column(name = "created_at")
    private final LocalDateTime createdAt = LocalDateTime.now();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_agenda", referencedColumnName = "id_agenda")
    private Agenda agenda;

    @Column(name = "ended_at")
    private LocalDateTime endedAt;

    @Column(name = "deadline")
    private LocalDateTime finalDateTime = DEFAULT_FINAL_DATE_TIME;

    @Enumerated(value = EnumType.STRING)

    @Column(name = "result")
    private VotingResult result;

    @Transient
    private VotingResult looserOption;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "voting_session_status")
    private VotingSessionStatus status;

    @Column(name = "yes_votes")
    private int yesVotes = 0;

    @Column(name = "no_votes")
    private int noVotes = 0;

}
