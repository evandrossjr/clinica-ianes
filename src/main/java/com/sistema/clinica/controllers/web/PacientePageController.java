package com.sistema.clinica.controllers.web;

import com.sistema.clinica.models.*;
import com.sistema.clinica.repositories.MedicoRepository;
import com.sistema.clinica.repositories.PacienteRepository;
import com.sistema.clinica.repositories.PessoaRepository;
import com.sistema.clinica.security.PessoaDetails;
import com.sistema.clinica.services.AgendaService;
import com.sistema.clinica.services.ConsultaService;
import com.sistema.clinica.services.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/paciente")
public class PacientePageController {

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private AgendaService agendaService;
    private final ConsultaService consultaService;

    private final MedicoRepository medicoRepository;
    private final PacienteRepository pacienteRepository;

    public PacientePageController(ConsultaService consultaService, MedicoRepository medicoRepository, PacienteRepository pacienteRepository) {
        this.consultaService = consultaService;
        this.medicoRepository = medicoRepository;
        this.pacienteRepository = pacienteRepository;
    }


    //Metodo sem o thymeleaf
    @GetMapping("/cadastro")
    public String mostrarFormularioCadastro(Model model) {

        return "paciente/cadastroPaciente";
    }

    //Metodo sem o thymeleaf

    @PostMapping
    public String salvarViaFormulario(@ModelAttribute Paciente paciente, RedirectAttributes redirectAttributes) {
        pacienteService.insert(paciente);
        redirectAttributes.addFlashAttribute("mensagem", "Paciente \"" + paciente.getNome() + "\" cadastrado com sucesso!");
        return "redirect:/paciente/cadastro";
    }





    @GetMapping("/minha-area")
    public String abrirPacienteArea(Model model, @AuthenticationPrincipal PessoaDetails pessoaDetails) {
        Pessoa pessoa = pessoaRepository.findByUsernameIgnoreCase(pessoaDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada"));

        // Aqui fazemos cast seguro, já que só pacientes acessam esse endpoint
        Paciente paciente = (Paciente) pessoa;

        model.addAttribute("titulo", "Minha Área");
        model.addAttribute("pessoa", paciente);
        model.addAttribute("conteudo", "paciente/minhaArea");

        return "layout";
    }

    @GetMapping("/agendamento-consulta")
    public String abrirCadastroConsulta(Model model, @AuthenticationPrincipal PessoaDetails pessoaDetails) {
        Pessoa pessoa = pessoaRepository.findByUsernameIgnoreCase(pessoaDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada"));

        // Aqui fazemos cast seguro, já que só pacienete acessam esse endpoint
        Paciente paciente = (Paciente) pessoa;

        model.addAttribute("titulo", "Agendamento de Consultas");
        model.addAttribute("pessoa", pessoa);
        model.addAttribute("conteudo", "paciente/cadastroConsulta");

        model.addAttribute("pacientes", pacienteRepository.findAll());
        model.addAttribute("especialidades", medicoRepository.findEspecialidades());



        return "layout";

    }


    @PostMapping("/agendamento-consulta")
    public String salvarAgendametoConsultas(@ModelAttribute Consulta consulta,
                                            RedirectAttributes redirectAttributes,
                                            Model model,
                                            @AuthenticationPrincipal PessoaDetails pessoaDetails) {
        try {
            System.out.println("Dados recebidos: " + consulta);
            Long idMedico = consulta.getMedico().getId();
            Medico medico = medicoRepository.findById(idMedico)
                    .orElseThrow(() -> new RuntimeException("Médico não encontrado"));



            Pessoa pessoa = pessoaRepository.findByUsernameIgnoreCase(pessoaDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Pessoa não encontrada"));


            Paciente paciente = (Paciente) pessoa; // cast seguro

            consulta.setMedico(medico);
            consulta.setPaciente(paciente);
            consultaService.insert(consulta);
            redirectAttributes.addFlashAttribute("mensagem",
                    "Consulta com o médico(a) Dr(a) \"" + consulta.getMedico().getNome() + "\" agendada com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao cadastrar consulta: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("erro",
                    "Erro ao cadastrar consulta: " + e.getMessage());
        }
        return "redirect:/paciente/agendamento-consulta";
    }

    @GetMapping("/minhas-consultas")
    public String abrirMinhaArea(Model model, @AuthenticationPrincipal PessoaDetails pessoaDetails) {
        String username = pessoaDetails.getUsername();

        Pessoa pessoa = pessoaRepository.findByUsernameIgnoreCase(pessoaDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada"));

        // Aqui fazemos cast seguro, já que só pacienete acessam esse endpoint
        Paciente paciente = (Paciente) pessoa;

        model.addAttribute("titulo", "Dashboard");
        model.addAttribute("pessoa", pessoa);
        model.addAttribute("conteudo", "paciente/minhasConsultas");




        List<String> descricoesConsultas = agendaService.proximasConsultas().stream()
                .filter(c -> c.getPaciente().getId().equals(paciente.getId()))
                .map(c -> {
                    String data = c.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    String hora = c.getHora().format(DateTimeFormatter.ofPattern("HH:mm"));
                    return data + " - " + hora + " - "  + " com Dr(a). " + c.getMedico().getNome()
                            + " (" + c.getMedico().getEspecialidade() + ")";
                }).toList();

        model.addAttribute("descricoesConsultas", descricoesConsultas);


        List<Medico> medicos = medicoRepository.findAll();
        model.addAttribute("medicos", medicos);


        return "layout";
    }
}