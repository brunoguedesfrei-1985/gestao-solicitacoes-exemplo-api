---

# Gestão de Solicitações API

API REST para gerenciamento de solicitações de serviço, desenvolvida em Java com Quarkus.

## O que a aplicação faz?
A Gestão de Solicitações API é uma solução para controle e acompanhamento de solicitações de serviço em empresas. Permite:
- Cadastro e autenticação de usuários (com diferentes cargos)
- Criação, consulta, atualização e gerenciamento de solicitações
- Gerenciamento de endereços vinculados aos usuários
- Segurança via autenticação JWT
- Documentação interativa dos endpoints via Swagger UI

Ideal para empresas que precisam organizar fluxos de atendimento, demandas internas ou serviços recorrentes.

## Funcionalidades
- Cadastro, autenticação e gerenciamento de usuários
- Gerenciamento de cargos e endereços
- Criação, atualização e consulta de solicitações
- Autenticação JWT
- Documentação automática via Swagger/OpenAPI

## Tecnologias Utilizadas
- Java 17+
- Quarkus
- JAX-RS (REST)
- Hibernate ORM (JPA)
- PostgreSQL (ou H2 para testes)
- Swagger UI (OpenAPI)

## Como rodar o projeto

### Pré-requisitos
- Java 17+
- Maven
- PostgreSQL rodando (ou configure H2 para testes)

### Configuração
As variáveis de ambiente para o banco de dados devem ser definidas:
- `QUARKUS_DATASOURCE_JDBC_URL` (ex: jdbc:postgresql://localhost:5432/seubanco)
- `QUARKUS_DATASOURCE_USERNAME`
- `QUARKUS_DATASOURCE_PASSWORD`

O script de inicialização do banco é executado automaticamente a partir de `src/main/resources/initdb/init.sql`.

### Executando em modo dev
```
./mvnw quarkus:dev ou execute script start-dev.sh
```

### Executando testes
```
./mvnw test
```

## Executando com Docker Compose

A aplicação já está pronta para ser executada com Docker Compose, incluindo o banco PostgreSQL. Siga os passos:

### 1. Pré-requisitos
- Docker e Docker Compose instalados

### 2. Suba os containers
No diretório do projeto, execute:
```sh
docker-compose up --build
```
Isso irá:
- Construir a imagem da aplicação
- Subir o banco PostgreSQL já configurado
- Inicializar a aplicação na porta 8090

### 3. Acesse a API e a documentação
- API: http://localhost:8090
- Swagger UI: http://localhost:8090/q/swagger-ui

### 4. Variáveis e persistência
- O banco de dados é persistido em um volume Docker (`postgres_data`).
- As variáveis de ambiente já estão configuradas no `docker-compose.yml`.
- O script de inicialização do banco pode ser customizado em `src/main/resources/initdb/init.sql`.

### 5. Parar e remover containers
```sh
docker-compose down
```

## Documentação da API
- Swagger UI: [http://localhost:8090/q/swagger-ui](http://localhost:8090/q/swagger-ui)
- OpenAPI JSON: [http://localhost:8090/openapi](http://localhost:8090/openapi)

## Autenticação
A maioria dos endpoints exige autenticação JWT. Use o endpoint `/public/login` para obter um token e utilize o botão "Authorize" no Swagger UI para autenticar.

## Estrutura do Projeto
- `src/main/java/com/empresa/model` — Entidades JPA
- `src/main/java/com/empresa/dto` — DTOs
- `src/main/java/com/empresa/rest` — Resources (controllers REST)
- `src/main/java/com/empresa/service` — Regras de negócio
- `src/main/java/com/empresa/dao` — Acesso a dados
- `src/main/resources` — Configurações e scripts SQL

## Observações
- Para ambiente de testes, o banco H2 pode ser ativado via profile `%test`.
- O script de inicialização pode ser customizado em `application.properties`.

---

## Integrações externas
A aplicação realiza integrações automáticas com serviços externos para enriquecer e validar os dados:

- **Consulta de CEP**: Integração com a API ViaCEP (https://viacep.com.br/) para buscar e preencher automaticamente os dados de endereço a partir do CEP informado pelo usuário.
- **Validação de CPF**: Validação de CPF utilizando serviços externos para garantir que o número informado é válido antes de permitir o cadastro de usuários.

Essas integrações tornam o processo de cadastro mais seguro, prático e confiável.

---

Dúvidas ou sugestões? Abra uma issue ou entre em contato!
