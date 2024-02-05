package br.com.fiapstore.pedido.domain.repository;

import org.springframework.messaging.handler.annotation.Payload;

public interface IPedidoQueueAdapterIN {
    void receive(@Payload String message);
}
