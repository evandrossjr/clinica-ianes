package com.sistema.clinica.models.dtos;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class AgendaDisponivelDTO {

    private LocalDate data;
    private List<LocalTime> horariosDisponiveis;

    public AgendaDisponivelDTO(LocalDate data, List<LocalTime> horariosDisponiveis) {
        this.data = data;
        this.horariosDisponiveis = horariosDisponiveis;
    }

    public LocalDate getData() {
        return data;
    }

    public List<LocalTime> getHorariosDisponiveis() {
        return horariosDisponiveis;
    }
}
