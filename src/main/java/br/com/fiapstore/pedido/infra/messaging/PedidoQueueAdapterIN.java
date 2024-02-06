package br.com.fiapstore.pedido.infra.messaging;

import br.com.fiapstore.pedido.domain.exception.OperacaoInvalidaException;
import br.com.fiapstore.pedido.domain.exception.PedidoNaoEncontradoException;
import br.com.fiapstore.pedido.domain.exception.PercentualDescontoAcimaDoLimiteException;
import br.com.fiapstore.pedido.domain.repository.IPedidoQueueAdapterIN;
import br.com.fiapstore.pedido.domain.usecase.ConfirmarPedidoUseCase;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PedidoQueueAdapterIN implements IPedidoQueueAdapterIN {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private Gson gson;

    private final ConfirmarPedidoUseCase confirmarPedidoUseCase;

    public PedidoQueueAdapterIN (ConfirmarPedidoUseCase confirmarPedidoUseCase) {
        this.confirmarPedidoUseCase = confirmarPedidoUseCase;
    }
    @RabbitListener(queues = {"${queue4.name}"})
    @Override
    public void receive(String message) throws PedidoNaoEncontradoException, PercentualDescontoAcimaDoLimiteException, OperacaoInvalidaException {
        Map<String, String> mensagem = gson.fromJson(message, HashMap.class);
        this.confirmarPedidoUseCase.executar(mensagem.get("codigoPedido"));
        logger.debug("Confirmação do pedido registrado");
    }
}
