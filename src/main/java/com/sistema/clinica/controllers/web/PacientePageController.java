package com.sistema.clinica.controllers.web;

import com.sistema.clinica.models.*;
import com.sistema.clinica.models.dtos.ConsultaDTO;
import com.sistema.clinica.models.dtos.ConsultaForm;
import com.sistema.clinica.models.dtos.PacienteDTO;
import com.sistema.clinica.models.dtos.mappers.PacienteMapper;
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
    public String salvarViaFormulario(@ModelAttribute PacienteDTO dto, RedirectAttributes redirectAttributes) {
        pacienteService.insert(dto);
        redirectAttributes.addFlashAttribute("mensagem", "Paciente \"" + dto.nome() + "\" cadastrado com sucesso!");
        return "redirect:/paciente/cadastro";
    }






    @GetMapping("/minha-area")
    public String abrirPacienteArea(Model model, @AuthenticationPrincipal PessoaDetails pessoaDetails) {
        Paciente paciente = (Paciente) pessoaRepository.findByUsernameIgnoreCase(pessoaDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada"));

        PacienteDTO pacienteDTO = PacienteMapper.toDTO(paciente);

        model.addAttribute("titulo", "Minha Área");
        model.addAttribute("pessoa", paciente);
        model.addAttribute("conteudo", "paciente/minhaArea");

        return "layout";
    }

    @GetMapping("/agendamento-consulta")
    public String abrirCadastroConsulta(Model model, @AuthenticationPrincipal PessoaDetails pessoaDetails) {
        Paciente paciente = (Paciente) pessoaRepository.findByUsernameIgnoreCase(pessoaDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada"));

        model.addAttribute("titulo", "Agendamento de Consultas");
        model.addAttribute("pessoa", PacienteMapper.toDTO(paciente));
        model.addAttribute("conteudo", "paciente/cadastroConsulta");
        model.addAttribute("pacientes", pacienteRepository.findAll().
                stream().
                map(PacienteMapper::toDTO).
                toList());
        model.addAttribute("especialidades", medicoRepository.findEspecialidades());

        return "layout";

    }


    @PostMapping("/agendamento-consulta")
    public String salvarAgendametoConsultas(@ModelAttribute ConsultaForm consultaForm,
                                            RedirectAttributes redirectAttributes,
                                            @AuthenticationPrincipal PessoaDetails pessoaDetails) {

        try {
            // Obter paciente autenticado
            Paciente paciente = (Paciente) pessoaRepository.findByUsernameIgnoreCase(pessoaDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));

            // Obter médico selecionado
            Medico medico = medicoRepository.findById(consultaForm.getMedicoId())
                    .orElseThrow(() -> new RuntimeException("Médico não encontrado"));

            // Converter form para DTO completo
            ConsultaDTO consultaDTO = consultaForm.toDTO(paciente, medico);

            // Chamar o serviço
            consultaService.insert(consultaForm,medico.getId(),paciente.getId());

            redirectAttributes.addFlashAttribute("mensagem",
                    "Consulta agendada com sucesso para " +
                            consultaForm.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

            return "redirect:/paciente/agendamento-consulta";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro",
                    "Erro ao agendar consulta: " + e.getMessage());
            return "redirect:/paciente/agendamento-consulta";
        }
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