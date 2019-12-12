package br.com.hbsis.categoriaProduto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.Scanner;

/**
 *  CLASSE RESPONSÁVEL POR RECEBER AS REQUISIÇÕES EXTERNAS AO SISTEMA
 */
@RestController
@RequestMapping("/categorias") // MAPEAMENTO DAS SOLICITAÇÕES
public class CategoriaProdutoRest {

    // ENVIA MENSAGENS PARA O 'CONSOLE' DURANTE EXECUÇÃO DO PROGRAMA
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoriaProdutoRest.class);

    private final CategoriaProdutoService categoriaProdutoService;

    @Autowired
    public CategoriaProdutoRest(CategoriaProdutoService categoriaProdutoService) {
        this.categoriaProdutoService = categoriaProdutoService;
    }

    @PostMapping
    public CategoriaProdutoDTO save(@RequestBody CategoriaProdutoDTO categoriaProdutoDTO) {
        LOGGER.info("Recebendo solicitação de persistência de categoria...");
        LOGGER.debug("Payload: {}", categoriaProdutoDTO);

        return this.categoriaProdutoService.save(categoriaProdutoDTO);
    }

    @GetMapping("/{id}")
    public CategoriaProdutoDTO find(@PathVariable("id") Long id){

        LOGGER.info("Recebendo find by id... id: [{}]", id);

        return this.categoriaProdutoService.findById(id);
    }

    @PutMapping("/{id}")
    public CategoriaProdutoDTO update(@PathVariable("id") Long id, @RequestBody CategoriaProdutoDTO categoriaProdutoDTO){
        LOGGER.info("Recebendo update para categoria de id: {}", id);
        LOGGER.debug("Payload: {}", categoriaProdutoDTO);

        return this.categoriaProdutoService.update(categoriaProdutoDTO, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id){
        LOGGER.info("Recebendo delete para categoria de id: {}", id);

        this.categoriaProdutoService.delete(id);
    }

    // EXPORTAR PARA CSV - ATIVIDADE 3
    // MÉTODO DE LISTAGEM DOS DADOS
    @GetMapping("/exportarcsv")
    public void exportarCSV(HttpServletResponse file) throws Exception {

        LOGGER.info("Recebendo exportação para CSV... ");

        categoriaProdutoService.findAll(file);
    }

    // IMPORTAR DE UM CSV - ATIVIDADE 4
    @PostMapping("/importarcsv")
    public void importarCSV(@RequestParam("file") MultipartFile file) throws Exception {

        LOGGER.info("Recebendo importação de um CSV...");

        categoriaProdutoService.obterTudo(file);

    }
}
