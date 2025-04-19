package com.sistema.clinica.controllers.page;

import com.sistema.clinica.models.*;
import com.sistema.clinica.repositories.FuncionarioRepository;
import com.sistema.clinica.repositories.MedicoRepository;
import com.sistema.clinica.repositories.PacienteRepository;
import com.sistema.clinica.repositories.PessoaRepository;
import com.sistema.clinica.security.PessoaDetails;
import com.sistema.clinica.services.*;
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

    @GetMapping("/agendamento-consulta")
    public String abrirCadastroConsulta(Model model, @AuthenticationPrincipal PessoaDetails pessoaDetails) {
        Pessoa pessoa = pessoaRepository.findByUsernameIgnoreCase(pessoaDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada"));

        // Aqui fazemos cast seguro, já que só funcionario acessam esse endpoint
        Funcionario funcionario = (Funcionario) pessoa;

        model.addAttribute("titulo", "Agendamento de Consultas");
        model.addAttribute("pessoa", pessoa);
        model.addAttribute("conteudo", "funcionario/cadastroConsulta");

        model.addAttribute("pacientes", pacienteRepository.findAll());
        model.addAttribute("especialidades", medicoRepository.findEspecialidades());


        return "layout";

    }


    @PostMapping("/agendamento-consulta")
    public String salvarAgendametoConsultas(@ModelAttribute Consulta consulta,
                                            RedirectAttributes redirectAttributes,
                                            Model model) {
        try {
            System.out.println("Dados recebidos: " + consulta);
            Long idMedico = consulta.getMedico().getId();
            Medico medico = medicoRepository.findById(idMedico)
                    .orElseThrow(() -> new RuntimeException("Médico não encontrado"));

            consulta.setMedico(medico);// Log simples
            consultaService.insert(consulta);
            redirectAttributes.addFlashAttribute("mensagem",
                    "Consulta com o médico(a) Dr(a) \"" + consulta.getMedico().getNome() + "\" agendada com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao cadastrar consulta: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("erro",
                    "Erro ao cadastrar consulta: " + e.getMessage());
        }
        return "redirect:/funcionario/agendamento-consulta";
    }


    @GetMapping("/cadastro-medico")
    public String abrirCadastroMedico(Model model, @AuthenticationPrincipal PessoaDetails pessoaDetails) {
        Pessoa pessoa = pessoaRepository.findByUsernameIgnoreCase(pessoaDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada"));

        // Aqui fazemos cast seguro, já que só funcionario acessam esse endpoint
        Funcionario funcionario = (Funcionario) pessoa;

        model.addAttribute("titulo", "Cadastro Médico");
        model.addAttribute("pessoa", pessoa);
        model.addAttribute("conteudo", "funcionario/cadastroMedico");

        model.addAttribute("funcionario", new Funcionario());

        return "layout";

    }



    @PostMapping("/cadastro-medico")
    public String salvarCadastroMedico(@ModelAttribute Medico medico,
                                            RedirectAttributes redirectAttributes,
                                            Model model) {
        try {
            System.out.println("Dados recebidos: " + medico); // Log simples
            medicoService.insert(medico);
            redirectAttributes.addFlashAttribute("mensagem",
                    "Médico Dr(a) \"" + medico.getNome() + "\" cadastrado com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao cadastrar funcionário: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("erro",
                    "Erro ao cadastrar funcionário: " + e.getMessage());
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
    public String salvarCadastroFuncionario(@ModelAttribute Paciente paciente,
                                            RedirectAttributes redirectAttributes,
                                            Model model) {
        try {
            System.out.println("Dados recebidos: " + paciente); // Log simples
            pacienteService.insert(paciente);
            redirectAttributes.addFlashAttribute("mensagem",
                    "Paciente \"" + paciente.getNome() + "\" cadastrado com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao cadastrar paciente: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("erro",
                    "Erro ao cadastrar funcionário: " + e.getMessage());
        }
        return "redirect:/funcionario/cadastro-paciente";
    }

}
