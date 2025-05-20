package com.sistema.clinica.controllers.web;


import com.sistema.clinica.models.*;
import com.sistema.clinica.models.dtos.EditarPerfilForm;
import com.sistema.clinica.models.dtos.FuncionarioDTO;
import com.sistema.clinica.models.dtos.mappers.FuncionarioMapper;
import com.sistema.clinica.repositories.FuncionarioRepository;
import com.sistema.clinica.repositories.MedicoRepository;
import com.sistema.clinica.repositories.PessoaRepository;
import com.sistema.clinica.security.PessoaDetails;
import com.sistema.clinica.services.AgendaService;
import com.sistema.clinica.services.FuncionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

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
            funcionarioService.insert(FuncionarioMapper.toDTO(funcionario));
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




    @GetMapping("/editar-funcionario")
    public String editarFuncionario(Model model, @AuthenticationPrincipal PessoaDetails pessoaDetails) {
        Pessoa pessoa = pessoaRepository.findByUsernameIgnoreCase(pessoaDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada"));

        EditarPerfilForm form = new EditarPerfilForm();
        form.setEmail(pessoa.getEmail());


        model.addAttribute("pessoas", funcionarioRepository.findAll());

        model.addAttribute("form", form);
        model.addAttribute("titulo", "Editar Funcionario");
        model.addAttribute("pessoa", pessoa);
        model.addAttribute("conteudo", "admin/editarFuncionario");

        return "layout";
    }


    @GetMapping("/editar-funcionario/selecionar")
    public String selecionarPessoa(@RequestParam(name = "id", required = false) Long id, Model model) {
        Object pessoa = (id != null) ? funcionarioService.findById(id) : new Funcionario();
        model.addAttribute("pessoa", pessoa);
        model.addAttribute("pessoas", funcionarioService.findAll());
        model.addAttribute("titulo", "Editar Funcionário");
        model.addAttribute("conteudo", "admin/editarFuncionario");


        return "layout";
    }

    @PostMapping("/editar-funcionario")
    public String salvarFuncionario(@ModelAttribute FuncionarioDTO pessoa, RedirectAttributes redirectAttributes) {
        // Garantir que a senha não seja alterada
        if (pessoa != null) {
            // Setar a senha antiga novamente para garantir que não será alterada
            FuncionarioDTO funcionarioExistente = funcionarioService.findById(pessoa.id());

            FuncionarioDTO funcionarioAtualizado = new FuncionarioDTO(
                    pessoa.id(),
                    pessoa.nome(),
                    pessoa.username(),
                    funcionarioExistente.password(), // senha antiga mantida aqui
                    pessoa.cpf(),
                    pessoa.email(),
                    pessoa.telefone(),
                    pessoa.setor(),
                    pessoa.matricula()
            );
            funcionarioService.update(funcionarioAtualizado);

            redirectAttributes.addFlashAttribute("mensagem", "Usuário de " + funcionarioExistente.nome() + " atualizado com sucesso!");

            return "redirect:/admin/editar-funcionario/selecionar?id=" + pessoa.id();
        }

        // Caso pessoa seja null, pode retornar algo ou lançar exceção
        throw new IllegalArgumentException("Funcionário inválido");
    }

    @PostMapping("/editar-funcionario/selecionar")
    public String deletarFuncionario(@RequestParam(name = "id") Long id, RedirectAttributes redirectAttributes, @AuthenticationPrincipal PessoaDetails pessoaDetails) {

        Long idUsuarioLogado = pessoaDetails.getPessoa().getId();

        Optional<Pessoa> funcionarioSelecionado = pessoaRepository.findById(id);
        if (funcionarioSelecionado.isPresent()) {

            Pessoa funcionario = funcionarioSelecionado.get();

            if (idUsuarioLogado.equals(funcionario.getId())) {

                redirectAttributes.addFlashAttribute("erro", "Você não pode deletar seu próprio usuário.");
                return "redirect:/admin/editar-funcionario";
            }
            if (funcionario.getRoles().contains("ROLE_ADMIN")) {
                redirectAttributes.addFlashAttribute("erro", "Você não pode deletar um usuário com a role ADMIN.");
                return "redirect:/admin/editar-funcionario";
            }
        }
        funcionarioService.delete(id);
        redirectAttributes.addFlashAttribute("mensagem", "Usuário deletado com sucesso!");
        return "redirect:/admin/dashboard";
    }

}


