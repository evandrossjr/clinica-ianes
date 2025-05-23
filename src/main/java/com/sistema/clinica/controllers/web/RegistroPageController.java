package com.sistema.clinica.controllers.web;

import com.sistema.clinica.models.Paciente;
import com.sistema.clinica.models.dtos.PacienteDTO;
import com.sistema.clinica.services.PacienteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    public String registroNovPaciente(@ModelAttribute PacienteDTO paciente,
                                      RedirectAttributes redirectAttributes,
                                      Model model) {


        try {
            System.out.println("Dados recebidos: " + paciente);
            pacienteService.insert(paciente);
            redirectAttributes.addFlashAttribute("mensagem",
                    "Paciente \"" + paciente.nome() + "\" cadastrado com sucesso!");
            model.addAttribute("mensagem", "Paciente \"" + paciente.nome() + "\" cadastrado com sucesso!");
            model.addAttribute("titulo", "Cadastro Paciente");
            model.addAttribute("conteudo", "registro");
            return "redirect:/login";
        } catch (Exception e) {
            System.err.println("Erro ao cadastrar paciente: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("erro",
                    "Erro ao cadastrar o paciente: " + e.getMessage());
            model.addAttribute("titulo", "Cadastro Paciente");
            model.addAttribute("paciente", paciente);
            model.addAttribute("conteudo", "registro");
            model.addAttribute("erro", e.getMessage());
            return "registro";
        }
    }

}
