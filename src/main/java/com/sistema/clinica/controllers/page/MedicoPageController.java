package com.sistema.clinica.controllers.page;

import com.sistema.clinica.models.Medico;
import com.sistema.clinica.models.Paciente;
import com.sistema.clinica.models.Pessoa;
import com.sistema.clinica.repositories.PessoaRepository;
import com.sistema.clinica.security.PessoaDetails;
import com.sistema.clinica.services.MedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private final PessoaRepository pessoaRepository;

    @Autowired
    public MedicoPageController(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }


    @GetMapping("/cadastro")
    public String mostrarFormularioCadastro(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Usuário: " + auth.getName());
        System.out.println("Authorities: " + auth.getAuthorities());

        return "medico/cadastroMedico";
    }


    @PostMapping("/cadastro")
    public String salvarViaFormulario(@ModelAttribute Medico medico, RedirectAttributes redirectAttributes) {
        medicoService.insert(medico);
        redirectAttributes.addFlashAttribute("mensagem", "Médico \"" + medico.getNome() + "\" cadastrado com sucesso!");
        return "redirect:/medico/cadastro";
    }

    @GetMapping("/minha-area")
    public String abrirCadastroConsulta(Model model, @AuthenticationPrincipal PessoaDetails pessoaDetails) {
        Pessoa pessoa = pessoaRepository.findByUsernameIgnoreCase(pessoaDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada"));

        // Aqui fazemos cast seguro, já que só medicos acessam esse endpoint
        Medico medico = (Medico) pessoa;

        model.addAttribute("titulo", "Minha Área");
        model.addAttribute("pessoa", medico);
        model.addAttribute("conteudo", "medico/minhaArea");

        return "layout";
    }
}
