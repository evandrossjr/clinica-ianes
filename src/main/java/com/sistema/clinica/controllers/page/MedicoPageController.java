package com.sistema.clinica.controllers.page;

import com.sistema.clinica.models.Medico;
import com.sistema.clinica.services.MedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/medico")
public class MedicoPageController {

    @Autowired
    private MedicoService medicoService;

    @GetMapping("/cadastro")
    public String mostrarFormularioCadastro(Model model) {

        return "medico/cadastroMedico";
    }


    @PostMapping
    public String salvarViaFormulario(@ModelAttribute Medico medico, RedirectAttributes redirectAttributes) {
        medicoService.insert(medico);
        redirectAttributes.addFlashAttribute("mensagem", "Médico \"" + medico.getNome() + "\" cadastrado com sucesso!");
        return "redirect:/medico/cadastro";
    }
}
