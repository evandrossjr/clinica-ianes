package com.sistema.clinica;

import com.sistema.clinica.models.Funcionario;
import com.sistema.clinica.models.Paciente;
import com.sistema.clinica.models.Consulta;
import com.sistema.clinica.models.Medico;
import com.sistema.clinica.models.enums.DiaDaSemana;
import com.sistema.clinica.models.enums.MetodoPagamento;
import com.sistema.clinica.models.enums.Modalidade;
import com.sistema.clinica.repositories.FuncionarioRepository;
import com.sistema.clinica.repositories.PacienteRepository;
import com.sistema.clinica.repositories.ConsultaRepository;
import com.sistema.clinica.repositories.MedicoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Configuration
public class DataLoader implements CommandLineRunner {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Override
    public void run(String... args) throws Exception {

        Medico medico = new Medico();
        medico.setNome("Fabio Alex");
        medico.setCpf("12345678900");
        medico.setEmail("fabio.alex@clinica.com");
        medico.setTelefone(95008400);
        medico.setCrm(1020);
        medico.setDiasDisponiveis(Set.of(DiaDaSemana.SEGUNDA, DiaDaSemana.QUARTA, DiaDaSemana.SEXTA));
        medico.setEspecialidade("Oftalmologista");

        medicoRepository.save(medico);

        // Criar paciente
        Paciente paciente = new Paciente();
        paciente.setNome("Maria da Silva");
        paciente.setCpf("98765432100");
        paciente.setEmail("maria.silva@email.com");
        paciente.setTelefone(940000000);
        // ... outros campos que você tiver no Paciente

        pacienteRepository.save(paciente);

        // Criar consultas de teste
        Consulta c1 = new Consulta();
        c1.setPaciente(paciente);
        c1.setMedico(medico);
        c1.setData(LocalDate.now().plusDays(1));
        c1.setHora(LocalTime.of(9, 0));
        c1.setMetodoPagamento(MetodoPagamento.DEBITO);
        c1.setPagamentoRealizado(true);
        c1.setModalidade(Modalidade.PARTICULAR);

        Consulta c2 = new Consulta();
        c2.setPaciente(paciente);
        c2.setMedico(medico);
        c2.setData(LocalDate.now().plusDays(3));
        c2.setHora(LocalTime.of(10, 30));
        c2.setMetodoPagamento(MetodoPagamento.DINHEIRO);
        c2.setPagamentoRealizado(true);
        c2.setModalidade(Modalidade.SUS);

        Consulta c3 = new Consulta();
        c3.setPaciente(paciente);
        c3.setMedico(medico);
        c3.setData(LocalDate.now().plusDays(5));
        c3.setHora(LocalTime.of(15, 0));
        c3.setMetodoPagamento(MetodoPagamento.CREDITO);
        c3.setPagamentoRealizado(false);
        c3.setModalidade(Modalidade.PLANO_DE_SAUDE);

        consultaRepository.saveAll(List.of(c1, c2, c3));


        Funcionario f1 = new Funcionario();
        f1.setNome("JOana da Silva");
        f1.setCpf("95785432100");
        f1.setEmail("Joana.silva@email.com");
        f1.setTelefone(970000000);
        f1.setMatricula(1005);
        f1.setSetor("Recepão");

        Funcionario f2 = new Funcionario();
        f2.setNome("Carlos André");
        f2.setCpf("95785485100");
        f2.setEmail("Joana.silva@email.com");
        f2.setTelefone(960000000);
        f2.setMatricula(1006);
        f2.setSetor("Administrativo");

        funcionarioRepository.saveAll(List.of(f1, f2));



        System.out.println("Dados de teste salvos com sucesso!");
    };

    }

