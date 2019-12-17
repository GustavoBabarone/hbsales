package br.com.hbsis.linhaCategoria;

import br.com.hbsis.categoriaProduto.CategoriaProdutoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 *  CLASSE RESPONSÁVEL POR RECEBER AS REQUISIÇÕES EXTERNAS AO SISTEMA
 */
@RestController
@RequestMapping("/linhaCategoria") /** MAPEAMENTO DAS SOLICITAÇÕES */
public class LinhaCategoriaRest {

    /* ENVIAR MENSAGEM VIA 'CONSOLE' DURANTE EXECUÇÃO DO PROGRAMA */
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

        return this.linhaCategoriaService.save(linhaCategoriaDTO);
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

        return this.linhaCategoriaService.alterar(linhaCategoriaDTO, id);

    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable("id") Long id){

        LOGGER.info("Recebendo exclusão para categoria de id: {}", id);

        this.linhaCategoriaService.excluir(id);
    }

    // EXPORTAR PARA CSV - ATIVIDADE 6
    // MÉTODO DE LISTAGEM DE DADOS
    @GetMapping("/exportarcsv")
    public void exportarCSV(HttpServletResponse response) throws Exception {

        LOGGER.info("Recebendo persistência de exportação para CSV...");

        linhaCategoriaService.findAll(response);

    }

    // IMPORTAR DE UM CSV - ATIVIDADE 7
    @PostMapping("/importarcsv")
    public void importarCSV(@RequestParam("file")MultipartFile file) throws Exception {

        LOGGER.info("Recebendo importação de um CSV...");

        linhaCategoriaService.obterTudo(file);
    }
}








