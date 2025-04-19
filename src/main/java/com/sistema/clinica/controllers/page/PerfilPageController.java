package com.sistema.clinica.controllers.page;

import com.sistema.clinica.models.Paciente;
import com.sistema.clinica.models.Pessoa;
import com.sistema.clinica.models.dtos.EditarPerfilForm;
import com.sistema.clinica.repositories.PessoaRepository;
import com.sistema.clinica.security.PessoaDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/perfil")
public class PerfilPageController {

    private final PessoaRepository pessoaRepository;

    public PerfilPageController(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    @GetMapping("/editar")
    public String abrirPacienteArea(Model model, @AuthenticationPrincipal PessoaDetails pessoaDetails) {
        Pessoa pessoa = pessoaRepository.findByUsernameIgnoreCase(pessoaDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada"));

        EditarPerfilForm form = new EditarPerfilForm();
        form.setEmail(pessoa.getEmail());

        model.addAttribute("form", form);
        model.addAttribute("titulo", "Editar Perfil");
        model.addAttribute("pessoa", pessoa);
        model.addAttribute("conteudo", "editarPerfil");

        return "layout";
    }

    @PostMapping("/editar")
    public String editarPerfil(@ModelAttribute("form") EditarPerfilForm form,
                               RedirectAttributes redirectAttributes,
                               @AuthenticationPrincipal PessoaDetails pessoaDetails) {

        Pessoa pessoa = pessoaRepository.findByUsernameIgnoreCase(pessoaDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada"));

        // Verifica se a senha atual está correta
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(form.getPassword(), pessoa.getPassword())) {
            redirectAttributes.addFlashAttribute("erro", "Senha atual incorreta.");
            return "redirect:/perfil/editar";
        }

        // Atualiza o email
        pessoa.setEmail(form.getEmail());

        // Atualiza a nova senha, se foi preenchida
        if (form.getNewPassword() != null && !form.getNewPassword().isBlank()) {
            pessoa.setPassword(encoder.encode(form.getNewPassword()));
        }

        pessoaRepository.save(pessoa);
        redirectAttributes.addFlashAttribute("mensagem", "Perfil atualizado com sucesso!");

        return "redirect:/perfil/editar";
    }
}
