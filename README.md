# 🏥 Procardio API

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.1-brightgreen)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)
![License](https://img.shields.io/badge/License-MIT-yellow)

API RESTful para agendamento de consultas médicas, desenvolvida com Spring Boot, oferecendo autenticação segura via JWT e integração com Google OAuth2.

## 📋 Índice

- [Sobre o Projeto](#sobre-o-projeto)
- [Funcionalidades](#funcionalidades)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Arquitetura](#arquitetura)
- [Pré-requisitos](#pré-requisitos)
- [Instalação](#instalação)
- [Configuração](#configuração)
- [Executando o Projeto](#executando-o-projeto)
- [Documentação da API](#documentação-da-api)
- [Estrutura do Banco de Dados](#estrutura-do-banco-de-dados)
- [Autenticação e Autorização](#autenticação-e-autorização)
- [Endpoints Principais](#endpoints-principais)
- [Exemplos de Uso](#exemplos-de-uso)
- [Tratamento de Erros](#tratamento-de-erros)
- [Contribuindo](#contribuindo)
- [Licença](#licença)

## 🎯 Sobre o Projeto

O **Procardio API** é um sistema completo de gerenciamento de consultas médicas que permite:

- Cadastro e autenticação de usuários (pacientes e administradores)
- Gerenciamento de médicos e especialidades
- Agendamento, edição e cancelamento de consultas
- Verificação de disponibilidade de horários
- Autenticação via Google OAuth2
- Controle de acesso baseado em perfis (RBAC)

## ✨ Funcionalidades

### 👤 Gestão de Usuários
- ✅ Cadastro de novos usuários
- ✅ Login tradicional (email/senha)
- ✅ Login via Google OAuth2
- ✅ Atualização de dados pessoais
- ✅ Gerenciamento de endereços

### 👨‍⚕️ Gestão de Médicos
- ✅ Cadastro de médicos
- ✅ Listagem por especialidade
- ✅ Busca por CRM
- ✅ Gerenciamento de agenda

### 📅 Gestão de Consultas
- ✅ Agendamento de consultas
- ✅ Verificação de disponibilidade
- ✅ Reagendamento
- ✅ Cancelamento
- ✅ Listagem de consultas por paciente
- ✅ Listagem de consultas por médico
- ✅ Prevenção de conflitos de horários

## 🚀 Tecnologias Utilizadas

### Backend
- **Java 17** - Linguagem de programação
- **Spring Boot 4.0.1** - Framework principal
- **Spring Security** - Autenticação e autorização
- **Spring Data JPA** - Persistência de dados
- **JWT (java-jwt 4.4.0)** - Geração e validação de tokens
- **OAuth2 Client** - Integração com Google

### Banco de Dados
- **MySQL 8.0** - Banco de dados relacional

### Documentação
- **SpringDoc OpenAPI 2.5.0** - Documentação Swagger/OpenAPI

### Ferramentas
- **Lombok** - Redução de código boilerplate
- **Maven** - Gerenciamento de dependências
- **Bean Validation** - Validação de dados

## 🏗️ Arquitetura

O projeto segue uma arquitetura em camadas:

```
src/main/java/br/com/procardio/api/procardio_api/
├── config/              # Configurações (Security, Filters)
├── controller/          # Controladores REST
├── dto/                 # Data Transfer Objects
├── enums/               # Enumerações (Perfil, Especialidade)
├── exceptions/          # Exceções personalizadas
├── model/               # Entidades JPA
├── repository/          # Repositórios de dados
└── service/             # Regras de negócio
```

## 📦 Pré-requisitos

Antes de começar, você precisará ter instalado:

- [Java JDK 17+](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [MySQL 8.0+](https://dev.mysql.com/downloads/mysql/)
- [Maven 3.9+](https://maven.apache.org/download.cgi) (ou use o wrapper incluído)
- [Git](https://git-scm.com/)

## 🔧 Instalação

1. **Clone o repositório**
```bash
git clone https://github.com/seu-usuario/procardio-api.git
cd procardio-api
```

2. **Configure o banco de dados MySQL**
```sql
CREATE DATABASE procardio_db;
CREATE USER 'procardio_user'@'localhost' IDENTIFIED BY 'sua_senha';
GRANT ALL PRIVILEGES ON procardio_db.* TO 'procardio_user'@'localhost';
FLUSH PRIVILEGES;
```

3. **Configure as variáveis de ambiente** (opcional)

Crie um arquivo `.env` na raiz do projeto ou configure diretamente no `application.properties`:

```properties
# Banco de Dados
DB_URL=jdbc:mysql://localhost:3306/procardio_db
DB_USERNAME=procardio_user
DB_PASSWORD=sua_senha

# JWT
JWT_SECRET=sua_chave_secreta_muito_segura_aqui

# OAuth2 Google (opcional)
GOOGLE_CLIENT_ID=seu_client_id
GOOGLE_CLIENT_SECRET=seu_client_secret
```

## ⚙️ Configuração

### application.properties

Edite o arquivo `src/main/resources/application.properties`:

```properties
# Configurações da Aplicação
spring.application.name=procardio-api

# Configurações do Banco de Dados
spring.datasource.url=jdbc:mysql://localhost:3306/procardio_db
spring.datasource.username=root
spring.datasource.password=sua_senha
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true

# JWT
api.security.secret=${JWT_SECRET:chave-secreta-padrao-desenvolvimento}

# OAuth2 Google (se configurado)
spring.security.oauth2.client.registration.google.client-id=${GOOGLE_CLIENT_ID}
spring.security.oauth2.client.registration.google.client-secret=${GOOGLE_CLIENT_SECRET}
spring.security.oauth2.client.registration.google.scope=profile,email

# Porta do servidor
server.port=8080
```

## 🎮 Executando o Projeto

### Usando Maven Wrapper (recomendado)

**Linux/Mac:**
```bash
./mvnw spring-boot:run
```

**Windows:**
```bash
mvnw.cmd spring-boot:run
```

### Usando Maven instalado

```bash
mvn spring-boot:run
```

### Compilando e executando o JAR

```bash
mvn clean package
java -jar target/procardio-api-0.0.1-SNAPSHOT.jar
```

A aplicação estará disponível em: `http://localhost:8080`

O Swagger UI abrirá automaticamente no navegador: `http://localhost:8080/swagger-ui/index.html`

## 📚 Documentação da API

Após iniciar a aplicação, acesse a documentação interativa:

- **Swagger UI**: http://localhost:8080/swagger-ui/index.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

## 🗄️ Estrutura do Banco de Dados

### Entidades Principais

#### tb_usuarios
```sql
- id (PK)
- nome
- email (unique)
- senha
- logradouro
- bairro
- cidade
- estado
- cep
- numero
- complemento
```

#### tb_perfis (relacionamento com tb_usuarios)
```sql
- usuario_id (FK)
- perfis (ENUM: ADMIN, USER, PACIENTE)
```

#### tb_medicos
```sql
- id (PK)
- nome
- email (unique)
- crm (unique)
- especialidade (ENUM)
```

#### tb_consultas
```sql
- id (PK)
- paciente_id (FK -> tb_usuarios)
- medico_id (FK -> tb_medicos)
- data_hora
```

### Relacionamentos

- **Usuario** ↔ **Perfis**: One-to-Many (um usuário pode ter múltiplos perfis)
- **Usuario** ↔ **Consultas**: One-to-Many (um paciente pode ter múltiplas consultas)
- **Medico** ↔ **Consultas**: One-to-Many (um médico pode ter múltiplas consultas)

## 🔐 Autenticação e Autorização

### Perfis de Usuário

- **ADMIN**: Acesso total ao sistema
- **PACIENTE**: Acesso para agendar e gerenciar próprias consultas
- **USER**: Perfil básico

### Fluxo de Autenticação

1. **Registro**: `POST /api/auth` - Cria novo usuário
2. **Login**: `POST /api/auth/login` - Retorna JWT token
3. **Google OAuth**: `GET /oauth2/authorization/google` - Login via Google

### Uso do Token JWT

Inclua o token no header de todas as requisições autenticadas:

```
Authorization: Bearer {seu_token_jwt}
```

## 🛣️ Endpoints Principais

### Autenticação

| Método | Endpoint | Descrição | Autenticação |
|--------|----------|-----------|--------------|
| POST | `/api/auth` | Cadastrar novo usuário | Não |
| POST | `/api/auth/login` | Login | Não |
| GET | `/oauth2/authorization/google` | Login via Google | Não |

### Consultas

| Método | Endpoint | Descrição | Perfil |
|--------|----------|-----------|--------|
| POST | `/api/consultas` | Agendar consulta | ADMIN, PACIENTE |
| GET | `/api/consultas/{id}` | Buscar consulta | ADMIN, PACIENTE |
| GET | `/api/consultas/minhas-consultas` | Listar minhas consultas | PACIENTE |
| GET | `/api/consultas/medico/{medicoId}` | Listar por médico | ADMIN |
| GET | `/api/consultas/paciente/{pacienteId}` | Listar por paciente | ADMIN |
| GET | `/api/consultas/disponibilidade/{medicoId}` | Verificar disponibilidade | ADMIN, PACIENTE |
| PUT | `/api/consultas/{id}` | Atualizar consulta | ADMIN, PACIENTE |
| DELETE | `/api/consultas/{id}` | Cancelar consulta | ADMIN, PACIENTE |

### Usuários

| Método | Endpoint | Descrição | Perfil |
|--------|----------|-----------|--------|
| PUT | `/api/usuarios/{id}` | Atualizar usuário | ADMIN, PACIENTE |

### Médicos

| Método | Endpoint | Descrição | Perfil |
|--------|----------|-----------|--------|
| DELETE | `/api/medicos/{id}` | Deletar médico | ADMIN |

## 💡 Exemplos de Uso

### 1. Cadastrar Novo Usuário

```bash
curl -X POST http://localhost:8080/api/auth \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "email": "joao@email.com",
    "senha": "senha123",
    "cep": "12345-678",
    "numero": "100",
    "complemento": "Apto 201",
    "perfis": ["PACIENTE"]
  }'
```

### 2. Fazer Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "joao@email.com",
    "senha": "senha123"
  }'
```

**Resposta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### 3. Agendar Consulta

```bash
curl -X POST http://localhost:8080/api/consultas \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {seu_token}" \
  -d '{
    "medicoId": 1,
    "dataHora": "2026-02-15T14:30:00"
  }'
```

### 4. Verificar Disponibilidade

```bash
curl -X GET "http://localhost:8080/api/consultas/disponibilidade/1?dataInicio=2026-02-15T08:00:00&dataFim=2026-02-15T18:00:00" \
  -H "Authorization: Bearer {seu_token}"
```

### 5. Listar Minhas Consultas

```bash
curl -X GET http://localhost:8080/api/consultas/minhas-consultas \
  -H "Authorization: Bearer {seu_token}"
```

## ⚠️ Tratamento de Erros

A API retorna erros padronizados:

### Conflito de Agendamento (409)
```json
{
  "error": "ConflitoAgendamentoException",
  "message": "Já existe uma consulta agendada para este médico neste horário"
}
```

### Usuário Não Encontrado (404)
```json
{
  "error": "UsuarioNaoEncontradoException",
  "message": "Usuário não encontrado com ID: 5"
}
```

### Não Autorizado (401)
```json
{
  "error": "Unauthorized",
  "message": "Token JWT inválido ou expirado"
}
```

### Acesso Negado (403)
```json
{
  "error": "Forbidden",
  "message": "Acesso negado"
}
```

## 🧪 Testes

Execute os testes:

```bash
./mvnw test
```

## 📝 Boas Práticas Implementadas

- ✅ Arquitetura em camadas (Controller, Service, Repository)
- ✅ DTOs para transferência de dados
- ✅ Validação de entrada com Bean Validation
- ✅ Tratamento global de exceções
- ✅ Segurança com Spring Security e JWT
- ✅ Documentação automática com Swagger
- ✅ Uso de Records para DTOs imutáveis
- ✅ Lombok para redução de boilerplate
- ✅ Separação de concerns

## 🤝 Contribuindo

Contribuições são bem-vindas! Para contribuir:

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/MinhaFeature`)
3. Commit suas mudanças (`git commit -m 'Adiciona MinhaFeature'`)
4. Push para a branch (`git push origin feature/MinhaFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

## 👨‍💻 Autor

Desenvolvido com ❤️ para gestão eficiente de consultas médicas.

## 🙏 Agradecimentos

- Spring Boot Team
- Comunidade Java
- Todos os contribuidores

---

⭐ Se este projeto foi útil, considere dar uma estrela no GitHub!
