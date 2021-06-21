package com.vargas.leo.gerenciadorassembleia.repository;

import com.vargas.leo.gerenciadorassembleia.domain.Agenda;
import com.vargas.leo.gerenciadorassembleia.exception.NotFoundException;
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

    public Agenda findById(String agendaId) {
        for (Agenda a : agendaRepository) {
            if(a.getId().equals(agendaId)) {
                return a;
            }
        }
        return null;
    }
}
