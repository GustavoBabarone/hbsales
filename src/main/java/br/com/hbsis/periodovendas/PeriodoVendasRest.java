package br.com.hbsis.periodovendas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/periodoVendas")
public class PeriodoVendasRest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PeriodoVendasRest.class);
    private final PeriodoVendasService periodoVendasService;

    @Autowired
    public PeriodoVendasRest(PeriodoVendasService periodoVendasService) {
        this.periodoVendasService = periodoVendasService;
    }

    @PostMapping
    public PeriodoVendasDTO save(@RequestBody PeriodoVendasDTO periodoVendasDTO){

        LOGGER.info("Recebendo save de periodo de vendas");
        return this.periodoVendasService.salvar(periodoVendasDTO);
    }

    @GetMapping("/{id}")
    public PeriodoVendasDTO find(@PathVariable("id") Long id){

        LOGGER.info("Recebendo findById para periodo de vendas de id: [{}]", id);
        return this.periodoVendasService.findById(id);
    }

    @PutMapping("/{id}")
    public PeriodoVendasDTO update(@PathVariable("id") Long id, @RequestBody PeriodoVendasDTO periodoVendasDTO){

        LOGGER.info("Recebendo update para periodo de vendas de id: [{}]", id);
        return this.periodoVendasService.atualizar(periodoVendasDTO, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id){

        LOGGER.info("Recebendo delete para periodo de vendas de id: [{}]", id);
        this.periodoVendasService.deletar(id);
    }
}
