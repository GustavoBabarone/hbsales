package br.com.hbsis.linhacategoria;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/linhaCategoria")
public class LinhaCategoriaRest {

    private static final Logger LOGGER = LoggerFactory.getLogger(LinhaCategoriaRest.class);
    private final LinhaCategoriaService linhaCategoriaService;

    @Autowired
    public LinhaCategoriaRest(LinhaCategoriaService linhaCategoriaService) {
        this.linhaCategoriaService = linhaCategoriaService;
    }

    @PostMapping
    public LinhaCategoriaDTO save(@RequestBody LinhaCategoriaDTO linhaCategoriaDTO) {

        LOGGER.info("Recebendo solicitação de persistência de linha de categoria...");
        LOGGER.debug("Payload: {}", linhaCategoriaDTO);
        return this.linhaCategoriaService.salvar(linhaCategoriaDTO);
    }

    @GetMapping("/{id}")
    public LinhaCategoriaDTO obter(@PathVariable("id") Long id){

        LOGGER.info("Recebedendo obtenção por id... id: [{}]", id);
        return this.linhaCategoriaService.findById(id);

    }

    @PutMapping("/{id}")
    public LinhaCategoriaDTO alterar(@PathVariable("id") Long id, @RequestBody LinhaCategoriaDTO linhaCategoriaDTO) {

        LOGGER.info("Recebendo alteração para linha de categoria de id: {}", id);
        LOGGER.debug("Payload: {}", linhaCategoriaDTO);
        return this.linhaCategoriaService.atualizar(linhaCategoriaDTO, id);

    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable("id") Long id){

        LOGGER.info("Recebendo exclusão para categoria de id: {}", id);
        this.linhaCategoriaService.deletar(id);
    }
}








