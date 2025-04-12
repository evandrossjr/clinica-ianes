package com.sistema.clinica.controllers.page;


import com.sistema.clinica.models.Consulta;
import com.sistema.clinica.models.Funcionario;
import com.sistema.clinica.models.Medico;
import com.sistema.clinica.models.dtos.EspacoVagoDTO;
import com.sistema.clinica.repositories.FuncionarioRepository;
import com.sistema.clinica.repositories.MedicoRepository;
import com.sistema.clinica.services.AgendaService;
import com.sistema.clinica.services.ConsultaService;
import com.sistema.clinica.services.MedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.spring6.processor.SpringValueTagProcessor;

import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {

    private final AgendaService agendaService;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;



    public HomeController(AgendaService agendaService) {
        this.agendaService = agendaService;
    }


    @GetMapping
    public String mostrarPainel(Model model) {
        List<Consulta> proximasConsultas = agendaService.proximasConsultas();
        model.addAttribute("consultas", proximasConsultas);
        List<Medico> medicos = medicoRepository.findAll();
        List<Funcionario> funcionarios = funcionarioRepository.findAll();
        model.addAttribute("medicos", medicos);
        model.addAttribute("funcionarios", funcionarios);

        return "home";
    }

    @GetMapping("/hoje/medicos")
    public ResponseEntity<List<Medico>> medicosDoDia() {
        return ResponseEntity.ok(agendaService.medicosDoDia());
    }

    @GetMapping("/hoje/consultas")
    public ResponseEntity<List<Consulta>> consultasDoDia() {
        return ResponseEntity.ok(agendaService.consultasDoDia());
    }

    @GetMapping("/hoje/espacos-vagos")
    public ResponseEntity<List<EspacoVagoDTO>> espacosVagosDoDia() {
        return ResponseEntity.ok(agendaService.espacosVagosDoDia());
    }

    @GetMapping("/proximas")
    public String verProximasConsultas(Model model) {
        List<Consulta> consultas = agendaService.proximasConsultas();
        model.addAttribute("consultas", consultas);
        return "home"; // nome do template HTML (sem .html)
    }
}
