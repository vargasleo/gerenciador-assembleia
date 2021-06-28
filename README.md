# Gerenciador de Assembleia

API REST para gerenciar sessões de votação em assembleias.

Um associado válido (que exista no cadastro) pode:

- Cadastrar uma nova pauta
- Abrir uma sessão de votação em uma pauta (o prazo deve ser informado, default = 1 min)
- Receber votos dos associados em pautas (os votos são apenas sim/não)
- Contabilizar os votos e dar o resultado da votação na pauta (o resultado pode ser sim/não/empate)

## Aplicação em nuvem

A aplicação está rodando em nuvem através do Heroku: [**Clique aqui**](https://gerenciador-assembleia.herokuapp.com/)

## Documentação dos endpoints

Documentação gerada automaticamente pelo Swagger: [**Clique aqui**](https://gerenciador-assembleia.herokuapp.com/swagger.html)

# Tecnologias

- Java
- Maven
- Git
- Heroku
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Cloud Stream
- Spring Cloud OpenFeign
- Springdoc OpenAPI (Swagger)
- RabbitMQ (CloudAMQP)
- PostgreSQL (Heroku PostgreSQL)
- Lombok
- Hibernate
- JUnit 4

## Pré-requisitos

- Java/JDK 16 - [Download](https://www.oracle.com/java/technologies/javase-jdk16-downloads.html)
- PostgreSQL 13.3 - [Download](https://www.enterprisedb.com/downloads/postgres-postgresql-downloads)
- Erlang 24 - [Download](https://www.erlang.org/downloads)
- RabbitMQ 3.8 - [Download](https://www.rabbitmq.com/download.html)

## Configurando banco de dados

Antes de rodar o projeto, é necessário criar o banco de dados. 

Ao instalar, mantenha a porta padrão, ou ajuste no `application.properties`

No terminal, rode:

```
psql -U postgres -h localhost
create user postgres with password 'admin';
```

O usuário e a senha devem ser condizentes com os Path Variables da variável `spring.datasource.url` no arquivo `application.properties` 

## Local

### Rodando testes unitários e testes de integração:

```
mvn test
```

### Rodando a aplicação:

```
mvn package
mvn spring-boot:run
```

## Notas

*A API consome um endpoint **externo à aplicação**, [user-info.herokuapp.com/users](http://user-info.herokuapp.com/users)/{cpf} e esta API **não** é mantida pelo autor.*
