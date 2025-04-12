package com.sistema.clinica.controllers.page;

import com.sistema.clinica.models.Funcionario;
import com.sistema.clinica.services.FuncionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/funcionario")
public class FuncionarioPageController {

    @Autowired
    private FuncionarioService funcionarioService;

    @GetMapping("/cadastro")
    public String mostrarFormularioCadastro(Model model) {

        return "funcionario/cadastroFuncionario";
    }


    @PostMapping
    public String salvarViaFormulario(@ModelAttribute Funcionario funcionario, RedirectAttributes redirectAttributes) {
        funcionarioService.insert(funcionario);
        redirectAttributes.addFlashAttribute("mensagem", "Funcionário \"" + funcionario.getNome() + "\" cadastrado com sucesso!");
        return "redirect:/funcionario/cadastro";
    }
}
