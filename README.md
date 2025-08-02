# 🏦 HBank API - Spring Boot & Spring Data JPA

Este projeto é uma **API REST bancária** desenvolvida com Spring Boot, Spring Data JPA e banco de dados MySQL. A aplicação simula operações bancárias básicas como transferências entre carteiras, consulta de saldo e geração de extrato. O foco está no uso de boas práticas com JPA, modelagem de relacionamentos, tratamento global de exceções e segurança básica com filtro de IP e interceptação de requisições.

---

## 🔧 Funcionalidades

- Transferência de valores entre carteiras
- Consulta de saldo da carteira
- Emissão de extrato bancário com histórico de transações
- Validação de dados com Hibernate Validator
- Tratamento global de exceções para respostas padronizadas e robustez da API
- Filtro de IP para segurança das requisições
- Interceptor de auditoria para registrar acessos

## 📐 Modelagem e Recursos JPA

Relacionamentos com:
- `@ManyToOne`

Outros recursos utilizados:
- Uso de Enums para representar status e tipos
- Configuração de chaves estrangeiras com `@JoinColumn`
- Projeções com interfaces para otimizar consultas e evitar carregamento desnecessário
- Consultas nativas (`@Query(nativeQuery = true)`) para operações específicas

---

## 🛠️ Tecnologias Utilizadas

- Java 17+
- Spring Boot
- Spring Data JPA
- Hibernate Validator
- Tratamento global de exceções com `@ControllerAdvice` e `@ExceptionHandler` para respostas padronizadas e melhor controle de erros
- MySQL
- Maven  

## ⚙️ Requisitos para rodar o projeto

1. Ter o MySQL instalado e rodando
2. Criar um banco de dados com o nome `h_bank` ou alterar no `application.properties`
3. Alterar o arquivo `src/main/resources/application.properties` para conter as credenciais do seu banco:

```properties
spring.application.name=ecommerce

spring.datasource.url=jdbc:mysql://localhost:3306/h_bank?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=${BANCOKEY}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

spring.sql.init.mode=always
spring.jpa.defer-datasource-initialization=true
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
```

> 🔐 **Atenção**: Substitua `${BANCOKEY}` pela sua senha do banco local, ou configure uma variável de ambiente com esse nome.

## 🚀 Como rodar

Com o Maven instalado:

```bash
./mvnw spring-boot:run
```

Ou rode diretamente pela sua IDE (ex: IntelliJ IDEA).

---

Desenvolvido por Henrique Lindman ✨
