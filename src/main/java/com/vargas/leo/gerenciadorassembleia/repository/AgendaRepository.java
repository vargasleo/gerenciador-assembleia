package com.vargas.leo.gerenciadorassembleia.repository;

import com.vargas.leo.gerenciadorassembleia.domain.Agenda;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface AgendaRepository extends Repository<Agenda, Integer> {

    Agenda save(Agenda agenda);

    Optional<Agenda> findById(String agendaId);

}
