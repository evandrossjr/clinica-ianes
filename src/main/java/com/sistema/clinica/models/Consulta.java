package com.sistema.clinica.models;

import com.sistema.clinica.models.enums.MetodoPagamento;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate data;
    private LocalTime hora;
    private boolean pagamentoRealizado;

    @Enumerated(EnumType.STRING)
    private MetodoPagamento metodoPagamento;

    public Consulta() {
    }

    public Consulta(Long id, LocalDate data, LocalTime hora, boolean pagamentoRealizado, MetodoPagamento metodoPagamento) {
        this.id = id;
        this.data = data;
        this.hora = hora;
        this.pagamentoRealizado = pagamentoRealizado;
        this.metodoPagamento = metodoPagamento;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public boolean isPagamentoRealizado() {
        return pagamentoRealizado;
    }

    public void setPagamentoRealizado(boolean pagamentoRealizado) {
        this.pagamentoRealizado = pagamentoRealizado;
    }

    public MetodoPagamento getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(MetodoPagamento metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }
}