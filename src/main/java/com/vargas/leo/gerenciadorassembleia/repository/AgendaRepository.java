package com.vargas.leo.gerenciadorassembleia.repository;

import com.vargas.leo.gerenciadorassembleia.domain.Agenda;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AgendaRepository {

    private final List<Agenda> agendaRepository = new ArrayList<>();

    public Agenda save(Agenda agenda) {
        agendaRepository.add(agenda);
        return agenda;
    }
}
