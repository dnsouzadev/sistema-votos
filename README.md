# Sistema de Votação

Sistema de votação desenvolvido em Spring Boot com autenticação JWT, permitindo criação e participação em enquetes com resultados em tempo real.

## 📋 Características

### 🔐 **Sistema de Autenticação e Usuários**
- **Autenticação JWT**: Sistema seguro de login e registro de usuários
- **Sistema de Perfis**: Editar username, email, alterar senha
- **Estatísticas de Usuário**: Total de votações criadas, votos realizados e favoritos

### 🗳️ **Gerenciamento de Votações**
- **Criação de Enquetes**: Usuários podem criar enquetes com múltiplas opções
- **Edição de Votações**: Modificar votações que ainda não receberam votos
- **Exclusão de Votações**: Remover votações próprias
- **Encerramento Manual**: Finalizar votações antes do prazo
- **Sistema de Destaque**: Marcar votações importantes para aparecer no topo
- **Controle de Expiração**: Enquetes com data de expiração automática

### 🎯 **Sistema de Votação**
- **Votação Segura**: Controle de votos únicos por usuário
- **Enquetes Anônimas**: Suporte para votação anônima ou identificada
- **Resultados em Tempo Real**: Visualização com contagem e percentuais
- **Resultados Públicos/Privados**: Controle de visibilidade dos resultados

### 📊 **Funcionalidades Avançadas**
- **Listagem Paginada**: Votações públicas com filtros e paginação
- **Histórico Completo**: 
  - Votações criadas pelo usuário
  - Votações onde o usuário votou
- **Sistema de Favoritos**: Salvar votações de interesse
- **Transparência**: Ver quem votou em votações não anônimas
- **Filtros por Status**: Listar votações ativas ou encerradas

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

### 🔐 **Autenticação**
- `POST /auth/register` - Registro de usuário
- `POST /auth/login` - Login de usuário

### 👤 **Perfil e Usuário**
- `GET /user/profile` - Ver perfil do usuário
- `PUT /user/profile` - Editar perfil (username, email)
- `PUT /user/password` - Alterar senha

### 📚 **Histórico do Usuário**
- `GET /user/polls/created` - Histórico de votações criadas
- `GET /user/polls/voted` - Histórico de votações onde votou
- `GET /user/polls/favorites` - Votações favoritadas

### ⭐ **Sistema de Favoritos**
- `POST /user/polls/{pollId}/favorite` - Adicionar aos favoritos
- `DELETE /user/polls/{pollId}/favorite` - Remover dos favoritos
- `GET /user/polls/{pollId}/favorite/status` - Verificar se é favorito

### 🗳️ **Gerenciamento de Enquetes**
- `POST /polls` - Criar enquete
- `GET /polls/{id}` - Obter enquete por ID
- `PUT /polls/{id}` - Editar enquete (sem votos)
- `DELETE /polls/{id}` - Excluir enquete
- `PUT /polls/{id}/close` - Encerrar enquete manualmente
- `PUT /polls/{id}/featured` - Marcar/desmarcar como destaque

### 📋 **Listagem de Enquetes**
- `GET /polls/user` - Listar enquetes do usuário logado
- `GET /polls/public` - Listar enquetes públicas (com paginação)
- `GET /polls/status/{status}` - Filtrar por status (active/closed)

### 🎯 **Sistema de Votação**
- `POST /polls/{pollId}/vote` - Votar em uma enquete
- `GET /polls/{pollId}/vote/results` - Ver resultados da enquete

### 👥 **Transparência**
- `GET /user/polls/{pollId}/voters` - Ver quem votou (não anônimas)

## 📊 Modelos de Dados

### **User** 
- `id` (UUID), `username`, `email`, `password`, `createdAt`

### **Poll**
- `id` (UUID), `title`, `description`, `anonymous`, `publicResults`
- `featured` (destaque), `active` (ativa/encerrada), `expirationDate`
- `createdAt`, `createdBy` (User)

### **PollOption**
- `id` (UUID), `optionText`, `poll` (Poll)

### **Vote**
- `id` (UUID), `votedBy` (User), `selectedOption` (PollOption)
- `poll` (Poll), `votedAt`

### **UserFavoritePoll**
- `id` (UUID), `user` (User), `poll` (Poll), `favoritedAt`

## 🔒 Segurança

- **Autenticação JWT**: Tokens seguros para acesso à API
- **Criptografia de Senhas**: Usando BCrypt para hash das senhas
- **Proteção de Endpoints**: Spring Security protege rotas sensíveis
- **Validação Rigorosa**: Validação de entrada em todos os DTOs
- **Controle de Propriedade**: Apenas criadores podem editar/excluir votações
- **Prevenção de Edição**: Bloqueio de edição quando há votos registrados
- **Controle de Acesso**: Verificação de permissões para ver resultados/votantes
- **Anonimato Garantido**: Proteção total em votações anônimas

## 🗃️ Banco de Dados

- PostgreSQL como banco principal
- Hibernate para ORM
- Migration automática com `ddl-auto=update`
- Timezone UTC configurado

## 📝 Exemplos de Uso

### 🔐 **Autenticação**
```bash
# Registrar usuário
POST /auth/register
{
  "username": "joao",
  "email": "joao@email.com",
  "password": "senha123"
}

# Login
POST /auth/login
{
  "email": "joao@email.com",
  "password": "senha123"
}
```

### 👤 **Perfil**
```bash
# Ver perfil
GET /user/profile

# Editar perfil
PUT /user/profile
{
  "username": "joao_silva",
  "email": "joao.silva@email.com"
}

# Alterar senha
PUT /user/password
{
  "currentPassword": "senha123",
  "newPassword": "novasenha456",
  "confirmPassword": "novasenha456"
}
```

### 🗳️ **Votações**
```bash
# Criar enquete
POST /polls
{
  "title": "Linguagem favorita",
  "description": "Qual sua linguagem de programação favorita?",
  "anonymous": false,
  "publicResults": true,
  "expirationDate": "2024-12-31T23:59:59",
  "options": ["Java", "Python", "JavaScript", "Go"]
}

# Votar
POST /polls/{pollId}/vote
{
  "optionId": "uuid-da-opcao"
}

# Ver resultados
GET /polls/{pollId}/vote/results

# Editar enquete (sem votos)
PUT /polls/{id}
{
  "title": "Nova pergunta",
  "description": "Descrição atualizada",
  "anonymous": false,
  "publicResults": true,
  "expirationDate": "2025-01-31T23:59:59",
  "options": ["Opção A", "Opção B", "Opção C"]
}
```

### 📋 **Listagens com Paginação**
```bash
# Listar votações públicas
GET /polls/public?page=0&size=10&onlyActive=true

# Histórico de votações criadas
GET /user/polls/created?page=0&size=5

# Histórico de votações onde votou
GET /user/polls/voted?page=0&size=5

# Filtrar por status
GET /polls/status/active?page=0&size=10
GET /polls/status/closed?page=0&size=10
```

### ⭐ **Favoritos**
```bash
# Adicionar aos favoritos
POST /user/polls/{pollId}/favorite

# Remover dos favoritos
DELETE /user/polls/{pollId}/favorite

# Ver favoritos
GET /user/polls/favorites?page=0&size=10
```

### 🎯 **Gerenciamento**
```bash
# Marcar como destaque
PUT /polls/{id}/featured

# Encerrar manualmente
PUT /polls/{id}/close

# Excluir votação
DELETE /polls/{id}

# Ver quem votou (não anônimas)
GET /user/polls/{pollId}/voters
```

## 🚀 Funcionalidades Completas

✅ **Sistema de Autenticação JWT**  
✅ **CRUD Completo de Votações**  
✅ **Sistema de Votação com Validações**  
✅ **Resultados com Contagem e Porcentagem**  
✅ **Listagem Pública com Paginação**  
✅ **Editar Votação (sem votos)**  
✅ **Excluir Votação**  
✅ **Encerrar Votação Manualmente**  
✅ **Sistema de Destaque**  
✅ **Filtros por Status (ativas/encerradas)**  
✅ **Histórico de Votações Criadas**  
✅ **Histórico de Votações Votadas**  
✅ **Sistema de Favoritos**  
✅ **Ver Quem Votou (não anônimas)**  
✅ **Sistema de Perfis Completo**  
✅ **Alteração de Senha**  
✅ **Estatísticas do Usuário**


## 👨‍💻 Autor

Desenvolvido por [dnsouzadev](https://github.com/dnsouzadev)

## 📄 Licença

Este projeto está sob licença MIT.