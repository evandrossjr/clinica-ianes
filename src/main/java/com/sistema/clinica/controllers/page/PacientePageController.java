package com.sistema.clinica.controllers.page;

import com.sistema.clinica.models.*;
import com.sistema.clinica.repositories.PacienteRepository;
import com.sistema.clinica.repositories.PessoaRepository;
import com.sistema.clinica.security.PessoaDetails;
import com.sistema.clinica.services.AgendaService;
import com.sistema.clinica.services.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/paciente")
public class PacientePageController {

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private AgendaService agendaService;

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





    @GetMapping("/minha-area")
    public String abrirCadastroConsulta(Model model, @AuthenticationPrincipal PessoaDetails pessoaDetails) {
        Pessoa pessoa = pessoaRepository.findByUsernameIgnoreCase(pessoaDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada"));

        // Aqui fazemos cast seguro, já que só pacientes acessam esse endpoint
        Paciente paciente = (Paciente) pessoa;

        model.addAttribute("titulo", "Minha Área");
        model.addAttribute("pessoa", paciente);
        model.addAttribute("conteudo", "paciente/minhaArea");

        return "layout";
    }
}