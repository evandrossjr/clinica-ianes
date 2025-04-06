package com.sistema.clinica.models.enums;

import java.time.DayOfWeek;
import java.util.Arrays;

public enum DiaDaSemana {
    SEGUNDA(DayOfWeek.MONDAY),
    TERCA(DayOfWeek.TUESDAY),
    QUARTA(DayOfWeek.WEDNESDAY),
    QUINTA(DayOfWeek.THURSDAY),
    SEXTA(DayOfWeek.FRIDAY),
    SABADO(DayOfWeek.SATURDAY),
    DOMINGO(DayOfWeek.SUNDAY);

    private DayOfWeek dayOfWeek;


    DiaDaSemana(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public static DiaDaSemana fromDayOfWeek(DayOfWeek dayOfWeek) {
        return Arrays.stream(values())
                .filter(d -> d.dayOfWeek == dayOfWeek)
                .findFirst()
                .orElseThrow();
    }
}
