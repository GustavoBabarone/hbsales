package br.com.hbsis.produto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/produtos")
public class ProdutoRest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProdutoRest.class);
    private final ProdutoService produtoService;

    @Autowired /** CONSTRUTOR */
    public ProdutoRest(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @PostMapping
    public ProdutoDTO save(@RequestBody ProdutoDTO produtoDTO) {

        LOGGER.info("Recebendo solicitação de persistência de categoria...");
        LOGGER.debug("Payload: {}", produtoDTO);
        return this.produtoService.salvar(produtoDTO);
    }

    @GetMapping("/{id}")
    public ProdutoDTO find(@PathVariable("id") Long id) {

        LOGGER.info("Recebendo find by id... id: [{}]", id);
        return this.produtoService.findById(id);
    }

    @PutMapping("/{id}")
    public ProdutoDTO update(@PathVariable("id") Long id, @RequestBody ProdutoDTO produtoDTO) {

        LOGGER.info("Recebendo update para produto de id: {}", id);
        LOGGER.debug("Paylaod: {}", produtoDTO);
        return this.produtoService.atualizar(produtoDTO, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id){

        LOGGER.info("Recebendo delete para produto de id: {}", id);
        this.produtoService.deletar(id);
    }


}
