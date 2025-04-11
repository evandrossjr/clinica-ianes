package com.sistema.clinica.controllers;

import com.sistema.clinica.models.Consulta;
import com.sistema.clinica.services.AgendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/teste")
public class TesteController {

    @Autowired
    private AgendaService agendaService;

    @GetMapping("/proximas")
    public List<Consulta> testProximas() {
        return agendaService.proximasConsultas();
    }
}
