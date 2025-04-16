package com.sistema.clinica.controllers.page;


import com.sistema.clinica.models.*;
import com.sistema.clinica.models.dtos.EspacoVagoDTO;
import com.sistema.clinica.repositories.FuncionarioRepository;
import com.sistema.clinica.repositories.MedicoRepository;
import com.sistema.clinica.repositories.PessoaRepository;
import com.sistema.clinica.security.PessoaDetails;
import com.sistema.clinica.services.AgendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminPageController {

    private final AgendaService agendaService;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;


    @Autowired
    private PessoaRepository pessoaRepository;

    public AdminPageController(AgendaService agendaService) {
        this.agendaService = agendaService;
    }


    @GetMapping("/dashboard")
    public String abrirCadastroConsulta(Model model, @AuthenticationPrincipal PessoaDetails pessoaDetails) {
        Pessoa pessoa = pessoaRepository.findByUsernameIgnoreCase(pessoaDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada"));

        // Aqui fazemos cast seguro, já que só admin acessam esse endpoint
        Funcionario funcionario = (Funcionario) pessoa;

        model.addAttribute("titulo", "Dashboard");
        model.addAttribute("pessoa", pessoa);
        model.addAttribute("conteudo", "admin/dashboard");


        List<String> descricoesConsultas = agendaService.proximasConsultas().stream()
                .map(c -> {
                    String data = c.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    String hora = c.getHora().format(DateTimeFormatter.ofPattern("HH:mm"));
                    return data + " - " + hora + " - " + c.getPaciente().getNome()
                            + " com Dr(a). " + c.getMedico().getNome()
                            + " (" + c.getMedico().getEspecialidade() + ")";
                }).toList();

        model.addAttribute("descricoesConsultas", descricoesConsultas);



        List<Medico> medicos = medicoRepository.findAll();
        List<Funcionario> funcionarios = funcionarioRepository.findAll();
        model.addAttribute("medicos", medicos);
        model.addAttribute("funcionarios", funcionarios);


        return "layout";
    }




    @GetMapping("/")
    public String mostrarPainel(Model model) {
        System.out.println("nada");
        return "admin/dashboard";
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
        return "admin/dashboard";
    }
}
