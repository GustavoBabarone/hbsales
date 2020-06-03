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

    @Autowired
    public LinhaCategoriaRest(LinhaCategoriaService linhaCategoriaService) {
        this.linhaCategoriaService = linhaCategoriaService;
    }

    @PostMapping
    public LinhaCategoriaDTO save(@RequestBody LinhaCategoriaDTO linhaCategoriaDTO) {

        LOGGER.info("Recebendo save de linha de categoria");
        return this.linhaCategoriaService.salvar(linhaCategoriaDTO);
    }

    @GetMapping("/{id}")
    public LinhaCategoriaDTO find(@PathVariable("id") Long id){

        LOGGER.info("Recebedendo findById para linha de categoria de id: [{}]", id);
        return this.linhaCategoriaService.findById(id);
    }

    @PutMapping("/{id}")
    public LinhaCategoriaDTO update(@PathVariable("id") Long id, @RequestBody LinhaCategoriaDTO linhaCategoriaDTO) {

        LOGGER.info("Recebendo update para linha de categoria de id: [{}]", id);
        return this.linhaCategoriaService.atualizar(linhaCategoriaDTO, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id){

        LOGGER.info("Recebendo delete para linah de categoria de id: [{}]", id);
        this.linhaCategoriaService.deletar(id);
    }

    @GetMapping("/export-linha-categoria")
    public void exportLinhaCategoria(HttpServletResponse response) throws Exception {

        LOGGER.info("Recebendo exportação de CSV linha de categoria");
        this.linhaCategoriaService.exportar(response);
    }

    @PostMapping("/import-linha-categoria")
    public void importLinhaCategoria(@RequestParam("file") MultipartFile arquivo) throws Exception {

        LOGGER.info("Recebendo importação de CSV linha de categoria");
        this.linhaCategoriaService.importar(arquivo);
    }
}








