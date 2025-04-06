package com.sistema.clinica.services;


import com.sistema.clinica.models.Consulta;
import com.sistema.clinica.models.Medico;
import com.sistema.clinica.models.dtos.AgendaDisponivelDTO;
import com.sistema.clinica.models.enums.DiaDaSemana;
import com.sistema.clinica.repositories.ConsultaRepository;
import com.sistema.clinica.repositories.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
public class AgendaService {

    private final ConsultaRepository consultaRepository;
    private final MedicoRepository medicoRepository;


    @Autowired
    public AgendaService(ConsultaRepository consultaRepository, MedicoRepository medicoRepository) {
        this.consultaRepository = consultaRepository;
        this.medicoRepository = medicoRepository;
    }

    private static final List<LocalTime> HORARIOS_FIXOS = List.of(
            LocalTime.of(9, 0), LocalTime.of(9, 30),
            LocalTime.of(10, 0), LocalTime.of(10, 30),
            LocalTime.of(11, 0), LocalTime.of(11, 30),
            LocalTime.of(14, 0), LocalTime.of(14, 30),
            LocalTime.of(15, 0), LocalTime.of(15, 30)
    );

    public List<AgendaDisponivelDTO> listarAgenda(Long idMedico) {
        Optional<Medico> optMedico = medicoRepository.findById(idMedico);

        if (optMedico.isEmpty()) {
            throw new RuntimeException("Médico não encontrado com ID: " + idMedico);
        }

        Medico medico = optMedico.get();
        List<AgendaDisponivelDTO> agendaFinal = new ArrayList<>();

        LocalDate data = LocalDate.now();
        int diasAdicionados = 0;

        while (diasAdicionados < 7) {
            DayOfWeek diaSemana = data.getDayOfWeek();
            boolean ehDiaUtil = diaSemana != DayOfWeek.SATURDAY && diaSemana != DayOfWeek.SUNDAY;

            if (ehDiaUtil && medico.getDiasDisponiveis().contains(DiaDaSemana.fromDayOfWeek(diaSemana))) {

                List<LocalTime> horariosDisponiveis = new ArrayList<>(HORARIOS_FIXOS);

                // Remove horários já agendados
                List<Consulta> consultas = consultaRepository.findByMedicoAndData(medico, data);
                for (Consulta consulta : consultas) {
                    horariosDisponiveis.remove(consulta.getHora());
                }

                agendaFinal.add(new AgendaDisponivelDTO(data, horariosDisponiveis));
                diasAdicionados++;
            }

            data = data.plusDays(1);
        }

        return agendaFinal;
    }

    private boolean ehFinalDeSemana(LocalDate data) {
        DiaDaSemana dia = DiaDaSemana.fromDayOfWeek(data.getDayOfWeek());
        return dia == DiaDaSemana.SABADO || dia == DiaDaSemana.DOMINGO;
    }

}
