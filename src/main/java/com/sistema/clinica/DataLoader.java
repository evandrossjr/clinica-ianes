package com.sistema.clinica;

import com.sistema.clinica.models.Cliente;
import com.sistema.clinica.models.Consulta;
import com.sistema.clinica.models.Medico;
import com.sistema.clinica.models.enums.DiaDaSemana;
import com.sistema.clinica.models.enums.MetodoPagamento;
import com.sistema.clinica.repositories.ClienteRepository;
import com.sistema.clinica.repositories.ConsultaRepository;
import com.sistema.clinica.repositories.MedicoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Configuration
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Override
    public void run(String... args) throws Exception {

        Medico medico = new Medico();
        medico.setNome("Fabio Alex");
        medico.setCpf("12345678900");
        medico.setEmail("fabio.alex@clinica.com");
        medico.setTelefone(95008400);
        medico.setCrm(1020);
        medico.setDiasDisponiveis(Set.of(DiaDaSemana.SEGUNDA, DiaDaSemana.QUARTA, DiaDaSemana.SEXTA));
        medico.setEndereco(null); // como você disse que sempre será null

        medicoRepository.save(medico);

        // Criar cliente
        Cliente cliente = new Cliente();
        cliente.setNome("Maria da Silva");
        cliente.setCpf("98765432100");
        cliente.setEmail("maria.silva@email.com");
        cliente.setTelefone(940000000);
        // ... outros campos que você tiver no Cliente

        clienteRepository.save(cliente);

        // Criar consultas de teste
        Consulta c1 = new Consulta();
        c1.setCliente(cliente);
        c1.setMedico(medico);
        c1.setData(LocalDate.now().plusDays(1));
        c1.setHora(LocalTime.of(9, 0));
        c1.setMetodoPagamento(MetodoPagamento.DEBITO);
        c1.setPagamentoRealizado(true);

        Consulta c2 = new Consulta();
        c2.setCliente(cliente);
        c2.setMedico(medico);
        c2.setData(LocalDate.now().plusDays(3));
        c2.setHora(LocalTime.of(10, 30));
        c2.setMetodoPagamento(MetodoPagamento.DINHEIRO);
        c2.setPagamentoRealizado(true);

        Consulta c3 = new Consulta();
        c3.setCliente(cliente);
        c3.setMedico(medico);
        c3.setData(LocalDate.now().plusDays(5));
        c3.setHora(LocalTime.of(15, 0));
        c3.setMetodoPagamento(MetodoPagamento.CREDITO);
        c3.setPagamentoRealizado(false);

        consultaRepository.saveAll(List.of(c1, c2, c3));

        System.out.println("Dados de teste salvos com sucesso!");
    };

    }

