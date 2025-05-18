package com.sistema.clinica.services;

import com.sistema.clinica.models.Consulta;
import com.sistema.clinica.models.Medico;
import com.sistema.clinica.models.Paciente;
import com.sistema.clinica.models.dtos.ConsultaDTO;
import com.sistema.clinica.models.dtos.ConsultaForm;
import com.sistema.clinica.models.dtos.mappers.ConsultaMapper;
import com.sistema.clinica.repositories.ConsultaRepository;
import com.sistema.clinica.repositories.MedicoRepository;
import com.sistema.clinica.repositories.PacienteRepository;
import com.sistema.clinica.repositories.PessoaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;
    @Autowired
    private MedicoRepository medicoRepository;
    @Autowired
    private PessoaRepository pessoaRepository;
    @Autowired
    private PacienteRepository pacienteRepository;

    private static final List<LocalTime> HORARIOS_FIXOS = List.of(
            LocalTime.of(9, 0), LocalTime.of(9, 30),
            LocalTime.of(10, 0), LocalTime.of(10, 30),
            LocalTime.of(11, 0), LocalTime.of(11, 30),
            LocalTime.of(14, 0), LocalTime.of(14, 30),
            LocalTime.of(15, 0), LocalTime.of(15, 30)
    );

    public List<Consulta> findAll(){
        return consultaRepository.findAll();
    }

    public Consulta findById(Long id){
        Optional<Consulta> obj = consultaRepository.findById(id);
        return obj.orElseThrow(()-> new ResourceAccessException("Consulta não encontrado"));
    }

    public ConsultaDTO insert(@Valid ConsultaForm form, Long pacienteId, Long medicoId) {
        Objects.requireNonNull(form, "Dados da consulta não podem ser nulos");

        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));

        Medico medico = medicoRepository.findById(medicoId)
                .orElseThrow(() -> new EntityNotFoundException("Médico não encontrado"));

        Consulta consulta = ConsultaMapper.formToEntity(form, paciente, medico);
        consulta.setPagamentoRealizado(false); // Garante que inicie como false

        if (!HORARIOS_FIXOS.contains(consulta.getHora())) {
            throw new IllegalArgumentException("Horário Inválido");
        }

        boolean existe = consultaRepository.existsByMedicoIdAndDataAndHora(
                consulta.getMedico().getId(),
                consulta.getData(),
                consulta.getHora()
        );

        if (existe) {
            throw new IllegalArgumentException("Já existe consulta marcada para este médico no dia e horário selecionados");
        }

        Consulta saved = consultaRepository.save(consulta);
        return ConsultaMapper.toDTO(saved);
    }

    public void delete(Long id){
        consultaRepository.deleteById(id);
    }

    public Consulta update(Long id, Consulta obj){
        try{
            Consulta entity = consultaRepository
                    .findById(id).orElseThrow(() -> new EntityNotFoundException("Consulta não encontrado com matrícula: " + id));
            updateData(entity, obj);
            return consultaRepository.save(entity);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar a consulta: " + e.getMessage(), e);
        }
    }

    private void updateData(Consulta entity, Consulta obj) {
        entity.setMedico(obj.getMedico());
        entity.setData(obj.getData());
        entity.setPaciente(obj.getPaciente());
        entity.setHora(obj.getHora());
        entity.setModalidade(obj.getModalidade());
    }


}
