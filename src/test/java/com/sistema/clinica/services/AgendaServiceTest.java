package com.sistema.clinica.services;


import com.sistema.clinica.models.Consulta;
import com.sistema.clinica.models.Medico;
import com.sistema.clinica.models.dtos.AgendaDisponivelDTO;
import com.sistema.clinica.models.dtos.EspacoVagoDTO;
import com.sistema.clinica.models.enums.DiaDaSemana;
import com.sistema.clinica.repositories.ConsultaRepository;
import com.sistema.clinica.repositories.MedicoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AgendaServiceTest {

    @InjectMocks
    private AgendaService agendaService;

    @Mock
    private ConsultaRepository consultaRepository;

    @Mock
    private MedicoRepository medicoRepository;

    private Medico medico;

    @BeforeEach
    public void setUp() {
        medico = new Medico();
        medico.setId(1L);
        medico.setNome("Dr. Paulo");
        medico.setDiasDisponiveis(Set.of(DiaDaSemana.SEGUNDA, DiaDaSemana.TERCA));
    }

    @Test
    public void deveListarAgenda() {
        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medico));

        List<AgendaDisponivelDTO> agenda = agendaService.listarAgenda(1L);

        assertNotNull(agenda);
        assertFalse(agenda.isEmpty());
    }

    @Test
    public void deveRetornarErroQuandoMedicoNaoEncontrado() {
        when(medicoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> agendaService.listarAgenda(1L));
    }

    @Test
    public void deveRetornarMedicosDoDia() {
        when(medicoRepository.findByDiasDisponiveisContaining(DiaDaSemana.SEGUNDA)).thenReturn(List.of(medico));

        List<Medico> medicos = agendaService.medicosDoDia();

        assertNotNull(medicos);
        assertFalse(medicos.isEmpty());
    }

    @Test
    public void deveRetornarConsultasDoDia() {
        when(consultaRepository.findByData(LocalDate.now())).thenReturn(List.of(new Consulta()));

        List<Consulta> consultas = agendaService.consultasDoDia();

        assertNotNull(consultas);
        assertFalse(consultas.isEmpty());
    }

    @Test
    public void deveRetornarEspacosVagosDoDia() {
        when(consultaRepository.findByMedicoAndData(medico, LocalDate.now())).thenReturn(List.of());

        List<EspacoVagoDTO> espacos = agendaService.espacosVagosDoDia();

        assertNotNull(espacos);
        assertFalse(espacos.isEmpty());
    }
}
