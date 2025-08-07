# 📦 Projeto Tech-Challeng FIAP - Fase 1

## Zé Comanda

Desenvolvimento de um backend completo utilizando Spring Boot, com foco na gestão de usuários, donos de restaurante e clientes, incluindo funcionalidades de cadastro, login, atualização de dados e troca de senha.

---

## 🚀 Funcionalidades

- ✅ Criar usuário
- ✅ Listar usuários
- ✅ Atualizar dados do usuário
- ✅ Deletar usuário
- ✅ Trocar senha (com verificação da senha atual)
- ✅ Validação dos dados
- ✅ Segurança com Spring Security e BCrypt

---

## 🛠️ Tecnologias utilizadas

- Java 21
- Spring Boot
- Spring Data JPA
- Spring Security
- PostgreSQL (via Docker)
- Maven
- Docker & Docker Compose
- Swagger

---

## 🔐 Segurança

- Endpoints protegidos com Spring Security
- Senhas armazenadas com `BCryptPasswordEncoder`
- Troca de senha exige verificação da senha atual
- Autenticação baseada em login em senha
- Controle de acesso

---

## 📄 Endpoints principais

### 🆕 Cadastrar usuário

POST /api/v1/auth/register

Body:

<pre>{
 "name": "João Silva",
 "email": "joao.silva@fiap.com",
 "phoneNumber": "+55 11 91234-5678",
 "login": "joaosilva",
 "password": "123",
 "address": {
    "street": "Rua das Flores",
    "neighborhood": "Centro",
    "city": "São Paulo",
    "number": "123",
    "state": "SP",
    "country": "Brasil"
  }
}</pre>

### 🔍 Listar usuários

GET /api/v1/user

### 🔐 Login de usuário

POST /api/v1/auth/login

Body:

<pre>{ 
  "login": "joaosilva",
  "password": "123"
  } </pre>

⚠️ Esse endpoint retorna um token de autenticação JWT. Você deve copiá-lo para utilizar nas próximas requisições protegidas.

### ✏️ Atualizar usuário

PUT  /api/v1/user

Body: 

<pre>{
  "name": "João Silva",
  "email": "joao.silva3@fiap.com",
  "phoneNumber": "+55 11 91234-5678",
  "login": "joao"
}</pre>

### 🔐 Trocar senha

PATCH /api/v1/auth/change-password

Body: 

<pre>{
  "currentPassword": "123",
  "newPassword": "1234",
  "confirmationPassword": "1234"
}</pre>

### ❌ Deletar usuário

DELETE /api/v1/user



⚠️ Observação: A collection do postman está localizada dentro do projeto em collection > ZeComanda User API.postman_collection.json

---

## 🧪 Testando a API

Você pode testar os endpoints usando ferramentas como:

- [Postman](https://www.postman.com/)
- [Insomnia](https://insomnia.rest/)

---

## 🐳 Execução com Docker Compose

### Build e inicialização dos containers

### Passo a passo

- Inicializar o Docker
- Baixar o código-fonte do projeto
- Alterar o nome do arquivo .env-sample para .env
- Preencher as variáveis de ambiente do arquivo .env da seguinte forma:
<pre>
  TECH_CHALLENGE_USER=admin
  TECH_CHALLENGE_PASSWORD=sucesso
  TECH_CHALLENGE_DB_NAME=tech-challenge
</pre>
 - Executar na raiz do projeto o comando:
<pre>
  docker-compose up --build
</pre>

---

## 🧾 Documentação Swagger
Você pode acessar a documentação completa da API através do Swagger:

👉 [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui/index.html#/)

--- 

## ✅ Validações aplicadas
- Campos obrigatórios
- Verificação da senha atual ao trocar senha
- Tratamento de exceções com mensagens específicas
- Verificação de existência de usuário pelo Token
