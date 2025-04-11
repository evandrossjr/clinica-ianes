package com.sistema.clinica.controllers;

import com.sistema.clinica.models.dtos.AgendaDisponivelDTO;
import com.sistema.clinica.services.AgendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
