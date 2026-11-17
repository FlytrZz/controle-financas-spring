# 💰 API de Controle Financeiro

API REST para controle financeiro pessoal, desenvolvida com **Spring Boot**. Permite registrar **receitas** e **despesas** (transações) e organizá-las por **categorias**.

## 📋 Sumário

- [Tecnologias](#-tecnologias)
- [Funcionalidades](#-funcionalidades)
- [Estrutura do projeto](#-estrutura-do-projeto)
- [Pré-requisitos](#-pré-requisitos)
- [Como rodar](#-como-rodar)
- [Endpoints](#-endpoints)
- [Tratamento de erros](#-tratamento-de-erros)
- [Testes](#-testes)
- [Autor](#-autor)

## 🛠 Tecnologias

- **Java 21**
- **Spring Boot 4.1.2**
  - Spring Web MVC
  - Spring Data JPA (Hibernate)
  - Bean Validation
  - Spring Boot DevTools
  - Spring Boot Docker Compose
- **MySQL**
- **Maven** (wrapper incluído)
- **JUnit 5 + Mockito + MockMvc** para testes

## ✨ Funcionalidades

- CRUD completo de **categorias**
- CRUD completo de **transações** (receitas e despesas)
- Cada transação é vinculada a uma categoria
- Validação dos dados de entrada com Bean Validation
- Bloqueio de categorias com nome duplicado (sem diferenciar maiúsculas/minúsculas)
- Tratamento global de exceções com respostas de erro padronizadas
- Testes unitários das camadas de controller e service

## 🗂 Estrutura do projeto

```
src/main/java/com/spring/apiControleFinanceiro
├── DTO/           # Records de entrada (CategoriaDTO, TransacaoDTO)
├── controller/    # Endpoints REST
├── service/       # Regras de negócio
├── repository/    # Interfaces Spring Data JPA
├── model/         # Entidades (Categoria, Transacao) e enum Tipo
└── exception/     # Exceções customizadas e GlobalExceptionHandler
```

### Modelo de dados

**Categoria**

| Campo | Tipo   |
|-------|--------|
| id    | Long   |
| nome  | String |

**Transacao**

| Campo     | Tipo                     |
|-----------|--------------------------|
| id        | Long                     |
| descricao | String                   |
| valor     | BigDecimal               |
| data      | LocalDate                |
| tipo      | Enum (`RECEITA`, `DESPESA`) |
| categoria | Categoria (ManyToOne)    |

## ✅ Pré-requisitos

- [JDK 21](https://adoptium.net/)
- [MySQL](https://dev.mysql.com/downloads/) rodando localmente **ou** [Docker](https://www.docker.com/) para subir o banco via `compose.yaml`
- Git

> Não é preciso instalar o Maven, o projeto já inclui o `mvnw`.

## 🚀 Como rodar

### 1. Clone o repositório

```bash
git clone https://github.com/FlytrZz/controle-financas-spring.git
cd controle-financas-spring
```

### 2. Suba o banco de dados

Usando o Docker Compose:

```bash
docker compose up -d
```

> ⚠️ No `compose.yaml` a porta está declarada como `'3306'`, o que faz o Docker escolher uma porta aleatória no host. Como a aplicação conecta na porta `3306` fixa, altere para `'3306:3306'` antes de subir o container.

Se preferir, use uma instância de MySQL que você já tenha instalada.

### 3. Configure as variáveis de ambiente

A conexão com o banco é lida de variáveis de ambiente (veja `application.properties`):

| Variável      | Descrição                  | Exemplo (compose.yaml) |
|---------------|----------------------------|------------------------|
| `DB_HOST`     | Host do MySQL              | `localhost`            |
| `DB_DATABASE` | Nome do banco              | `mydatabase`           |
| `DB_USER`     | Usuário do banco           | `myuser`               |
| `DB_PASSWORD` | Senha do usuário           | `secret`               |

**Linux / macOS**

```bash
export DB_HOST=localhost
export DB_DATABASE=mydatabase
export DB_USER=myuser
export DB_PASSWORD=secret
```

**Windows (PowerShell)**

```powershell
$env:DB_HOST="localhost"
$env:DB_DATABASE="mydatabase"
$env:DB_USER="myuser"
$env:DB_PASSWORD="secret"
```

> As tabelas são criadas/atualizadas automaticamente (`spring.jpa.hibernate.ddl-auto=update`) e o banco é criado caso não exista (`createDatabaseIfNotExist=true`).

### 4. Execute a aplicação

```bash
./mvnw spring-boot:run
```

No Windows: `mvnw.cmd spring-boot:run`

A API ficará disponível em `http://localhost:8080`.

## 🔌 Endpoints

### Categorias — `/v1/categorias`

| Método | Rota                  | Descrição                  | Status de sucesso |
|--------|-----------------------|----------------------------|-------------------|
| GET    | `/v1/categorias`      | Lista todas as categorias  | `200 OK`          |
| GET    | `/v1/categorias/{id}` | Busca uma categoria por id | `200 OK`          |
| POST   | `/v1/categorias`      | Cria uma categoria         | `201 Created`     |
| PUT    | `/v1/categorias/{id}` | Atualiza uma categoria     | `200 OK`          |
| DELETE | `/v1/categorias/{id}` | Remove uma categoria       | `204 No Content`  |

**Body (POST / PUT)**

```json
{
  "nome": "Mercado"
}
```

### Transações — `/v1/transacoes`

| Método | Rota                  | Descrição                  | Status de sucesso |
|--------|-----------------------|----------------------------|-------------------|
| GET    | `/v1/transacoes`      | Lista todas as transações  | `200 OK`          |
| GET    | `/v1/transacoes/{id}` | Busca uma transação por id | `200 OK`          |
| POST   | `/v1/transacoes`      | Cria uma transação         | `201 Created`     |
| PUT    | `/v1/transacoes/{id}` | Atualiza uma transação     | `200 OK`          |
| DELETE | `/v1/transacoes/{id}` | Remove uma transação       | `204 No Content`  |

**Body (POST / PUT)**

```json
{
  "descricao": "Compras do mês",
  "valor": 250.90,
  "data": "2026-11-10",
  "tipo": "DESPESA",
  "idCategoria": 1
}
```

**Regras de validação**

| Campo         | Regra                                  |
|---------------|----------------------------------------|
| `descricao`   | Opcional                               |
| `valor`       | Obrigatório e maior que zero           |
| `data`        | Obrigatória (formato `yyyy-MM-dd`)     |
| `tipo`        | Obrigatório (`RECEITA` ou `DESPESA`)   |
| `idCategoria` | Obrigatório e deve ser de uma categoria existente |

### Exemplos com cURL

```bash
# Criar uma categoria
curl -X POST http://localhost:8080/v1/categorias \
  -H "Content-Type: application/json" \
  -d '{"nome": "Mercado"}'

# Criar uma transação
curl -X POST http://localhost:8080/v1/transacoes \
  -H "Content-Type: application/json" \
  -d '{"descricao": "Compras do mês", "valor": 250.90, "data": "2026-11-10", "tipo": "DESPESA", "idCategoria": 1}'

# Listar transações
curl http://localhost:8080/v1/transacoes
```

## ⚠️ Tratamento de erros

As exceções de negócio são tratadas por um `@RestControllerAdvice` e retornam o seguinte formato:

```json
{
  "status": 404,
  "mensagem": "Categoria inexistente!",
  "timestamp": "2026-11-17T19:52:00.123456"
}
```

| Status | Quando acontece                                                                 |
|--------|---------------------------------------------------------------------------------|
| `404`  | Categoria ou transação não encontrada                                           |
| `409`  | Categoria com nome já existente, ou tentativa de remover um registro em uso (ex.: categoria vinculada a transações) |

## 🧪 Testes

Para rodar os testes:

```bash
./mvnw test
```

Cobertura atual:

- `CategoriaControllerTest` e `TransacaoControllerTest` (`@WebMvcTest` + MockMvc)
- `CategoriaServiceTest` e `TransacaoServiceTest` (Mockito)

## 👨‍💻 Autor

Feito por **João Henrique** — [@FlytrZz](https://github.com/FlytrZz)
