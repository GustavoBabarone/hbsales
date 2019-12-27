package br.com.hbsis.csv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/csv")
public class CsvRest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CsvRest.class);
    private final CsvService csvService;

    @Autowired
    public CsvRest(CsvService csvService) {
        this.csvService = csvService;
    }

    /** ATIVIDADE 3 */
    @GetMapping("/export-categoria-produto")
    public void exportCategoriaProduto(HttpServletResponse response) throws Exception {

        LOGGER.info("Recebendo exportação para CSV categoria produto... ");
        this.csvService.exportarCategoria(response);
    }

    /** ATIVIDADE 4 */
    @PostMapping("/import-categoria-produto")
    public void importCategoriaProduto(@RequestParam("file") MultipartFile arquivo) throws Exception {

        LOGGER.info("Recebendo importação de um CSV categoria produto...");
        this.csvService.importarCategoria(arquivo);
    }

    /** ATIVIDADE 6 */
    @GetMapping("/export-linha-categoria")
    public void exportLinhaCategoria(HttpServletResponse response) throws Exception {

        LOGGER.info("Recebendo exportação para CSV linha categoria... ");
        this.csvService.exportarLinha(response);
    }

    /** ATIVIDADE 7 */
    @PostMapping("/import-linha-categoria")
    public void importLinhaCategoria(@RequestParam("file")MultipartFile arquivo) throws Exception {

        LOGGER.info("Recebendo importação de um CSV linha categoria...");
        this.csvService.importarLinha(arquivo);
    }

    /** ATIVIDADE 9 */
    @GetMapping("/export-produto")
    public void exportProduto(HttpServletResponse file) throws Exception {

        LOGGER.info("Recebendo exportação para CSV produto...");
        this.csvService.exportarProduto(file);
    }

    /** ATIVIDADE 10 */
    @PostMapping("/import-produto")
    public void importProduto(@RequestParam("file")MultipartFile arquivo) throws Exception {

        LOGGER.info("Recebendo importação de um CSV produto...");
        this.csvService.importarProduto(arquivo);
    }

//    /** ATIVIDADE 11 */
//    @PostMapping("/import-produtos-por-fornecedor/{id}")
//    public void importProdutoPorFornecedor(@RequestParam("file")MultipartFile arquivo, @PathVariable("id") Long id) throws Exception {
//
//        LOGGER.info("Recebendo importação de um CSV produto por fornecedor... id: [{}]", id);
//        this.csvService.importarProdutoPorFornecedor(arquivo, id);
//    }

}
