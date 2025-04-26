package com.sistema.clinica.controllers.page;


import com.sistema.clinica.models.*;
import com.sistema.clinica.models.dtos.EditarPerfilForm;
import com.sistema.clinica.models.dtos.EspacoVagoDTO;
import com.sistema.clinica.repositories.FuncionarioRepository;
import com.sistema.clinica.repositories.MedicoRepository;
import com.sistema.clinica.repositories.PessoaRepository;
import com.sistema.clinica.security.PessoaDetails;
import com.sistema.clinica.services.AgendaService;
import com.sistema.clinica.services.FuncionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/admin")
public class AdminPageController {

    private final AgendaService agendaService;

    private final FuncionarioService funcionarioService;



    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;


    @Autowired
    private PessoaRepository pessoaRepository;

    public AdminPageController(AgendaService agendaService, FuncionarioService funcionarioService) {
        this.agendaService = agendaService;
        this.funcionarioService = funcionarioService;
    }


    @GetMapping("/dashboard")
    public String abrirDashboard(Model model, @AuthenticationPrincipal PessoaDetails pessoaDetails) {
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


    @GetMapping("/cadastro-funcionario")
    public String abrirCadastroFuncionario(Model model, @AuthenticationPrincipal PessoaDetails pessoaDetails) {
        Pessoa pessoa = pessoaRepository.findByUsernameIgnoreCase(pessoaDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada"));

        // Aqui fazemos cast seguro, já que só admin acessam esse endpoint
        Funcionario funcionario = (Funcionario) pessoa;

        model.addAttribute("titulo", "Cadastro Funcionário");
        model.addAttribute("pessoa", pessoa);
        model.addAttribute("conteudo", "admin/cadastroFuncionario");

        model.addAttribute("funcionario", new Funcionario());

        return "layout";
    }

    @PostMapping("/cadastro-funcionario")
    public String salvarCadastroFuncionario(@ModelAttribute Funcionario funcionario,
                                            RedirectAttributes redirectAttributes,
                                            Model model) {
        try {
            System.out.println("Dados recebidos: " + funcionario); // Log simples
            funcionarioService.insert(funcionario);
            redirectAttributes.addFlashAttribute("mensagem",
                    "Funcionário \"" + funcionario.getNome() + "\" cadastrado com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao cadastrar funcionário: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("erro",
                    "Erro ao cadastrar funcionário: " + e.getMessage());
        }
        return "redirect:/admin/cadastro-funcionario";
    }


    @GetMapping("/lista-de-funcionario")
    public String listaDeFuncionarios(Model model, @AuthenticationPrincipal PessoaDetails pessoaDetails){
        Pessoa pessoa = pessoaRepository.findByUsernameIgnoreCase(pessoaDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada"));

        // Aqui fazemos cast seguro, já que só admin acessam esse endpoint
        Funcionario funcionario = (Funcionario) pessoa;

        model.addAttribute("titulo", "Lista de Funcionários");
        model.addAttribute("pessoa", pessoa);
        model.addAttribute("conteudo", "admin/listaDeFuncionarios");


        List<Funcionario> funcionarios = funcionarioRepository.findAll();
        model.addAttribute("funcionarios", funcionarios);

        return "layout";
    }

    @GetMapping("/perfil-funcionario")
    public String editarFuncionario(Model model, @AuthenticationPrincipal PessoaDetails pessoaDetails) {
        Pessoa pessoa = pessoaRepository.findByUsernameIgnoreCase(pessoaDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada"));

        EditarPerfilForm form = new EditarPerfilForm();
        form.setEmail(pessoa.getEmail());


        model.addAttribute("pacientes", pessoaRepository.findAll());

        model.addAttribute("form", form);
        model.addAttribute("titulo", "Editar Funcionario");
        model.addAttribute("pessoa", pessoa);
        model.addAttribute("conteudo", "admin/perfilFuncionario");

        return "layout";
    }




}
