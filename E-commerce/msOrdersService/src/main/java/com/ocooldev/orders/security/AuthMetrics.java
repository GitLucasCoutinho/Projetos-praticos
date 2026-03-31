package com.ocooldev.orders.security;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class AuthMetrics {

    private final MeterRegistry registry;

    public AuthMetrics(MeterRegistry registry) {
        this.registry = registry;
    }

    // Incrementa contador de logins bem-sucedidos
    public void incrementLoginSuccess() {
        registry.counter("auth_login_success_total").increment();
    }

    // Incrementa contador de falhas de login
    public void incrementLoginFailure() {
        registry.counter("auth_login_failure_total").increment();
    }

    // Registra tempo de validação de token
    public void recordTokenValidation(long millis) {
        registry.timer("auth_token_validation_time")
                .record(millis, java.util.concurrent.TimeUnit.MILLISECONDS);
    }

    public void incrementOrdersCreated() {
        registry.counter("orders_created_total").increment();
    }

    public void incrementOrdersFailed() {
        registry.counter("orders_failed_total").increment();
    }

    public void recordOrderProcessingTime(long millis) {
        registry.timer("orders_processing_time")
                .record(millis, java.util.concurrent.TimeUnit.MILLISECONDS);
    }


}
