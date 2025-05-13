package com.sistema.clinica.models;

import com.sistema.clinica.models.enums.MetodoPagamento;
import com.sistema.clinica.models.enums.Modalidade;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Paciente paciente; //um paciente pode ter varias consultas

    @ManyToOne
    private Medico medico; //um médico pode ter varias consultas

    @NotNull
    private LocalDate data;
    @NotNull
    private LocalTime hora;

    @Enumerated(EnumType.STRING)
    private Modalidade modalidade;

    private boolean pagamentoRealizado;



    public Consulta() {
    }


    public Consulta(Long id, Paciente paciente, Medico medico, LocalDate data, LocalTime hora, boolean pagamentoRealizado,  Modalidade modalidade) {
        this.id = id;
        this.paciente = paciente;
        this.medico = medico;
        this.data = data;
        this.hora = hora;
        this.pagamentoRealizado = pagamentoRealizado;
        this.modalidade = modalidade;
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


    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public Modalidade getModalidade() {
        return modalidade;
    }

    public void setModalidade(Modalidade modalidade) {
        this.modalidade = modalidade;
    }
}