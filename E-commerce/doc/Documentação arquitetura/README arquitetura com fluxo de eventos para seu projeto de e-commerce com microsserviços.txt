[ Cliente ]
     |
     v
API REST / Criar Pedido
     |
     v
[ Serviço de Pedidos ]
     |
     v
Evento "Order Created" → Kafka
     |
     v
[ Kafka Event Bus ]
     |                     \
     v                      \
[ Serviço de Pagamentos ]   \
     |                       \
Valida Transação              \
     |                         \
Evento "Payment Approved" → RabbitMQ
     |
     v
[ Serviço de Estoque ]
     |
     v
Atualiza Estoque
     |
     v
Evento "Stock Updated"

Todos os serviços conectados ao:
[ Banco de Dados PostgreSQL ]

Monitoramento:
- Kafka: Kafka UI / Prometheus
- RabbitMQ: RabbitMQ Management / Grafana





/ecommerce-project
   ├── docker-compose.yml
   ├── orders-service
   │    └── Dockerfile
   ├── payments-service
   │    └── Dockerfile
   └── inventory-service
        └── Dockerfile



