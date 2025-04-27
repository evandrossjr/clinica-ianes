package com.sistema.clinica.controllers.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // nome do arquivo .html (sem .html) na pasta templates
    }
}
