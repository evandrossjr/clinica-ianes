package com.sistema.clinica.services;


import com.sistema.clinica.models.Consulta;
import com.sistema.clinica.models.Medico;
import com.sistema.clinica.models.dtos.AgendaDisponivelDTO;
import com.sistema.clinica.models.dtos.EspacoVagoDTO;
import com.sistema.clinica.models.enums.DiaDaSemana;
import com.sistema.clinica.security.repositories.ConsultaRepository;
import com.sistema.clinica.security.repositories.MedicoRepository;
import jakarta.annotation.PostConstruct;
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

    public List<Medico> medicosDoDia() {
        DiaDaSemana hoje = DiaDaSemana.fromDayOfWeek(LocalDate.now().getDayOfWeek());
        return medicoRepository.findByDiasDisponiveisContaining(hoje);
    }

    public List<Consulta> consultasDoDia() {
        LocalDate hoje = LocalDate.now();
        return consultaRepository.findByData(hoje);
    }

    public List<EspacoVagoDTO> espacosVagosDoDia() {
        LocalDate hoje = LocalDate.now();
        DayOfWeek diaSemana = hoje.getDayOfWeek();

        if (diaSemana == DayOfWeek.SATURDAY || diaSemana == DayOfWeek.SUNDAY) {
            return List.of(); // retorna vazio se for fim de semana
        }

        DiaDaSemana dia = DiaDaSemana.fromDayOfWeek(diaSemana);
        List<Medico> medicos = medicoRepository.findByDiasDisponiveisContaining(dia);

        List<EspacoVagoDTO> espacos = new ArrayList<>();

        for (Medico medico : medicos) {
            List<LocalTime> horariosDisponiveis = new ArrayList<>(HORARIOS_FIXOS);

            List<Consulta> consultas = consultaRepository.findByMedicoAndData(medico, hoje);
            for (Consulta consulta : consultas) {
                horariosDisponiveis.remove(consulta.getHora());
            }

            for (LocalTime horario : horariosDisponiveis) {
                espacos.add(new EspacoVagoDTO(medico.getNome(), horario));
            }
        }

        return espacos;
    }

    public List<Consulta> proximasConsultas() {
        LocalDate hoje = LocalDate.now();
        LocalDate fim = hoje.plusDays(7);
        return consultaRepository.findByDataBetweenOrderByDataAscHoraAsc(hoje, fim);
    }

    // Método que retorna os horários disponíveis para um médico em uma data específica
    public List<LocalTime> getHorariosDisponiveis(Long idMedico, LocalDate data) {
        Optional<Medico> optMedico = medicoRepository.findById(idMedico);

        if (optMedico.isEmpty()) {
            throw new RuntimeException("Médico não encontrado com ID: " + idMedico);
        }

        Medico medico = optMedico.get();
        List<LocalTime> horariosDisponiveis = new ArrayList<>(HORARIOS_FIXOS);

        // Verifica se o médico trabalha naquele dia
        DayOfWeek diaSemana = data.getDayOfWeek();
        if (!medico.getDiasDisponiveis().contains(DiaDaSemana.fromDayOfWeek(diaSemana))) {
            return List.of(); // Retorna uma lista vazia se o médico não trabalha naquele dia
        }

        // Remove horários já agendados
        List<Consulta> consultas = consultaRepository.findByMedicoAndData(medico, data);
        for (Consulta consulta : consultas) {
            horariosDisponiveis.remove(consulta.getHora());
        }

        return horariosDisponiveis;
    }

    @PostConstruct
    public void testarMetodo() {
        List<Consulta> consultas = proximasConsultas();
        System.out.println("CONSULTAS FUTURAS:");
        consultas.forEach(c -> System.out.println(c.getData() + " " + c.getHora()));
    }



}
