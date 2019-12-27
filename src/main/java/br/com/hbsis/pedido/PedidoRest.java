package br.com.hbsis.pedido;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/pedidos")
public class PedidoRest {

    public static final Logger LOGGER = LoggerFactory.getLogger(PedidoRest.class);
    public final PedidoService pedidoService;

    @Autowired
    public PedidoRest(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public PedidoDTO save(@RequestBody PedidoDTO pedidoDTO) throws ParseException {

        LOGGER.info("Recebendo requisição de cadastro de pedido...");
        LOGGER.debug("Payload... {}", pedidoDTO);

        return this.pedidoService.save(pedidoDTO);
    }
}
