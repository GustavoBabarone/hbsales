package br.com.hbsis.categoriaproduto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/categorias")
public class CategoriaProdutoRest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoriaProdutoRest.class);
    private final CategoriaProdutoService categoriaProdutoService;

    @Autowired
    public CategoriaProdutoRest(CategoriaProdutoService categoriaProdutoService) {
        this.categoriaProdutoService = categoriaProdutoService;
    }

    @PostMapping
    public CategoriaProdutoDTO save(@RequestBody CategoriaProdutoDTO categoriaProdutoDTO) {

        LOGGER.info("Recebendo save para categoria de produto");
        return this.categoriaProdutoService.salvar(categoriaProdutoDTO);
    }

    @GetMapping("/{id}")
    public CategoriaProdutoDTO find(@PathVariable("id") Long id){

        LOGGER.info("Recebendo findById para categoria de produto de id: [{}]", id);
        return this.categoriaProdutoService.findById(id);
    }

    @PutMapping("/{id}")
    public CategoriaProdutoDTO update(@PathVariable("id") Long id, @RequestBody CategoriaProdutoDTO categoriaProdutoDTO){

        LOGGER.info("Recebendo update para categoria de produto de id: [{}]", id);
        return this.categoriaProdutoService.atualizar(categoriaProdutoDTO, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id){

        LOGGER.info("Recebendo delete para categoria de produto de id: [{}]", id);
        this.categoriaProdutoService.deletar(id);
    }

    @GetMapping("/export-categoria-produto")
    public void exportCategoriaProduto(HttpServletResponse response) throws Exception {

        LOGGER.info("Recebendo exportação de CSV categoria de produto");
        this.categoriaProdutoService.exportarCsv(response);
    }

    @PostMapping("/import-categoria-produto")
    public void importCategoriaProduto(@RequestParam("file") MultipartFile arquivo) throws Exception {

        LOGGER.info("Recebendo importação de CSV categoria de produto");
        this.categoriaProdutoService.importarCategoria(arquivo);
    }
}
