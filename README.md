# Java HTTP Server (Pure Java)

Este projeto é um servidor HTTP criado **100% em Java puro**, usando o módulo `jdk.httpserver` da JDK, sem Spring Boot e sem frameworks externos.

O objetivo é entender como um servidor web funciona por baixo, antes de migrar para soluções mais completas como Spring Boot, microsserviços e arquiteturas distribuídas.

---

## 🚀 Funcionalidades atuais

- Servidor HTTP rodando na porta **8080**
- Rota `GET /hello` respondendo um JSON simples
- Rota `GET /products` retornando uma lista de produtos em memória
- Rota `GET /products/{id}` retornando um produto específico
- Rota `POST /products` para criar novos produtos
- Manipulação manual de:
    - Headers
    - Status code
    - Corpo da resposta (body)
- Estrutura de código organizada em:
    - `Main` (server)
    - `handlers` (rotas)
    - `models` (entidades)

---

## 🧠 Por que esse projeto existe?

Antes de avançar para frameworks como Spring Boot, é essencial entender:

- Como criar um servidor HTTP “na mão”
- Como uma requisição chega até o código (handler)
- Como uma resposta HTTP é construída (status, headers, body)
- Como organizar o código em pacotes, handlers e modelos

Esse projeto faz parte da minha jornada para me tornar um **Backend Java** sólido, preparado para trabalhar com:

- Java 17+
- Spring Boot
- Microsserviços
- Kafka
- Arquitetura distribuída

---

## 📂 Estrutura do projeto

A estrutura atual do projeto é:

```bash
src/
  server/
    Main.java
    handlers/
      HelloHandler.java
      ProductHandler.java
    models/
      Product.java
out/
  (arquivos compilados gerados pelo javac)
```

---

## ▶️ Como rodar o projeto

### 1. Compilar os arquivos Java

No diretório raiz do projeto:

```bash
javac -cp libs/gson-2.10.1.jar -d out $(find src -name "*.java")
```

Esse comando:

- Compila todos os arquivos `.java` dentro de `src/`
- Gera os `.class` dentro da pasta `out/`

> Em Windows (sem `find`), você pode compilar manualmente, por exemplo:
>
> ```bash
> javac -cp libs/gson-2.10.1.jar -d out src/server/Main.java src/server/handlers/*.java src/server/models/*.java
> ```

---

### 2. Executar o servidor

Ainda no diretório raiz:

```bash
java -cp "out:libs/gson-2.10.1.jar" server.Main
```

Em Windows:

```bash
java -cp "out;libs/gson-2.10.1.jar" server.Main
```

Se tudo estiver correto, você verá no console:

```text
Servidor rodando na porta 8080
```

---

## 🧪 Testando as rotas

### 🔹 Rota `GET /hello`

- URL: `http://localhost:8080/hello`

**Curl:**

```bash
curl http://localhost:8080/hello
```

**Resposta esperada:**

```json
{"message": "Olá, mundo do Java puro!"}
```

---

### 🔹 Rota `GET /products`

- URL: `http://localhost:8080/products`

**Curl:**

```bash
curl http://localhost:8080/products
```

**Resposta esperada (exemplo):**

```json
[
  { "id": 1, "name": "Notebook", "price": 3500.0 },
  { "id": 2, "name": "Mouse Gamer", "price": 120.0 },
  { "id": 3, "name": "Teclado Mecânico", "price": 450.0 }
]
```

---

### 🔹 Rota `GET /products/{id}`

- URL: `http://localhost:8080/products/1`

**Curl:**

```bash
curl http://localhost:8080/products/1
```

**Resposta esperada:**

```json
{ "id": 1, "name": "Notebook", "price": 3500.0 }
```

**Erros tratados:**

- `GET /products/abc` → **400 (ID inválido)**
- `GET /products/999` → **404 (Produto não encontrado)**

---

### 🔹 Rota `POST /products`

- URL: `http://localhost:8080/products`
- Método: `POST`
- Content-Type: `application/json`

**Curl:**

```bash
curl -X POST http://localhost:8080/products \
  -H "Content-Type: application/json" \
  -d '{"name": "Monitor 4K", "price": 1200.0}'
```

**Body da requisição (exemplo):**

```json
{
  "name": "Monitor 4K",
  "price": 1200.0
}
```

**Resposta esperada (201 Created):**

```json
{
  "id": 4,
  "name": "Monitor 4K",
  "price": 1200.0
}
```

> **Nota:** O ID é gerado automaticamente pelo servidor (próximo ID disponível). Não é necessário enviar o `id` no body da requisição.

**Erros tratados:**

- `POST /products` com body vazio → **400 (Empty body)**
- `POST /products` com método não permitido → **405 (Método não permitido)**

---

## 🔧 Próximos passos (evolução planejada)

- [x] Adicionar suporte a `POST /products` para criar novos produtos
- [ ] Adicionar `DELETE /products/{id}` para remover um produto
- [ ] Adicionar logs mais completos para cada requisição
- [ ] Persistir dados em arquivo (simulando banco)
- [ ] Migrar a ideia para um projeto equivalente em Spring Boot

---

## 📌 Objetivo de estudo

Este projeto é parte prática da minha transição focada em:

- Fortalecer base em **Java puro**
- Compreender backend em nível baixo (sem frameworks)
- Me preparar para atuar como **Software Engineer Backend (Java)** em empresas de alta escala.

---

## 📧 Contato

Vinicius Aguiar — Software Engineer  
LinkedIn: https://www.linkedin.com/in/viniciusaguiar-araujo/  
GitHub: https://github.com/ViniAguiar1