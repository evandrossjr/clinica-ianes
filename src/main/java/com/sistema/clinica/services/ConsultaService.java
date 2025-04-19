package com.sistema.clinica.services;

import com.sistema.clinica.models.Consulta;
import com.sistema.clinica.repositories.ConsultaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class ConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;

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

    public Consulta insert(Consulta obj){

        obj.setPagamentoRealizado(false);


        if (!HORARIOS_FIXOS.contains(obj.getHora())){
            throw new IllegalArgumentException("Horário Inválido");

        }

        boolean existe = consultaRepository.existsByMedicoIdAndDataAndHora(
                obj.getMedico().getId(),
                obj.getData(),
                obj.getHora()
        );
        if (existe){
            throw new IllegalArgumentException("Já existe consulta marcada para o médico(a) Dr(a)" + obj.getMedico().getNome() + "neste dia e horário");
        }
        return consultaRepository.save(obj);
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
        entity.setMetodoPagamento(obj.getMetodoPagamento());
        entity.setMedico(obj.getMedico());
        entity.setData(obj.getData());
        entity.setPaciente(obj.getPaciente());
        entity.setHora(obj.getHora());
        entity.setModalidade(obj.getModalidade());
    }




}
