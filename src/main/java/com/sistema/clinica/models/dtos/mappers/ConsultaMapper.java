package com.sistema.clinica.models.dtos.mappers;

import com.sistema.clinica.models.Consulta;
import com.sistema.clinica.models.Medico;
import com.sistema.clinica.models.Paciente;
import com.sistema.clinica.models.dtos.ConsultaDTO;
import com.sistema.clinica.models.dtos.ConsultaForm;
import com.sistema.clinica.models.dtos.ConsultaResumoDTO;

import java.time.format.DateTimeFormatter;

public class ConsultaMapper {
    //Entidade -> DTO
    public static ConsultaDTO toDTO(Consulta consulta){
        return new ConsultaDTO(
                consulta.getId(),
                consulta.getPaciente(),
                consulta.getMedico(),
                consulta.getData(),
                consulta.getHora(),
                consulta.isPagamentoRealizado(),
                consulta.getModalidade()
        );
    }

    public static Consulta toEntity(ConsultaDTO dto){
        Consulta consulta = new Consulta();
        consulta.setPaciente(dto.paciente());
        consulta.setMedico(dto.medico());
        consulta.setData(dto.data());
        consulta.setHora(dto.hora());
        consulta.setPagamentoRealizado(dto.pagamentoRealizado());
        consulta.setModalidade(dto.modalidade());
        return consulta;
    }

    public static ConsultaResumoDTO toResumoDTO(Consulta consulta) {
        return new ConsultaResumoDTO(
                consulta.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                consulta.getHora().format(DateTimeFormatter.ofPattern("HH:mm")),
                consulta.getPaciente().getNome(),
                consulta.getMedico().getNome(),
                consulta.getMedico().getEspecialidade(),
                consulta.getModalidade().toString()
        );
    }

    // Form -> DTO
    public static ConsultaDTO formToDto(ConsultaForm form, Paciente paciente, Medico medico) {
        return new ConsultaDTO(
                null, // ID será gerado ao salvar
                paciente,
                medico,
                form.getData(),
                form.getHora(),
                false, // Pagamento inicia como false
                form.getModalidade()
        );
    }

    // Form -> Entidade
    public static Consulta formToEntity(ConsultaForm form,
                                        Paciente paciente,
                                        Medico medico) {
        Consulta consulta = new Consulta();
        consulta.setData(form.getData());
        consulta.setHora(form.getHora());
        consulta.setModalidade(form.getModalidade());
        consulta.setPaciente(paciente);
        consulta.setMedico(medico);
        return consulta;
    }
}
