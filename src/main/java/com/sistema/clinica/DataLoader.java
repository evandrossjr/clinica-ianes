package com.sistema.clinica;

import com.sistema.clinica.models.Funcionario;
import com.sistema.clinica.models.Paciente;
import com.sistema.clinica.models.Consulta;
import com.sistema.clinica.models.Medico;
import com.sistema.clinica.models.enums.DiaDaSemana;
import com.sistema.clinica.models.enums.MetodoPagamento;
import com.sistema.clinica.models.enums.Modalidade;
import com.sistema.clinica.models.enums.Role;
import com.sistema.clinica.security.repositories.FuncionarioRepository;
import com.sistema.clinica.security.repositories.PacienteRepository;
import com.sistema.clinica.security.repositories.ConsultaRepository;
import com.sistema.clinica.security.repositories.MedicoRepository;

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
        medico.setTelefone("71991213094");
        medico.setCrm(1020);
        medico.setDiasDisponiveis(Set.of(DiaDaSemana.SEGUNDA, DiaDaSemana.QUARTA, DiaDaSemana.SEXTA));
        medico.setEspecialidade("Oftalmologista");
        medico.setUsername("Fabio");
        medico.setPassword("1234");
        medico.setRoles(Set.of(Role.ROLE_MEDICO));


        medicoRepository.save(medico);

        // Criar paciente
        Paciente paciente = new Paciente();
        paciente.setNome("Maria da Silva");
        paciente.setCpf("98765432100");
        paciente.setEmail("maria.silva@email.com");
        paciente.setTelefone("71991213094");
        paciente.setUsername("Maria");
        paciente.setPassword("1234");
        paciente.setRoles(Set.of(Role.ROLE_PACIENTE));
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
        f1.setTelefone("71991213094");
        f1.setMatricula(1005);
        f1.setSetor("Recepão");
        f1.setPassword("Testando");
        f1.setUsername("Joana");
        f1.setPassword("1234");
        f1.setRoles(Set.of(Role.ROLE_FUNCIONARIO));

        Funcionario f2 = new Funcionario();
        f2.setNome("Carlos André");
        f2.setCpf("95785485100");
        f2.setEmail("Joana.silva@email.com");
        f2.setTelefone("71991213094");
        f2.setMatricula(1006);
        f2.setSetor("Administrativo");
        f2.setPassword("1234");
        f2.setUsername("Carlos");
        f2.setPassword("1234");
        f2.setRoles(Set.of(Role.ROLE_FUNCIONARIO));

        funcionarioRepository.saveAll(List.of(f1, f2));



        Medico m1 = new Medico();
        m1.setNome("Carlos Mendes");
        m1.setCpf("11111111111");
        m1.setEmail("carlos.mendes@clinica.com");
        m1.setTelefone("71991213094");
        m1.setCrm(1001);
        m1.setEspecialidade("Cardiologista");
        m1.setDiasDisponiveis(Set.of(DiaDaSemana.SEGUNDA, DiaDaSemana.TERCA));
        m1.setUsername("Carlos");
        m1.setPassword("1234");
        m1.setRoles(Set.of(Role.ROLE_MEDICO));


        Medico m2 = new Medico();
        m2.setNome("Ana Paula");
        m2.setCpf("22222222222");
        m2.setEmail("ana.paula@clinica.com");
        m2.setTelefone("71991213094");
        m2.setCrm(1002);
        m2.setEspecialidade("Dermatologista");
        m2.setDiasDisponiveis(Set.of(DiaDaSemana.QUARTA, DiaDaSemana.QUINTA));
        m2.setUsername("Ana");
        m2.setPassword("1234");
        m2.setRoles(Set.of(Role.ROLE_MEDICO));

        Medico m3 = new Medico();
        m3.setNome("Roberto Lima");
        m3.setCpf("33333333333");
        m3.setEmail("roberto.lima@clinica.com");
        m3.setTelefone("71991213094");
        m3.setCrm(1003);
        m3.setEspecialidade("Ginecologista");
        m3.setDiasDisponiveis(Set.of(DiaDaSemana.SEXTA));
        m3.setUsername("roberto");
        m3.setPassword("1234");
        m3.setRoles(Set.of(Role.ROLE_MEDICO));

        Medico m4 = new Medico();
        m4.setNome("Juliana Rocha");
        m4.setCpf("44444444444");
        m4.setEmail("juliana.rocha@clinica.com");
        m4.setTelefone("71991213094");
        m4.setCrm(1004);
        m4.setEspecialidade("Neurologista");
        m4.setDiasDisponiveis(Set.of(DiaDaSemana.TERCA, DiaDaSemana.QUINTA));
        m4.setUsername("juliana");
        m4.setPassword("1234");
        m4.setRoles(Set.of(Role.ROLE_MEDICO));

        Medico m5 = new Medico();
        m5.setNome("Fernanda Alves");
        m5.setCpf("55555555555");
        m5.setEmail("fernanda.alves@clinica.com");
        m5.setTelefone("71991213094");
        m5.setCrm(1005);
        m5.setEspecialidade("Pediatra");
        m5.setDiasDisponiveis(Set.of(DiaDaSemana.SEGUNDA, DiaDaSemana.QUARTA));
        m5.setUsername("Fernanda");
        m5.setPassword("1234");
        m5.setRoles(Set.of(Role.ROLE_MEDICO));

        medicoRepository.saveAll(List.of(m1, m2, m3, m4, m5));

        Paciente p1 = new Paciente();
        p1.setNome("João Oliveira");
        p1.setCpf("88888888801");
        p1.setEmail("joao.oliveira@email.com");
        p1.setTelefone("71991213094");
        p1.setUsername("joao");
        p1.setPassword("1234");
        p1.setRoles(Set.of(Role.ROLE_PACIENTE));

        Paciente p2 = new Paciente();
        p2.setNome("Larissa Gomes");
        p2.setCpf("88888888802");
        p2.setEmail("larissa.gomes@email.com");
        p2.setTelefone("71991213094");
        p2.setUsername("Larissa");
        p2.setPassword("1234");
        p2.setRoles(Set.of(Role.ROLE_PACIENTE));

        Paciente p3 = new Paciente();
        p3.setNome("Lucas Martins");
        p3.setCpf("88888888803");
        p3.setEmail("lucas.martins@email.com");
        p3.setTelefone("71991213094");
        p3.setUsername("Lucas");
        p3.setPassword("1234");
        p3.setRoles(Set.of(Role.ROLE_PACIENTE));

        Paciente p4 = new Paciente();
        p4.setNome("Mariana Lopes");
        p4.setCpf("88888888804");
        p4.setEmail("mariana.lopes@email.com");
        p4.setTelefone("71991213094");
        p4.setUsername("mariana");
        p4.setPassword("1234");
        p4.setRoles(Set.of(Role.ROLE_PACIENTE));

        Paciente p5 = new Paciente();
        p5.setNome("Ricardo Silva");
        p5.setCpf("88888888805");
        p5.setEmail("ricardo.silva@email.com");
        p5.setTelefone("71991213094");
        p5.setUsername("Ricardo");
        p5.setPassword("1234");
        p5.setRoles(Set.of(Role.ROLE_PACIENTE));

        pacienteRepository.saveAll(List.of(p1, p2, p3, p4, p5));


        Funcionario func1 = new Funcionario();
        func1.setNome("Pedro Rocha");
        func1.setCpf("77777777701");
        func1.setEmail("pedro.rocha@clinica.com");
        func1.setTelefone("71991213094");
        func1.setMatricula(2001);
        func1.setSetor("Recepção");
        func1.setUsername("pedro");
        func1.setPassword("1234");
        func1.setRoles(Set.of(Role.ROLE_FUNCIONARIO));


        Funcionario func2 = new Funcionario();
        func2.setNome("Beatriz Ramos");
        func2.setCpf("77777777702");
        func2.setEmail("beatriz.ramos@clinica.com");
        func2.setTelefone("71991213094");
        func2.setMatricula(2002);
        func2.setSetor("Financeiro");
        func2.setUsername("beattriz");
        func2.setPassword("1234");
        func2.setRoles(Set.of(Role.ROLE_FUNCIONARIO));

        Funcionario func3 = new Funcionario();
        func3.setNome("Thiago Souza");
        func3.setCpf("77777777703");
        func3.setEmail("thiago.souza@clinica.com");
        func3.setTelefone("71991213094");
        func3.setMatricula(2003);
        func3.setSetor("TI");
        func3.setUsername("thiago");
        func3.setPassword("1234");
        func3.setRoles(Set.of(Role.ROLE_FUNCIONARIO));

        Funcionario func4 = new Funcionario();
        func4.setNome("Amanda Costa");
        func4.setCpf("77777777704");
        func4.setEmail("amanda.costa@clinica.com");
        func4.setTelefone("71991213094");
        func4.setMatricula(2004);
        func4.setSetor("RH");
        func4.setUsername("amanda");
        func4.setPassword("1234");
        func4.setRoles(Set.of(Role.ROLE_FUNCIONARIO));

        Funcionario func5 = new Funcionario();
        func5.setNome("Rodrigo Pires");
        func5.setCpf("77777777705");
        func5.setEmail("rodrigo.pires@clinica.com");
        func5.setTelefone("71991213094");
        func5.setMatricula(2005);
        func5.setSetor("Atendimento");
        func5.setUsername("rodrigo");
        func5.setPassword("1234");
        func5.setRoles(Set.of(Role.ROLE_FUNCIONARIO));


        funcionarioRepository.saveAll(List.of(func1, func2, func3, func4, func5));
        Consulta nova1 = new Consulta();
        nova1.setPaciente(p1);
        nova1.setMedico(m1);
        nova1.setData(LocalDate.now().plusDays(2));
        nova1.setHora(LocalTime.of(9, 0));
        nova1.setMetodoPagamento(MetodoPagamento.DEBITO);
        nova1.setPagamentoRealizado(true);
        nova1.setModalidade(Modalidade.SUS);

        Consulta nova2 = new Consulta();
        nova2.setPaciente(p2);
        nova2.setMedico(m2);
        nova2.setData(LocalDate.now().plusDays(3));
        nova2.setHora(LocalTime.of(10, 30));
        nova2.setMetodoPagamento(MetodoPagamento.DINHEIRO);
        nova2.setPagamentoRealizado(false);
        nova2.setModalidade(Modalidade.PARTICULAR);

        Consulta nova3 = new Consulta();
        nova3.setPaciente(p3);
        nova3.setMedico(m3);
        nova3.setData(LocalDate.now().plusDays(4));
        nova3.setHora(LocalTime.of(14, 0));
        nova3.setMetodoPagamento(MetodoPagamento.CREDITO);
        nova3.setPagamentoRealizado(true);
        nova3.setModalidade(Modalidade.PLANO_DE_SAUDE);

        Consulta nova4 = new Consulta();
        nova4.setPaciente(p4);
        nova4.setMedico(m4);
        nova4.setData(LocalDate.now().plusDays(5));
        nova4.setHora(LocalTime.of(15, 30));
        nova4.setMetodoPagamento(MetodoPagamento.DEBITO);
        nova4.setPagamentoRealizado(true);
        nova4.setModalidade(Modalidade.SUS);

        Consulta nova5 = new Consulta();
        nova5.setPaciente(p5);
        nova5.setMedico(m5);
        nova5.setData(LocalDate.now().plusDays(6));
        nova5.setHora(LocalTime.of(9, 30));
        nova5.setMetodoPagamento(MetodoPagamento.DINHEIRO);
        nova5.setPagamentoRealizado(false);
        nova5.setModalidade(Modalidade.PARTICULAR);

        consultaRepository.saveAll(List.of(nova1, nova2, nova3, nova4, nova5));


        System.out.println("Dados de teste salvos com sucesso!");
    };

    }

