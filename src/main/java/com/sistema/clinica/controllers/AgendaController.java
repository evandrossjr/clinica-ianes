package com.sistema.clinica.controllers;

import com.sistema.clinica.models.Consulta;
import com.sistema.clinica.models.dtos.AgendaDisponivelDTO;
import com.sistema.clinica.models.dtos.HorarioDisponivelDTO;
import com.sistema.clinica.services.AgendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/agenda")
public class AgendaController {
    private final AgendaService agendaService;


    @Autowired
    public AgendaController(AgendaService agendaService) {
        this.agendaService = agendaService;
    }

    @GetMapping("/{idMedico}")
    public ResponseEntity<List<AgendaDisponivelDTO>> consultarAgenda(@PathVariable Long idMedico) {
        List<AgendaDisponivelDTO> agenda = agendaService.listarAgenda(idMedico);
        return ResponseEntity.ok(agenda);
    }

    @GetMapping("/proximas-consultas")
    public List<Map<String, Object>> listarProximasConsultas() {
        return agendaService.proximasConsultas().stream().map(consulta -> {
            Map<String, Object> map = new HashMap<>();
            map.put("data", consulta.getData().toString());
            map.put("hora", consulta.getHora().toString());
            map.put("paciente", Map.of("nome", consulta.getPaciente().getNome()));
            map.put("medico", Map.of("nome", consulta.getMedico().getNome()));
            return map;
        }).collect(Collectors.toList());
    }

    // Endpoint para obter horários disponíveis por médico e data
    @GetMapping("/{idMedico}/horarios")
    public ResponseEntity<List<LocalTime>> consultarHorariosDisponiveis(@PathVariable Long idMedico, @RequestParam LocalDate data) {
        List<LocalTime> horarios = agendaService.getHorariosDisponiveis(idMedico, data);
        return ResponseEntity.ok(horarios);
    }
    @GetMapping("/{idMedico}/disponibilidades")
    public ResponseEntity<List<HorarioDisponivelDTO>> listarDisponibilidades(@PathVariable Long idMedico) {
        List<AgendaDisponivelDTO> agenda = agendaService.listarAgenda(idMedico);

        List<HorarioDisponivelDTO> disponibilidades = agenda.stream()
                .flatMap(dto -> dto.getHorariosDisponiveis().stream()
                        .map(horario -> new HorarioDisponivelDTO(dto.getData(), horario)))
                .toList();

        return ResponseEntity.ok(disponibilidades);
    }





}
