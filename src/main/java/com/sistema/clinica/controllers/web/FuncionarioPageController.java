package com.sistema.clinica.controllers.web;

import com.sistema.clinica.models.*;
import com.sistema.clinica.models.dtos.EditarPerfilForm;
import com.sistema.clinica.models.dtos.PacienteDTO;
import com.sistema.clinica.repositories.FuncionarioRepository;
import com.sistema.clinica.repositories.MedicoRepository;
import com.sistema.clinica.repositories.PacienteRepository;
import com.sistema.clinica.repositories.PessoaRepository;
import com.sistema.clinica.security.PessoaDetails;
import com.sistema.clinica.services.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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
        Funcionario funcionario = (Funcionario) pessoaRepository.findByUsernameIgnoreCase(pessoaDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada"));


        model.addAttribute("titulo", "Dashboard");
        model.addAttribute("pessoa", funcionario);
        model.addAttribute("conteudo", "funcionario/dashboard");


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
        List<Paciente> pacientes = pacienteRepository.findAll();
        model.addAttribute("medicos", medicos);
        model.addAttribute("funcionarios", funcionarios);
        model.addAttribute("pacientes", pacientes);


        return "layout";
    }

    @GetMapping("/agendamento-consulta")
    public String abrirCadastroConsulta(Model model, @AuthenticationPrincipal PessoaDetails pessoaDetails) {
        Funcionario funcionario = (Funcionario) pessoaRepository.findByUsernameIgnoreCase(pessoaDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada"));


        model.addAttribute("titulo", "Agendamento de Consultas");
        model.addAttribute("pessoa", funcionario);
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
        Funcionario funcionario = (Funcionario) pessoaRepository.findByUsernameIgnoreCase(pessoaDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada"));


        model.addAttribute("titulo", "Cadastro Médico");
        model.addAttribute("pessoa", funcionario);
        model.addAttribute("conteudo", "funcionario/cadastroMedico");

        model.addAttribute("funcionario", new Funcionario());

        return "layout";

    }



    @PostMapping("/cadastro-medico")
    public String salvarCadastroMedico(@ModelAttribute Medico medico,
                                            RedirectAttributes redirectAttributes,
                                            BindingResult result,
                                            Model model) {


        if (result.hasErrors()) {
            model.addAttribute("titulo", "Cadastro Paciente");
            model.addAttribute("medico", new Medico());
            model.addAttribute("conteudo", "funcionario/cadastroMedico");
            model.addAttribute("erro", "Dados Inválidos");

            return "layout";
        }
        try {
            System.out.println("Dados recebidos: " + medico);
            medicoService.insert(medico);
            redirectAttributes.addFlashAttribute("mensagem",
                    "Médico Dr(a) \"" + medico.getNome() + "\" cadastrado com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao cadastrar funcionário: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("erro",
                    "Erro ao cadastrar médico: " + e.getMessage());
        }
        return "redirect:/funcionario/cadastro-medico";
    }



    @GetMapping("/cadastro-paciente")
    public String abrirCadastroPaciente(Model model, @AuthenticationPrincipal PessoaDetails pessoaDetails) {
        Funcionario funcionario = (Funcionario) pessoaRepository.findByUsernameIgnoreCase(pessoaDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada"));


        model.addAttribute("titulo", "Cadastro Paciente");
        model.addAttribute("pessoa", funcionario);
        model.addAttribute("conteudo", "funcionario/cadastroPaciente");

        model.addAttribute("funcionario", new Funcionario());

        return "layout";

    }

    @PostMapping("/cadastro-paciente")
    public String salvarCadastroPaciente(@ModelAttribute @Valid PacienteDTO paciente,
                                            BindingResult result,
                                            RedirectAttributes redirectAttributes,
                                            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("titulo", "Cadastro Paciente");
            model.addAttribute("paciente", new PacienteDTO(null, "", "", "", "", "", ""));
            model.addAttribute("conteudo", "funcionario/cadastroPaciente");
            model.addAttribute("erro", "Dados Inválidos");

            return "layout";
        }

        try {
            System.out.println("Dados recebidos: " + paciente);
            pacienteService.insert(paciente);
            redirectAttributes.addFlashAttribute("mensagem",
                    "Paciente \"" + paciente.nome() + "\" cadastrado com sucesso!");

        } catch (Exception e) {
            System.err.println("Erro ao cadastrar paciente: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("erro",
                    "Erro ao cadastrar o paciente: " + e.getMessage());
        }
        return "redirect:/funcionario/cadastro-paciente";
    }

    @GetMapping("/editar-medico")
    public String editarMedico(Model model, @AuthenticationPrincipal PessoaDetails pessoaDetails) {
        Funcionario funcionario = (Funcionario) pessoaRepository.findByUsernameIgnoreCase(pessoaDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada"));

        Medico medico = new Medico();

        model.addAttribute("medicos", medicoRepository.findAll());

        model.addAttribute("titulo", "Editar Médico");
        model.addAttribute("medico", medico);
        model.addAttribute("conteudo", "funcionario/editarMedico");

        return "layout";
    }

    @GetMapping("/editar-medico/selecionar")
    public String selecionarMedico(@RequestParam(name = "id", required = false) Long id, Model model) {
        Medico medico = (id != null) ? medicoService.findById(id) : new Medico();
        model.addAttribute("medico", medico);
        model.addAttribute("medicos", medicoService.findAll());
        model.addAttribute("titulo", "Editar Médico");
        model.addAttribute("conteudo", "funcionario/editarMedico");


        return "layout";
    }

    @PostMapping("/editar-medico")
    public String salvarMedico(@ModelAttribute Medico pessoa, RedirectAttributes redirectAttributes) {
        if (pessoa != null) {
            Medico medicoExistente = medicoService.findById(((Medico) pessoa).getId());
            ((Medico) pessoa).setPassword(medicoExistente.getPassword());
            medicoService.update(pessoa.getId(), pessoa);
            redirectAttributes.addFlashAttribute("mensagem", "Perfil de Dr(a) " + medicoExistente.getNome() +" atualizado com sucesso!");
        }
        assert pessoa != null;
        return "redirect:/funcionario/editar-medico/selecionar?id=" + pessoa.getId();
    }

    @GetMapping("/editar-paciente")
    public String editarPaciente(Model model, @AuthenticationPrincipal PessoaDetails pessoaDetails) {
        Funcionario funcionario = (Funcionario) pessoaRepository.findByUsernameIgnoreCase(pessoaDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada"));

        Paciente paciente = new Paciente();

        model.addAttribute("pacientes", pacienteRepository.findAll());

        model.addAttribute("titulo", "Editar Paciente");
        model.addAttribute("paciente", paciente);
        model.addAttribute("conteudo", "funcionario/editarPaciente");

        return "layout";
    }

    @GetMapping("/editar-paciente/selecionar")
    public String selecionarPaciente(@RequestParam(name = "id", required = false) Long id, Model model) {
        Object paciente = (id != null) ? pacienteService.findById(id) : new Paciente();
        model.addAttribute("paciente", paciente);
        model.addAttribute("pacientes", pacienteService.findAll());
        model.addAttribute("titulo", "Editar Paciente");
        model.addAttribute("conteudo", "funcionario/editarPaciente");


        return "layout";
    }

    @PostMapping("/editar-paciente")
    public String salvarPaciente(@ModelAttribute PacienteDTO pessoa, RedirectAttributes redirectAttributes) {
        if (pessoa == null || pessoa.id() == null){
            throw new IllegalArgumentException("Paciente Inválido");
        }

        PacienteDTO pacienteExistente = pacienteService.findById(pessoa.id());
        if (pacienteExistente == null) {
            throw new IllegalArgumentException("Funcionário não encontrado");
        }

        PacienteDTO pacienteAtualizado = new PacienteDTO(
                pessoa.id(),
                pessoa.nome(),
                pessoa.username(),
                pacienteExistente.password(), // Mantém senha original
                pessoa.cpf(),
                pessoa.email(),
                pessoa.telefone()
        );

        pacienteService.update(pessoa.id(), pacienteAtualizado);

        redirectAttributes.addFlashAttribute("mensagem",
                "Usuário de " + pacienteExistente.nome() + " atualizado com sucesso!");

        return "redirect:/funcionario/editar-paciente/selecionar?id=" + pessoa.id();
    }

    @GetMapping("/validar-consultas")
    public String abrirValidarConsulta(Model model, @AuthenticationPrincipal PessoaDetails pessoaDetails) {
        Funcionario funcionario = (Funcionario) pessoaRepository.findByUsernameIgnoreCase(pessoaDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada"));


        model.addAttribute("titulo", "Validar Consultas");
        model.addAttribute("pessoa", funcionario);
        model.addAttribute("conteudo", "funcionario/validarConsultas");
        model.addAttribute("pacientes", pacienteRepository.findAll());
        model.addAttribute("especialidades", medicoRepository.findEspecialidades());

        return "layout";

    }

}
