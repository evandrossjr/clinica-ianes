package com.sistema.clinica.repositories;

import com.sistema.clinica.models.Cliente;
import com.sistema.clinica.models.Consulta;
import com.sistema.clinica.models.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    List<Consulta> findByMedicoAndData(Medico medico, LocalDate data);

    List<Consulta> findByMedico(Medico medico);

    List<Consulta> findByCliente(Cliente cliente);
}
