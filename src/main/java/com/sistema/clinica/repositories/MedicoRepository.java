package com.sistema.clinica.repositories;
import com.sistema.clinica.models.enums.DiaDaSemana;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sistema.clinica.models.Medico;

import java.util.List;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {


    List<Medico> findByDiasDisponiveisContaining(DiaDaSemana dia);
}
