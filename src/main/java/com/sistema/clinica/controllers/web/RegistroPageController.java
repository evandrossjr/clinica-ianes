package com.sistema.clinica.controllers.web;

import com.sistema.clinica.models.Paciente;
import com.sistema.clinica.services.PacienteService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegistroPageController {

    private final PacienteService pacienteService;

    public RegistroPageController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }


    @GetMapping("/registro")
    public String PaginaRegistro(){
        return "registro";
    }

    @PostMapping("/registro")
    public String registroNovPaciente(@ModelAttribute Paciente paciente, RedirectAttributes redirectAttributes) {
        pacienteService.insert(paciente);
        redirectAttributes.addFlashAttribute("mensagem", "Paciente \"" + paciente.getNome() + "\" cadastrado com sucesso!");
        return "redirect:/login";
    }

}
