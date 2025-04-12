package com.sistema.clinica.security.repositories;

import com.sistema.clinica.models.Paciente;
import com.sistema.clinica.models.Consulta;
import com.sistema.clinica.models.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    List<Consulta> findByMedicoAndData(Medico medico, LocalDate data);

    List<Consulta> findByMedico(Medico medico);

    List<Consulta> findByPaciente(Paciente paciente);

    List<Consulta> findByData(LocalDate hoje);

    List<Consulta> findByDataBetweenOrderByDataAscHoraAsc(LocalDate inicio, LocalDate fim);

    boolean existsByMedicoIdAndDataAndHora(Long medicoId, LocalDate data, LocalTime hora);
}
