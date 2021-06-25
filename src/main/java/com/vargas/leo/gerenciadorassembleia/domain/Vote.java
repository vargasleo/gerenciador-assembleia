package com.vargas.leo.gerenciadorassembleia.domain;

import com.vargas.leo.gerenciadorassembleia.domain.enums.VotingOption;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "vote")
@Getter
@Setter
@RequiredArgsConstructor
public class Vote {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "id_vote")
    private String id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user")
    private final User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_voting_session")
    private final VotingSession votingSession;

    @Column(name = "created_at")
    private final LocalDateTime votedAt = LocalDateTime.now();

    @Enumerated(value = EnumType.STRING)
    @Column(name = "voting_option")
    private VotingOption vote;

}
