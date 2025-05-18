package com.sistema.clinica.controllers.web;


import com.sistema.clinica.models.*;
import com.sistema.clinica.models.dtos.EditarPerfilForm;
import com.sistema.clinica.models.dtos.mappers.FuncionarioFormDTO;
import com.sistema.clinica.models.enums.Role;
import com.sistema.clinica.repositories.FuncionarioRepository;
import com.sistema.clinica.repositories.MedicoRepository;
import com.sistema.clinica.repositories.PessoaRepository;
import com.sistema.clinica.security.PessoaDetails;
import com.sistema.clinica.services.AgendaService;
import com.sistema.clinica.services.FuncionarioService;
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
    public String abrirCadastroFuncionario(Model model) {
        model.addAttribute("funcionarioForm", new FuncionarioFormDTO());
        model.addAttribute("titulo", "Cadastro de Funcionário");
        model.addAttribute("conteudo", "admin/cadastroFuncionario");
        return "layout";
    }

    @PostMapping("/cadastro-funcionario")
    public String salvarCadastroFuncionario(@Valid @ModelAttribute("funcionarioForm") FuncionarioFormDTO formDTO,
                                            BindingResult result,
                                            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "admin/funcionarios/cadastro";
        }

        try {
            funcionarioService.insert(formDTO);
            redirectAttributes.addFlashAttribute("mensagem",
                    "Funcionário " + formDTO.getNome() + " cadastrado com sucesso!");
            return "redirect:/admin/funcionarios";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro",
                    "Erro ao cadastrar funcionário: " + e.getMessage());
            return "redirect:/admin/cadastro-funcionario";
        }
    }




    @GetMapping("/editar-funcionario")
    public String editarFuncionario(@PathVariable Long id, Model model) {
        FuncionarioFormDTO formDTO = funcionarioService.buscarParaEdicao(id);
        model.addAttribute("funcionarioForm", formDTO);
        model.addAttribute("titulo", "Editar Funcionario");
        model.addAttribute("conteudo", "admin/editarFuncionario");

        return "layout";
    }


    @GetMapping("/editar-funcionario/selecionar")
    public String selecionarPessoa(@RequestParam(name = "id", required = false) Long id, Model model) {
        Pessoa pessoa = (id != null) ? funcionarioService.findById(id) : new Funcionario();
        model.addAttribute("pessoa", pessoa);
        model.addAttribute("pessoas", funcionarioService.findAll());
        model.addAttribute("titulo", "Editar Funcionário");
        model.addAttribute("conteudo", "admin/editarFuncionario");


        return "layout";
    }

    @PostMapping("/editar-funcionario")
    public String salvarFuncionario(@PathVariable Long id,
                                    @Valid @ModelAttribute("funcionarioForm") FuncionarioFormDTO formDTO,
                                    BindingResult result,
                                    RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "admin/funcionarios/editar";
        }

        try {
            funcionarioService.update(id, formDTO);
            redirectAttributes.addFlashAttribute("mensagem",
                    "Funcionário atualizado com sucesso!");
            return "redirect:/admin/funcionarios";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro",
                    "Erro ao atualizar funcionário: " + e.getMessage());
        return "redirect:/admin/editar-funcionario/selecionar?id=" + id;
    }
        }


    @PostMapping("/editar-funcionario/selecionar")
    public String deletarFuncionario(@PathVariable Long id,
                                     @AuthenticationPrincipal PessoaDetails pessoaDetails,
                                     RedirectAttributes redirectAttributes) {

        try {
            // 1. Verifica se o usuário está tentando se auto-excluir
            if (pessoaDetails.getPessoa().getId().equals(id)) {
                throw new IllegalStateException("Você não pode excluir seu próprio usuário");
            }

            // 2. Busca o funcionário
            Funcionario funcionario = funcionarioRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Funcionário não encontrado"));

            // 3. Verifica se é ADMIN
            if (funcionario.getRoles().contains(Role.ROLE_ADMIN)) {
                throw new UnsupportedOperationException("Não é permitido excluir um usuário ADMIN");
            }

            // 4. Executa a exclusão
            funcionarioRepository.delete(funcionario);

            redirectAttributes.addFlashAttribute("mensagem", "Funcionário excluído com sucesso!");
        } catch (IllegalStateException | UnsupportedOperationException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao excluir funcionário: " + e.getMessage());
        }

        return "redirect:/admin/dashboard";
    }

}


