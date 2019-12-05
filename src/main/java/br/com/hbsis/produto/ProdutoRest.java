package br.com.hbsis.produto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 *  CLASSE RESPONSÁVEL POR RECEBER AS REQUISIÇÕES EXTERNAS AO SISTEMA
 */
@RestController
@RequestMapping("/produtos") // MAPEAMENTO DAS SOLICITAÇÕES VIA URL
public class ProdutoRest {

    // ENVIAR MSG VIA 'CONSOLE' DURANTE EXECUÇÃO DO PROGRAMA
    private static final Logger LOGGER = LoggerFactory.getLogger(ProdutoRest.class);

    private final ProdutoService produtoService;

    @Autowired
    public ProdutoRest(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @PostMapping
    public ProdutoDTO save(@RequestBody ProdutoDTO produtoDTO) {

        LOGGER.info("Recebendo solicitação de persistência de categoria...");
        LOGGER.debug("Payload: {}", produtoDTO);

        return this.produtoService.save(produtoDTO);

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

        return this.produtoService.update(produtoDTO, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id){

        LOGGER.info("Recebendo delete para produto de id: {}", id);

        this.produtoService.delete(id);
    }

    // EXPORTAR PARA CSV - ATIVIDADE 9
    @GetMapping("/exportarcsv")
    public void exportarCSV(HttpServletResponse file) throws Exception {

        LOGGER.info("Recebendo exportação para CSV...");

        produtoService.findAll(file);
    }

    // IMPORTAR DE UM CSV - ATIVIDADE 10
    @PostMapping("/importarcsv")
    public void importarCSV(@RequestParam("file")MultipartFile file) throws Exception {

        LOGGER.info("Recebendo importação de um CSV...");

        produtoService.obterTudo(file);
    }

}
