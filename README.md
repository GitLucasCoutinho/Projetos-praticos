# Projetos praticos

1. E-commerce com Microsserviços
- Tecnologias: Java + Spring Boot, Kafka, RabbitMQ, Docker.
- Descrição:
- Serviço de pedidos que publica eventos no Kafka.
- Serviço de pagamentos que consome eventos e valida transações.
- Serviço de estoque que atualiza disponibilidade via RabbitMQ.
- Diferencial: Mostra domínio de comunicação assíncrona e escalabilidade.
- Exemplo real: Projeto no GitHub com Producer/Consumer em Kafka para pedidos .

2. Sistema de Auditoria Bancária
- Tecnologias: Java + Quarkus/Spring, Kafka, RabbitMQ.
- Descrição:
- Cadastro de agências bancárias.
- Serviço de auditoria que recebe eventos de alteração de status (ativo/inativo).
- Validação automática de CNPJs.
- Diferencial: Demonstra uso de mensageria em cenários críticos de negócios.
- Fonte de inspiração: Curso da Alura sobre mensageria com Java .

3. Monitoramento de Logs e Métricas
- Tecnologias: Java, Kafka, ElasticSearch, Grafana.
- Descrição:
- Aplicação que coleta logs de diferentes microsserviços.
- Kafka centraliza os eventos.
- ElasticSearch indexa e Grafana exibe dashboards.
- Diferencial: Mostra integração de mensageria com observabilidade, muito valorizado em DevOps.

4. Plataforma de Streaming de Dados
- Tecnologias: Java, Kafka Streams, RabbitMQ.
- Descrição:
- Processamento em tempo real de dados (ex.: cliques em um site).
- RabbitMQ para filas de tarefas pesadas.
- Kafka Streams para análise em tempo real.
- Diferencial: Demonstra conhecimento em event-driven architecture.
