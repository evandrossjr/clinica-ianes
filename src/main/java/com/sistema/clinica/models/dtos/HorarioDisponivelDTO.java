package com.sistema.clinica.models.dtos;

import java.time.LocalDate;
import java.time.LocalTime;

public class HorarioDisponivelDTO {
    private String data; // formato: "2025-04-14"
    private String hora; // formato: "09:00"
    private String dataHoraFormatada; // para exibir direto

    public HorarioDisponivelDTO(LocalDate data, LocalTime hora) {
        this.data = data.toString();
        this.hora = hora.toString();
        this.dataHoraFormatada = this.data + " " + this.hora;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getDataHoraFormatada() {
        return dataHoraFormatada;
    }

    public void setDataHoraFormatada(String dataHoraFormatada) {
        this.dataHoraFormatada = dataHoraFormatada;
    }

    // Getters
}

