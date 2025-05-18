package com.sistema.clinica.models.dtos.mappers;

import com.sistema.clinica.models.Medico;
import com.sistema.clinica.models.dtos.MedicoDTO;
import com.sistema.clinica.models.dtos.MedicoFormDTO;
import com.sistema.clinica.models.dtos.MedicoResumoDTO;
import com.sistema.clinica.models.enums.DiaDaSemana;

import java.util.Set;
import java.util.stream.Collectors;


public class MedicoMapper {

    // Converte Entidade para DTO
    public static MedicoDTO toDTO(Medico medico) {
        return new MedicoDTO(
                medico.getId(),
                medico.getNome(),
                medico.getCpf(),
                medico.getEmail(),
                medico.getTelefone(),
                medico.getCrm(),
                medico.getEspecialidade(),
                medico.getDiasDisponiveis(),
                medico.getUsername()
        );
    }

    // Converte DTO para Entidade (usado no cadastro)
    public static Medico toEntity(MedicoDTO medicoDTO) {
        Medico medico = new Medico();
        medico.setNome(medicoDTO.nome());
        medico.setCpf(medicoDTO.cpf());
        medico.setEmail(medicoDTO.email());
        medico.setTelefone(medicoDTO.telefone());
        medico.setCrm(medicoDTO.crm());
        medico.setEspecialidade(medicoDTO.especialidade());
        medico.setDiasDisponiveis(medicoDTO.diasDisponiveis());
        medico.setUsername(medicoDTO.username());
        return medico;
    }

    // Converte FormDTO para Entidade (usado no formulário)
    public static Medico formToEntity(MedicoFormDTO formDTO) {
        Medico medico = new Medico();
        medico.setNome(formDTO.getNome());
        medico.setCpf(formDTO.getCpf());
        medico.setEmail(formDTO.getEmail());
        medico.setTelefone(formDTO.getTelefone());
        medico.setCrm(formDTO.getCrm());
        medico.setEspecialidade(formDTO.getEspecialidade());
        medico.setDiasDisponiveis(formDTO.getDiasDisponiveis());
        medico.setUsername(formDTO.getEmail()); // Username padrão é o email
        return medico;
    }

    // Converte Entidade para FormDTO (usado para edição)
    public static MedicoFormDTO toFormDTO(Medico medico) {
        MedicoFormDTO formDTO = new MedicoFormDTO();
        formDTO.setNome(medico.getNome());
        formDTO.setCpf(medico.getCpf());
        formDTO.setEmail(medico.getEmail());
        formDTO.setTelefone(medico.getTelefone());
        formDTO.setCrm(medico.getCrm());
        formDTO.setEspecialidade(medico.getEspecialidade());
        formDTO.setDiasDisponiveis(medico.getDiasDisponiveis());
        return formDTO;
    }

    public static MedicoResumoDTO toResumoDTO(Medico medico) {
        return new MedicoResumoDTO(
                medico.getId(),
                medico.getNome(),
                medico.getEspecialidade(),
                medico.getCrm()
        );
    }
    private static String formatarDiasDisponiveis(Set<DiaDaSemana> dias) {
        return dias.stream()
                .map(DiaDaSemana::getDescricao)
                .collect(Collectors.joining(", "));
    }


}
