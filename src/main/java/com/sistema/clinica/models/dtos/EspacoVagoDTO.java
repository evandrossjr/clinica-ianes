package com.sistema.clinica.models.dtos;

import java.time.LocalTime;

public class EspacoVagoDTO {
    private String nomeMedico;
    private LocalTime horario;

    public EspacoVagoDTO(String nomeMedico, LocalTime horario) {
        this.nomeMedico = nomeMedico;
        this.horario = horario;
    }

    public String getNomeMedico() {
        return nomeMedico;
    }

    public LocalTime getHorario() {
        return horario;
    }
}