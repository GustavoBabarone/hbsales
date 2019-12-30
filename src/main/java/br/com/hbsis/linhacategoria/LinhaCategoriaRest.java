package br.com.hbsis.linhacategoria;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/linhaCategoria")
public class LinhaCategoriaRest {

    private static final Logger LOGGER = LoggerFactory.getLogger(LinhaCategoriaRest.class);
    private final LinhaCategoriaService linhaCategoriaService;

    @Autowired /** CONTRUTOR */
    public LinhaCategoriaRest(LinhaCategoriaService linhaCategoriaService) {
        this.linhaCategoriaService = linhaCategoriaService;
    }

    /** MÉTODOS */
    @PostMapping
    public LinhaCategoriaDTO save(@RequestBody LinhaCategoriaDTO linhaCategoriaDTO) {

        LOGGER.info("Recebendo solicitação de persistência de linha de categoria...");
        LOGGER.debug("Payload: {}", linhaCategoriaDTO);
        return this.linhaCategoriaService.salvar(linhaCategoriaDTO);
    }

    @GetMapping("/{id}")
    public LinhaCategoriaDTO find(@PathVariable("id") Long id){

        LOGGER.info("Recebedendo findById... id: [{}]", id);
        return this.linhaCategoriaService.findById(id);
    }

    @PutMapping("/{id}")
    public LinhaCategoriaDTO update(@PathVariable("id") Long id, @RequestBody LinhaCategoriaDTO linhaCategoriaDTO) {

        LOGGER.info("Recebendo update para linha de categoria de id: {}", id);
        LOGGER.debug("Payload: {}", linhaCategoriaDTO);
        return this.linhaCategoriaService.atualizar(linhaCategoriaDTO, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id){

        LOGGER.info("Recebendo delete para categoria de id: {}", id);
        this.linhaCategoriaService.deletar(id);
    }

    /** ATIVIDADE 6 */
    @GetMapping("/export-linha-categoria")
    public void exportLinhaCategoria(HttpServletResponse response) throws Exception {

        LOGGER.info("Recebendo exportação para CSV linha categoria... ");
        this.linhaCategoriaService.exportarLinha(response);
    }

    /** ATIVIDADE 7 */
    @PostMapping("/import-linha-categoria")
    public void importLinhaCategoria(@RequestParam("file") MultipartFile arquivo) throws Exception {

        LOGGER.info("Recebendo importação de um CSV linha categoria...");
        this.linhaCategoriaService.importarLinha(arquivo);
    }
}








