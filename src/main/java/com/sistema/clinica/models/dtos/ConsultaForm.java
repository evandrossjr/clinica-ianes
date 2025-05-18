package com.sistema.clinica.models.dtos;

import com.sistema.clinica.models.Medico;
import com.sistema.clinica.models.Paciente;
import com.sistema.clinica.models.enums.Modalidade;

import java.time.LocalDate;
import java.time.LocalTime;

public class ConsultaForm {
    private Long medicoId;
    private LocalDate data;
    private LocalTime hora;
    private Modalidade modalidade;

    // Getters e Setters
    public Long getMedicoId() {
        return medicoId;
    }

    public void setMedicoId(Long medicoId) {
        this.medicoId = medicoId;
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

    public Modalidade getModalidade() {
        return modalidade;
    }

    public void setModalidade(Modalidade modalidade) {
        this.modalidade = modalidade;
    }

    // Método para converter para ConsultaDTO (agora considerando Paciente e Medico)
    public ConsultaDTO toDTO(Paciente paciente, Medico medico) {
        return new ConsultaDTO(
                null, // ID será gerado ao salvar
                paciente,
                medico,
                this.data,
                this.hora,
                false, // Pagamento inicia como false
                this.modalidade
        );
    }
}