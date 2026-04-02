# Projetos praticos

# 📦 msOrdersService – Microsserviço de Pedidos para E-commerce

O **msOrdersService** é um microsserviço desenvolvido em **Spring Boot** responsável pelo gerenciamento de pedidos em uma arquitetura de e-commerce baseada em microsserviços. Ele permite a criação de pedidos com itens, persiste os dados em um banco **PostgreSQL**, publica eventos no **Kafka** para comunicação assíncrona e inclui autenticação JWT, métricas e documentação OpenAPI.

## 🚀 Funcionalidades

- **Criação de Pedidos**: Endpoint para criar pedidos com validação de dados (customerId, itens e valor total).
- **Persistência de Dados**: Utiliza JPA/Hibernate para salvar pedidos e itens no PostgreSQL.
- **Eventos Assíncronos**: Publica eventos no Kafka quando um pedido é criado, permitindo integração com outros microsserviços (ex.: inventário, notificações).
- **Autenticação JWT**: Protege os endpoints de pedidos com tokens JWT. Endpoint de login para gerar tokens.
- **Métricas e Monitoramento**: Exposição de métricas via Actuator (health, info, Prometheus) para monitoramento de pedidos criados, falhas e tempos de processamento.
- **Validação de Entrada**: Validações automáticas nos DTOs usando Bean Validation.
- **Documentação API**: Interface Swagger/OpenAPI para explorar e testar os endpoints.
- **Logs Estruturados**: Logs detalhados para rastreamento de operações.

## 🛠 Tecnologias Utilizadas

- **Java 21** com **Spring Boot 3.5.13**
- **Spring Web**: Para APIs REST
- **Spring Data JPA**: Para acesso ao banco de dados
- **PostgreSQL**: Banco de dados relacional
- **Spring Kafka**: Para integração com Apache Kafka
- **Spring Security**: Para autenticação JWT
- **JJWT**: Biblioteca para manipulação de tokens JWT
- **Spring Boot Actuator**: Para métricas e health checks
- **Micrometer + Prometheus**: Para coleta de métricas
- **SpringDoc OpenAPI**: Para documentação da API
- **Docker**: Para containerização
- **Maven**: Gerenciamento de dependências e build

## 📋 Pré-requisitos

- **Docker Desktop** instalado e rodando (para infraestrutura externa como PostgreSQL e Kafka)
- **Java 21+** instalado
- **Maven 3.8+** instalado
- **Git** para clonar o repositório (opcional)

## ⚙️ Executando Localmente (Desenvolvimento)

### 1. Subir Infraestrutura com Docker

Para desenvolvimento local, utilize o Docker Compose para subir PostgreSQL, Kafka e Zookeeper:

```bash
docker compose up -d postgres kafka zookeeper
```

Isso iniciará:
- PostgreSQL na porta 5432
- Kafka na porta 9092
- Zookeeper na porta 2181

### 2. Configuração do Projeto

O projeto usa perfis do Spring (`local` para desenvolvimento). As configurações estão em `src/main/resources/application.yaml`.

- **Perfil `local`**: Conecta ao PostgreSQL e Kafka locais (via Docker).

### 3. Executar o Serviço

#### Via IntelliJ IDEA:
- Abra o projeto no IntelliJ.
- Configure as VM Options: `-Dspring.profiles.active=local`
- Execute a classe `MsOrdersServiceApplication`.

#### Via Terminal (Maven):
```bash
# Compilar e executar (recomendado para testes)
mvn spring-boot:run "-Dspring-boot.run.profiles=local"

# Ou, forma alternativa
mvn spring-boot:run -Dspring.profiles.active=local
```

### 4. Verificar se Está Rodando

- **API**: Acesse `http://localhost:8080`
- **Swagger UI**: `http://localhost:8080/swagger-ui/index.html`
- **Actuator Health**: `http://localhost:8080/actuator/health`
- **Logs**: Verifique no console ou use `docker logs` para os containers.

### 5. Testar a API

#### 1. Fazer Login (obter token JWT):
```bash
POST http://localhost:8080/auth/login?username=admin
```
Resposta: Um token JWT (ex.: `eyJhbGciOiJIUzI1NiJ9...`)

#### 2. Criar um Pedido (usando o token):
```bash
POST http://localhost:8080/orders
Authorization: Bearer <seu-token-jwt>
Content-Type: application/json

{
  "customerId": "cliente123",
  "totalAmount": 150.00,
  "items": [
    {
      "productId": "produtoA",
      "quantity": 2
    },
    {
      "productId": "produtoB",
      "quantity": 1
    }
  ]
}
```
Resposta: Detalhes do pedido criado com ID gerado.

### 6. Executar Testes

```bash
mvn test -Dspring.profiles.active=local
```

Os testes incluem unitários, de integração e testes do Kafka.

## 🐳 Executando com Docker (Produção/Ambiente Containerizado)

### 1. Arquivo docker-compose.yml

Certifique-se de ter um `docker-compose.yml` na raiz do projeto ou no diretório pai com os serviços necessários (postgres, kafka, zookeeper, rabbitmq se aplicável). Exemplo básico:

```yaml
version: '3.8'
services:
  postgres:
    image: postgres:15
    environment:
      POSTGRES_DB: ecommerce_db
      POSTGRES_USER: ecommerce
      POSTGRES_PASSWORD: ecommerce
    ports:
      - "5432:5432"
  kafka:
    image: confluentinc/cp-kafka:7.4.0
    depends_on:
      - zookeeper
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka:9092
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
    ports:
      - "9092:9092"
  zookeeper:
    image: confluentinc/cp-zookeeper:7.4.0
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
      ZOOKEEPER_TICK_TIME: 2000
    ports:
      - "2181:2181"
  orders-service:
    build: ./msOrdersService
    ports:
      - "8080:8080"
    depends_on:
      - postgres
      - kafka
    environment:
      - SPRING_PROFILES_ACTIVE=docker
```

### 2. Subir Tudo com Docker Compose

```bash
# Subir todos os serviços com build
docker compose up -d --build

# Verificar containers ativos
docker ps

# Ver logs do msOrdersService
docker logs -f orders-service
```

O perfil `docker` será ativado automaticamente, conectando ao Kafka em `kafka:9092` e PostgreSQL em `postgres:5432`.

### 3. Testar

Use os mesmos endpoints de teste acima, mas acesse via `http://localhost:8080`.

## 📡 API Endpoints

### Autenticação
- `POST /auth/login?username={username}`: Gera um token JWT para o usuário.

### Pedidos
- `POST /orders`: Cria um novo pedido (requer JWT). Recebe `OrderRequestDTO` e retorna `OrderResponseDTO`.

### Documentação e Monitoramento
- `GET /swagger-ui/index.html`: Interface Swagger para explorar a API.
- `GET /v3/api-docs`: Documentação OpenAPI em JSON.
- `GET /actuator/health`: Status de saúde do serviço.
- `GET /actuator/info`: Informações do serviço.
- `GET /actuator/prometheus`: Métricas em formato Prometheus.

## 🗄 Esquema do Banco de Dados

- **Tabela `orders`**:
  - `id` (UUID, PK)
  - `customer_id` (VARCHAR)
  - `total_amount` (DECIMAL)
  - `created_at` (TIMESTAMP)

- **Tabela `order_item`**:
  - `id` (BIGINT, PK, AUTO_INCREMENT)
  - `order_id` (UUID, FK para orders)
  - `product_id` (VARCHAR)
  - `quantity` (INT)

O Hibernate cria/atualiza as tabelas automaticamente com `ddl-auto: update`.

## 📨 Eventos Kafka

- **Tópico**: `order-created`
- **Evento**: `OrderCreatedEvent` (publicado ao criar pedido)
  - `orderId`: UUID do pedido
  - `customerId`: ID do cliente
  - `items`: Lista de itens (productId, quantity)
  - `totalAmount`: Valor total
  - `createdAt`: Data de criação

Outros microsserviços podem consumir esse evento para ações como atualizar inventário ou enviar notificações.

## 📊 Métricas

O serviço expõe métricas via `/actuator/prometheus`:
- `orders_created_total`: Número de pedidos criados.
- `orders_failed_total`: Número de falhas na criação.
- `order_processing_time`: Tempo médio de processamento.

## 🧪 Testes

- **Unitários**: Testam lógica isolada (ex.: OrderService).
- **Integração**: Testam com banco e Kafka (usam Testcontainers).
- **Controller**: Testam endpoints REST.

Execute com `mvn test`.

## 🔧 Comandos Úteis

- **Derrubar tudo**: `docker compose down`
- **Subir com rebuild**: `docker compose up -d --build`
- **Subir só infraestrutura**: `docker compose up -d postgres kafka zookeeper`
- **Ver containers**: `docker ps`
- **Logs do serviço**: `docker logs -f orders-service`

## 🤝 Contribuição

1. Faça um fork do projeto.
2. Crie uma branch para sua feature (`git checkout -b feature/nova-funcionalidade`).
3. Commit suas mudanças (`git commit -am 'Adiciona nova funcionalidade'`).
4. Push para a branch (`git push origin feature/nova-funcionalidade`).
5. Abra um Pull Request.

## 📝 Licença

Este projeto é licenciado sob a MIT License.
