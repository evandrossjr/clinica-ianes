package com.sistema.clinica.controllers.web;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.Set;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Authentication authentication,  HttpServletResponse response) throws IOException {
        Object statusObj = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        if (statusObj != null) {
            int statusCode = Integer.parseInt(statusObj.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value() || statusCode == HttpStatus.FORBIDDEN.value()) {
                if (roles.contains("ROLE_ADMIN")) {
                    response.sendRedirect("/funcionario/dashboard");
                } else if (roles.contains("ROLE_MEDICO")) {
                    response.sendRedirect("/medico/minhas-consultas");
                } else if (roles.contains("ROLE_FUNCIONARIO")) {
                    response.sendRedirect("/funcionario/dashboard");
                } else if (roles.contains("ROLE_PACIENTE")) {
                    response.sendRedirect("/paciente/minhas-consultas");
                } else {
                    response.sendRedirect("/erro");
                }
            }
        }

        return "error"; // fallback para outras situações
    }
}