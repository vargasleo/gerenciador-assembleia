package com.vargas.leo.gerenciadorassembleia.domain;

import com.vargas.leo.gerenciadorassembleia.domain.enums.AgendaStatus;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "agenda")
public class Agenda {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "id_agenda")
    private Integer id;

    @Column(name = "subject")
    private String subject;

    @OneToOne(mappedBy="agenda")
    private VotingSession votingSession;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private AgendaStatus status;

}

