# Sistema de Votação

Sistema de votação desenvolvido em Spring Boot com autenticação JWT, permitindo criação e participação em enquetes com resultados em tempo real.

## 📋 Características

- **Autenticação JWT**: Sistema seguro de login e registro de usuários
- **Criação de Enquetes**: Usuários podem criar enquetes com múltiplas opções
- **Votação**: Sistema de votação com controle de votos únicos por usuário
- **Resultados**: Visualização de resultados com contagem e percentuais
- **Enquetes Anônimas**: Suporte para votação anônima
- **Controle de Expiração**: Enquetes com data de expiração
- **Resultados Públicos/Privados**: Controle de visibilidade dos resultados

## 🛠️ Tecnologias Utilizadas

- **Java 24**
- **Spring Boot 3.5.4**
  - Spring Web
  - Spring Data JPA
  - Spring Security
  - Spring Boot DevTools
  - Spring Validation
- **PostgreSQL** - Banco de dados
- **JWT** (JSON Web Tokens) - Autenticação
- **Lombok** - Redução de código boilerplate
- **Maven** - Gerenciamento de dependências

## 📁 Estrutura do Projeto

```
src/main/java/com/dnsouzadev/sistemavoto/
├── config/              # Configurações de segurança
├── controller/          # Controladores REST
├── dto/                # Data Transfer Objects
│   ├── request/        # DTOs de requisição
│   └── response/       # DTOs de resposta
├── model/              # Entidades JPA
├── repository/         # Repositórios de dados
├── service/            # Lógica de negócio
│   └── impl/          # Implementações dos serviços
└── utils/             # Utilitários (JWT, Auth)
```

## 🚀 Como Executar

### Pré-requisitos

- Java 24+
- PostgreSQL
- Maven

### Configuração do Banco de Dados

1. Crie um banco PostgreSQL:
```sql
CREATE DATABASE mydb;
CREATE USER myuser WITH PASSWORD 'mypassword';
GRANT ALL PRIVILEGES ON DATABASE mydb TO myuser;
```

2. Configure as credenciais em `application.properties` se necessário.

### Execução

```bash
# Clone o repositório
git clone <url-do-repositorio>

# Entre no diretório
cd sistema-voto

# Execute o projeto
./mvnw spring-boot:run
```

A aplicação estará rodando em `http://localhost:8080`

## 📡 API Endpoints

### Autenticação

- `POST /auth/register` - Registro de usuário
- `POST /auth/login` - Login de usuário

### Enquetes

- `POST /polls` - Criar enquete
- `GET /polls/{id}` - Obter enquete por ID
- `GET /polls/user` - Listar enquetes do usuário logado

### Votação

- `POST /polls/{pollId}/vote` - Votar em uma enquete
- `GET /polls/{pollId}/vote/results` - Ver resultados da enquete

## 📊 Modelos de Dados

### User
- ID, username, email, senha

### Poll
- ID, título, descrição, anônimo, resultados públicos, data de expiração, criador

### PollOption
- ID, texto da opção, enquete relacionada

### Vote
- ID, usuário, opção escolhida, data do voto

## 🔒 Segurança

- Autenticação baseada em JWT
- Senhas criptografadas
- Proteção de endpoints com Spring Security
- Validação de entrada de dados

## 🗃️ Banco de Dados

- PostgreSQL como banco principal
- Hibernate para ORM
- Migration automática com `ddl-auto=update`
- Timezone UTC configurado

## 📝 Exemplos de Uso

### Criar uma enquete
```json
POST /polls
{
  "title": "Linguagem favorita",
  "description": "Qual sua linguagem de programação favorita?",
  "anonymous": false,
  "publicResults": true,
  "expirationDate": "2024-12-31T23:59:59",
  "options": ["Java", "Python", "JavaScript", "Go"]
}
```

### Votar
```json
POST /polls/{pollId}/vote
{
  "optionId": "uuid-da-opcao"
}
```

## 👨‍💻 Autor

Desenvolvido por [dnsouzadev](https://github.com/dnsouzadev)

## 📄 Licença

Este projeto está sob licença MIT.