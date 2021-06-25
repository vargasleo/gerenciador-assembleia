package com.vargas.leo.gerenciadorassembleia.domain;

import com.vargas.leo.gerenciadorassembleia.domain.enums.AgendaStatus;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;


@Entity
@Table(name = "agenda")
@Getter
@Setter
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
