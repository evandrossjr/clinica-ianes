package com.sistema.clinica.controllers.web;

import com.sistema.clinica.models.*;
import com.sistema.clinica.models.dtos.*;
import com.sistema.clinica.models.dtos.mappers.ConsultaMapper;
import com.sistema.clinica.models.dtos.mappers.FuncionarioMapper;
import com.sistema.clinica.models.dtos.mappers.MedicoMapper;
import com.sistema.clinica.models.dtos.mappers.PacienteMapper;
import com.sistema.clinica.models.enums.DiaDaSemana;
import com.sistema.clinica.repositories.FuncionarioRepository;
import com.sistema.clinica.repositories.MedicoRepository;
import com.sistema.clinica.repositories.PacienteRepository;
import com.sistema.clinica.repositories.PessoaRepository;
import com.sistema.clinica.security.PessoaDetails;
import com.sistema.clinica.services.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.format.DateTimeFormatter;
import java.util.EnumSet;
import java.util.List;


@Controller
@RequestMapping("/funcionario")
public class FuncionarioPageController {

    private final AgendaService agendaService;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;


    @Autowired
    private PessoaRepository pessoaRepository;

    private final PacienteRepository pacienteRepository;

    private final ConsultaService consultaService;
    private final MedicoService medicoService;
    private final PacienteService pacienteService;


    public FuncionarioPageController(AgendaService agendaService, PacienteRepository pacienteRepository, ConsultaService consultaService, MedicoService medicoService, PacienteService pacienteService) {
        this.agendaService = agendaService;
        this.pacienteRepository = pacienteRepository;
        this.consultaService = consultaService;
        this.medicoService = medicoService;
        this.pacienteService = pacienteService;
    }


    @GetMapping("/dashboard")
    public String abrirDashboardFuncionario(Model model, @AuthenticationPrincipal PessoaDetails pessoaDetails) {
        Funcionario funcionario = (Funcionario) pessoaRepository.findByUsernameIgnoreCase(pessoaDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada"));

        model.addAttribute("titulo", "Dashboard");
        model.addAttribute("pessoa", FuncionarioMapper.toDTO(funcionario));
        model.addAttribute("conteudo", "funcionario/dashboard");

        // Usando DTOs para as consultas
        List<ConsultaResumoDTO> consultas = agendaService.proximasConsultas().stream()
                .map(ConsultaMapper::toResumoDTO)
                .toList();
        model.addAttribute("consultas", consultas);

        // Usando DTOs para as listas
        model.addAttribute("medicos", medicoRepository.findAll().stream()
                .map(MedicoMapper::toDTO)
                .toList());
        model.addAttribute("funcionarios", funcionarioRepository.findAll().stream()
                .map(FuncionarioMapper::toDTO)
                .toList());
        model.addAttribute("pacientes", pacienteRepository.findAll().stream()
                .map(PacienteMapper::toDTO)
                .toList());

        return "layout";
    }



    @GetMapping("/cadastro-medico")
    public String abrirCadastroMedico(Model model, @AuthenticationPrincipal PessoaDetails pessoaDetails) {
        model.addAttribute("titulo", "Cadastro Médico");
        model.addAttribute("medicoForm", new MedicoFormDTO());
        model.addAttribute("diasDaSemana", DiaDaSemana.values()); // Para o select de dias
        model.addAttribute("conteudo", "funcionario/cadastroMedico");
        return "layout";
    }

    @PostMapping("/cadastro-medico")
    public String salvarMedico(@Valid @ModelAttribute("medicoForm") MedicoFormDTO medicoFormDTO,
                               BindingResult result,
                               RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "funcionario/cadastroMedico";
        }

        try {
            medicoService.insert(medicoFormDTO);
            redirectAttributes.addFlashAttribute("mensagem",
                    "Médico Dr(a) " + medicoFormDTO.getNome() + " cadastrado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro",
                    "Erro ao cadastrar médico: " + e.getMessage());
        }

        return "redirect:/funcionario/cadastro-medico";
    }

    @GetMapping("/cadastro-paciente")
    public String abrirCadastroPaciente(Model model, @AuthenticationPrincipal PessoaDetails pessoaDetails) {
        Pessoa pessoa = pessoaRepository.findByUsernameIgnoreCase(pessoaDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada"));

        // Aqui fazemos cast seguro, já que só funcionario acessam esse endpoint
        Funcionario funcionario = (Funcionario) pessoa;

        model.addAttribute("titulo", "Cadastro Paciente");
        model.addAttribute("pessoa", pessoa);
        model.addAttribute("conteudo", "funcionario/cadastroPaciente");

        model.addAttribute("funcionario", new Funcionario());

        return "layout";

    }

    @PostMapping("/cadastro-paciente")
    public String salvarCadastroFuncionario(@ModelAttribute PacienteDTO paciente,
                                            RedirectAttributes redirectAttributes,
                                            Model model) {
        try {
            System.out.println("Dados recebidos: " + paciente); // Log simples
            pacienteService.insert(paciente);
            redirectAttributes.addFlashAttribute("mensagem",
                    "Paciente \"" + paciente.nome() + "\" cadastrado com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao cadastrar paciente: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("erro",
                    "Erro ao cadastrar funcionário: " + e.getMessage());
        }
        return "redirect:/funcionario/cadastro-paciente";
    }

    @GetMapping("/editar-medico")
    public String editarMedico(Model model, @AuthenticationPrincipal PessoaDetails pessoaDetails) {
        Pessoa pessoa = pessoaRepository.findByUsernameIgnoreCase(pessoaDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada"));

        Funcionario funcionario = (Funcionario) pessoa;
        Medico medico = new Medico();

        model.addAttribute("medicos", medicoRepository.findAll());

        model.addAttribute("titulo", "Editar Médico");
        model.addAttribute("medico", medico);
        model.addAttribute("conteudo", "funcionario/editarMedico");

        return "layout";
    }

    @GetMapping("/editar-medico/selecionar")
    public String selecionarMedico(
            @RequestParam(name = "id", required = false) Long id,
            Model model) {

        // Carrega médico existente ou novo
        MedicoDTO medicoDTO = (id != null)
                ? medicoService.findById(id)
                : new MedicoDTO(null, "", "", "", "",0 , null, EnumSet.noneOf(DiaDaSemana.class),"");

        // Lista de médicos para seleção
        List<MedicoResumoDTO> medicos = medicoService.listarTodosResumidos();

        model.addAttribute("medicoForm", medicoDTO);
        model.addAttribute("medicos", medicos);
        model.addAttribute("titulo", id != null ? "Editar Médico" : "Cadastrar Médico");
        model.addAttribute("conteudo", "funcionario/editarMedico");

        return "layout";
    }

    @PostMapping("/editar-medico")
    public String salvarMedico(
            @Valid @ModelAttribute("medicoForm") MedicoDTO medicoDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.medicoForm", result);
            redirectAttributes.addFlashAttribute("medicoForm", medicoDTO);
            return "redirect:/funcionario/editar-medico/selecionar?id=" + medicoDTO.id();
        }

        try {
            // 1. Busca o médico existente
            Medico medicoExistente = medicoRepository.findById(medicoDTO.id())
                    .orElseThrow(() -> new EntityNotFoundException("Médico não encontrado"));

            // 2. Atualiza apenas os campos permitidos
            Medico medicoAtualizado = medicoService.update(medicoDTO, medicoExistente);

            redirectAttributes.addFlashAttribute("mensagem",
                    "Perfil de Dr(a) " + medicoAtualizado.getNome() + " atualizado com sucesso!");

            return "redirect:/funcionario/editar-medico/selecionar?id=" + medicoAtualizado.getId();

        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            return "redirect:/funcionario/editar-medico";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao atualizar médico: " + e.getMessage());
            return "redirect:/funcionario/editar-medico/selecionar?id=" + medicoDTO.id();
        }
    }

    @GetMapping("/editar-paciente")
    public String editarPaciente(Model model, @AuthenticationPrincipal PessoaDetails pessoaDetails) {
        Pessoa pessoa = pessoaRepository.findByUsernameIgnoreCase(pessoaDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada"));

        Funcionario funcionario = (Funcionario) pessoa;
        Paciente paciente = new Paciente();

        model.addAttribute("pacientes", pacienteRepository.findAll());

        model.addAttribute("titulo", "Editar Paciente");
        model.addAttribute("paciente", paciente);
        model.addAttribute("conteudo", "funcionario/editarPaciente");

        return "layout";
    }

    @GetMapping("/editar-paciente/selecionar")
    public String selecionarPaciente(@RequestParam(name = "id", required = false) Long id, Model model) {
        PacienteDTO pacienteDTO = (id != null)
                ? pacienteService.findById(id)
                : new PacienteDTO(null, "", "", "", "", "", null);

        model.addAttribute("paciente", pacienteDTO);
        model.addAttribute("pacientes", pacienteService.findAll());
        model.addAttribute("titulo", "Editar Paciente");
        model.addAttribute("conteudo", "funcionario/editarPaciente");

        return "layout";
    }

    @PostMapping("/editar-paciente")
    public String salvarPaciente(@ModelAttribute("paciente") PacienteDTO pacienteDTO,
                                 RedirectAttributes redirectAttributes) {
        try {
            PacienteDTO pacienteAtualizado;

            if (pacienteDTO.id() == null) {
                // Novo paciente
                pacienteAtualizado = pacienteService.insert(pacienteDTO);
            } else {
                // Atualização
                pacienteAtualizado = pacienteService.update(pacienteDTO.id(), pacienteDTO);
            }

            redirectAttributes.addFlashAttribute("mensagem",
                    "Perfil de " + pacienteAtualizado.nome() + " atualizado com sucesso!");
            return "redirect:/funcionario/editar-paciente/selecionar?id=" + pacienteAtualizado.id();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao salvar paciente: " + e.getMessage());
            return "redirect:/funcionario/editar-paciente/selecionar" +
                    (pacienteDTO.id() != null ? "?id=" + pacienteDTO.id() : "");
        }
    }

    @GetMapping("/validar-consultas")
    public String abrirValidarConsulta(Model model, @AuthenticationPrincipal PessoaDetails pessoaDetails) {
        Pessoa pessoa = pessoaRepository.findByUsernameIgnoreCase(pessoaDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada"));

        // Aqui fazemos cast seguro, já que só funcionario acessam esse endpoint
        Funcionario funcionario = (Funcionario) pessoa;

        model.addAttribute("titulo", "Validar Consultas");
        model.addAttribute("pessoa", pessoa);
        model.addAttribute("conteudo", "funcionario/validarConsultas");
        model.addAttribute("pacientes", pacienteRepository.findAll());
        model.addAttribute("especialidades", medicoRepository.findEspecialidades());

        return "layout";

    }

}
