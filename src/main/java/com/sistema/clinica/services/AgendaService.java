package com.sistema.clinica.services;


import com.sistema.clinica.models.Consulta;
import com.sistema.clinica.models.Medico;
import com.sistema.clinica.models.enums.DiaDaSemana;
import com.sistema.clinica.repositories.ConsultaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class AgendaService {

    private final ConsultaRepository consultaRepository;


    @Autowired
    public AgendaService(ConsultaRepository consultaRepository) {
        this.consultaRepository = consultaRepository;
    }

    private static final List<LocalTime> HORARIOS_FIXOS = List.of(
            LocalTime.of(9, 0), LocalTime.of(9, 30),
            LocalTime.of(10, 0), LocalTime.of(10, 30),
            LocalTime.of(11, 0), LocalTime.of(11, 30),
            LocalTime.of(14, 0), LocalTime.of(14, 30),
            LocalTime.of(15, 0), LocalTime.of(15, 30)
    );

    public Map<LocalDate, List<LocalTime>> gerarAgendaDisponivel(Medico medico) {
        Map<LocalDate, List<LocalTime>> agenda = new LinkedHashMap<>();
        LocalDate hoje = LocalDate.now();
        int diasAdicionados = 0;

        while (diasAdicionados < 7) {
            if (!ehFinalDeSemana(hoje) && medico.getDiasDisponiveis().contains(hoje.getDayOfWeek())) {
                List<Consulta> consultasDoDia = consultaRepository.findByMedicoAndData(medico, hoje);
                List<LocalTime> horariosOcupados = consultasDoDia.stream()
                        .map(Consulta::getHora)
                        .toList();

                List<LocalTime> horariosDisponiveis = new ArrayList<>(HORARIOS_FIXOS);
                horariosDisponiveis.removeAll(horariosOcupados);

                agenda.put(hoje, horariosDisponiveis);
                diasAdicionados++;
            }
            hoje = hoje.plusDays(1);
        }

        return agenda;
    }

    private boolean ehFinalDeSemana(LocalDate data) {
        DiaDaSemana dia = DiaDaSemana.fromDayOfWeek(data.getDayOfWeek());
        return dia == DiaDaSemana.SABADO || dia == DiaDaSemana.DOMINGO;
    }

}
