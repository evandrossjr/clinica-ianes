package com.sistema.clinica.models.enums;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum DiaDaSemana {
    SEGUNDA("Segunda-feira", DayOfWeek.MONDAY, false),
    TERCA("Terça-feira", DayOfWeek.TUESDAY, false),
    QUARTA("Quarta-feira", DayOfWeek.WEDNESDAY, false),
    QUINTA("Quinta-feira", DayOfWeek.THURSDAY, false),
    SEXTA("Sexta-feira", DayOfWeek.FRIDAY, false),
    SABADO("Sábado", DayOfWeek.SATURDAY, true),
    DOMINGO("Domingo", DayOfWeek.SUNDAY, true);

    private final String descricao;
    private final DayOfWeek dayOfWeek;
    private final boolean fimDeSemana;

    DiaDaSemana(String descricao, DayOfWeek dayOfWeek, boolean fimDeSemana) {
        this.descricao = descricao;
        this.dayOfWeek = dayOfWeek;
        this.fimDeSemana = fimDeSemana;
    }

    // Getters
    public String getDescricao() {
        return descricao;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public boolean isFimDeSemana() {
        return fimDeSemana;
    }

    // Conversão de DayOfWeek
    public static DiaDaSemana fromDayOfWeek(DayOfWeek dayOfWeek) {
        return Arrays.stream(values())
                .filter(d -> d.dayOfWeek == dayOfWeek)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Dia da semana inválido"));
    }

    // Obter dia atual
    public static DiaDaSemana getDiaAtual() {
        return fromDayOfWeek(DayOfWeek.from(LocalDate.now()));
    }

    // Verificar se é dia útil
    public boolean isDiaUtil() {
        return !fimDeSemana;
    }

    // Formatar para exibição
    public String getAbreviatura() {
        return descricao.substring(0, 3).toUpperCase();
    }

    // Lista de dias úteis
    public static List<DiaDaSemana> getDiasUteis() {
        return Arrays.stream(values())
                .filter(DiaDaSemana::isDiaUtil)
                .collect(Collectors.toList());
    }
}