package com.sistema.clinica.services;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.sistema.clinica.models.dtos.MedicoDTO;
import com.sistema.clinica.models.dtos.MedicoFormDTO;
import com.sistema.clinica.models.dtos.MedicoResumoDTO;
import com.sistema.clinica.models.dtos.mappers.MedicoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import com.sistema.clinica.models.Medico;
import com.sistema.clinica.repositories.MedicoRepository;

import jakarta.persistence.EntityNotFoundException;
@Service
public class MedicoService {

    @Autowired
    private MedicoRepository medicoRepository;

    private final PasswordEncoder passwordEncoder;

    public MedicoService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    public List<MedicoDTO> findAll(){
        return medicoRepository.findAll().stream().map(MedicoMapper::toDTO).collect(Collectors.toList());
    }

    public MedicoDTO findById(Long id){
        Medico medico = medicoRepository.findById(id).orElseThrow(()-> new ResourceAccessException("Medico não encontrado"));
        return MedicoMapper.toDTO(medico);
    }



    public List<MedicoResumoDTO> listarTodosResumidos() {
        return medicoRepository.findAll()
                .stream()
                .map(MedicoMapper::toResumoDTO)
                .collect(Collectors.toList());
    }

    public MedicoDTO insert(MedicoFormDTO formDTO) {
        Medico medico = MedicoMapper.formToEntity(formDTO);
        // Configurações adicionais (senha, roles, etc.)
        Medico medicoSalvo = medicoRepository.save(medico);
        return MedicoMapper.toDTO(medicoSalvo);
    }

    public void delete(Long id){
        medicoRepository.deleteById(id);
    }

    public Medico update(MedicoDTO medicoDTO, Medico medicoExistente) {
        // Atualiza apenas os campos permitidos
        medicoExistente.setNome(medicoDTO.nome());
        medicoExistente.setEmail(medicoDTO.email());
        medicoExistente.setTelefone(medicoDTO.telefone());
        medicoExistente.setCrm(medicoDTO.crm());
        medicoExistente.setEspecialidade(medicoDTO.especialidade());
        medicoExistente.setDiasDisponiveis(medicoDTO.diasDisponiveis());

        // A senha não é atualizada aqui (mantém a existente)

        return medicoRepository.save(medicoExistente);
    }


    public List<Medico> buscarPorEspecialidade(String especialidade) {
        return medicoRepository.findByEspecialidade(especialidade);
    }
}



