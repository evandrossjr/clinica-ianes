# 🏥 Clínica Ianes - Sistema de Agendamento Médico

Sistema web completo para agendamento de consultas em uma clínica, com controle de médicos, pacientes, funcionários, agenda e pagamentos.

<div align="center">
  <img src="https://img.shields.io/badge/Spring%20Boot-2.7-green?style=flat-square&logo=springboot">
  <img src="https://img.shields.io/badge/Thymeleaf-template-yellowgreen?style=flat-square&logo=thymeleaf">
  <img src="https://img.shields.io/badge/H2%20Database-inMemory-blue?style=flat-square&logo=h2">
  <img src="https://img.shields.io/badge/Maven-build-brightgreen?style=flat-square&logo=apachemaven">
</div>

---

## 💡 Funcionalidades

✅ Login com Spring Security  
✅ Cadastro de usuários (com senha criptografada)  
✅ Controle de acesso por perfis: **Admin**, **Funcionário**, **Médico**, **Paciente**  
✅ Agendamento de consultas com disponibilidade inteligente  
✅ Cadastro de médicos, pacientes e funcionários  
✅ Histórico de consultas por paciente e médico  
✅ Registro de pagamento vinculado à modalidade (SUS, Plano, Particular)  
✅ Página inicial com próximas consultas  
[ ] Informação de Validação de Consulta (EM BREVE)  
[ ] Informação de Reconsulta - 15 dias da Validação da Consulta (EM BREVE)  
[ ] Informação de Pagamento - Particular (EM BREVE)  


---

## 🧠 Tecnologias Utilizadas

- **Java 17**
- **Spring Boot**
- **Spring Security**
- **Spring Data JPA**
- **Thymeleaf**
- **H2 Database** (modo dev)
- **Bootstrap 5.3**

---

## 🧪 Como Rodar Localmente

```bash
# Clone o repositório
git clone https://github.com/evandrossjr/clinica-ianes.git
cd clinica-ianes

# Rode o projeto com Maven
./mvnw spring-boot:run
