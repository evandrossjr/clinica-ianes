package com.sistema.clinica.controllers.page;

import com.sistema.clinica.models.Paciente;
import com.sistema.clinica.services.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/paciente")
public class PacientePageController {

    @Autowired
    private PacienteService pacienteService;

    @GetMapping("/cadastro")
    public String mostrarFormularioCadastro(Model model) {

        return "paciente/cadastroPaciente";
    }


    @PostMapping
    public String salvarViaFormulario(@ModelAttribute Paciente paciente, RedirectAttributes redirectAttributes) {
        pacienteService.insert(paciente);
        redirectAttributes.addFlashAttribute("mensagem", "Paciente \"" + paciente.getNome() + "\" cadastrado com sucesso!");
        return "redirect:/paciente/cadastro";
    }
}
