# Projetos praticos

# 📦 msOrdersService – E-commerce com Microsserviços

O msOrdersService é o microserviço responsável pelo gerenciamento de pedidos dentro da arquitetura de e-commerce baseada em microsserviços. Ele persiste dados no PostgreSQL e publica/consome eventos no Kafka.

------------------------------------------------------------
🚀 Pré-requisitos
1. Docker Desktop instalado e rodando
2. Java 17+
3. Maven 3.8+
4. PostgreSQL e Kafka ativos (via Docker Compose)

------------------------------------------------------------
⚙️ Executando em Docker
1. Subir toda a infraestrutura e microsserviços:
   docker compose up -d --build
2. Verificar containers ativos:
   docker ps
3. Acessar logs do msOrdersService:
   docker logs -f projeto-e-commercecommicroservicos_orders-service
4. Testar API:
   POST http://localhost:8080/orders
   Content-Type: application/json
   {
     "customerName": "Lucas",
     "totalAmount": 150.0
   }

   GET http://localhost:8080/orders

------------------------------------------------------------
⚙️ Executando Localmente (IntelliJ ou terminal)
1. Subir apenas infraestrutura com Docker:
   docker compose up -d postgres kafka zookeeper rabbitmq
2. Rodar o serviço no IntelliJ:
   VM options: -Dspring.profiles.active=local
3. Rodar o serviço via terminal (formas possíveis):
   - mvn spring-boot:run "-Dspring-boot.run.profiles=local"  
     (✔ Recomendada para testes, pois foi validada e funcionou corretamente)
   - mvn spring-boot:run -Dspring.profiles.active=local  
     (forma genérica, também ativa o profile local)
4. Rodar testes:
   mvn test "-Dspring.profiles.active=local"

------------------------------------------------------------
🔗 Conexão com o Banco para consultas
1. URL JDBC:
   jdbc:postgresql://localhost:5432/ecommerce_db
2. Usuário: ecommerce
3. Senha: ecommerce
4. Dica de conexão no DBeaver:
   - Criar nova conexão PostgreSQL
   - Host: localhost
   - Porta: 5432
   - Banco: ecommerce_db
   - Usuário: ecommerce
   - Senha: ecommerce
   - Em "Driver properties", ajustar ssl para "false" se necessário
   Essa configuração no DBeaver facilita visualizar tabelas, rodar queries e acompanhar se os dados estão sendo persistidos corretamente.

------------------------------------------------------------
🗂 Estrutura básica
1. Entity: Order
2. Tabela: orders
3. Campos:
   - id (PK, auto-increment)
   - customer_name (string)
   - total_amount (float)

------------------------------------------------------------
🛠 Ciclo rápido de comandos
1. Derrubar tudo:
   docker compose down
2. Subir tudo com rebuild:
   docker compose up -d --build
3. Subir só infraestrutura:
   docker compose up -d postgres kafka zookeeper rabbitmq
4. Ver containers ativos:
   docker ps
