package com.sistema.clinica.controllers.page;

import com.sistema.clinica.models.Consulta;
import com.sistema.clinica.models.Medico;
import com.sistema.clinica.models.Paciente;
import com.sistema.clinica.repositories.MedicoRepository;
import com.sistema.clinica.repositories.PacienteRepository;
import com.sistema.clinica.services.ConsultaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/consulta")
public class ConsultaPageController {

    @Autowired
    private ConsultaService consultaService;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @GetMapping("/cadastro")
    public String mostrarFormularioCadastro(Model model) {
        model.addAttribute("pacientes", pacienteRepository.findAll());
        model.addAttribute("especialidades", medicoRepository.findEspecialidades());

        return "consulta/cadastroConsulta";
    }





    @PostMapping
    public String salvarViaFormulario(@ModelAttribute Consulta consulta, RedirectAttributes redirectAttributes) {
        // Recupera os dados completos do banco
        Medico medico = medicoRepository.findById(consulta.getMedico().getId()).orElse(null);
        Paciente paciente = pacienteRepository.findById(consulta.getPaciente().getId()).orElse(null);

        // Atribui os objetos completos à consulta
        consulta.setMedico(medico);
        consulta.setPaciente(paciente);

        consultaService.insert(consulta);

        String nomeMedico = (medico != null) ? medico.getNome() : "desconhecido";
        redirectAttributes.addFlashAttribute("mensagem", "Consulta com o médico(a) Dr(a) \"" + nomeMedico + "\" agendada com sucesso!");

        return "redirect:/consulta/cadastro";
    }



}
