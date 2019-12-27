package br.com.hbsis.fornecedor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fornecedores")
public class FornecedorRest {

    private static final Logger LOGGER = LoggerFactory.getLogger(FornecedorRest.class);
    private final FornecedorService fornecedorService;

    @Autowired /** CONSTRUTOR */
    public FornecedorRest(FornecedorService fornecedorService){
        this.fornecedorService = fornecedorService;
    }

    @PostMapping
    public FornecedorDTO save(@RequestBody FornecedorDTO fornecedorDTO){

        LOGGER.info("Recebendo save para fornecedor...");
        LOGGER.debug("PayaLoad: {}", fornecedorDTO);
        return this.fornecedorService.salvar(fornecedorDTO);
    }

    @GetMapping("/{id}")
    public FornecedorDTO find(@PathVariable("id") Long id){

        LOGGER.info("Recebendo findById... id: [{}]", id);
        return this.fornecedorService.findById(id);
    }

    @PutMapping("/{id}")
    public FornecedorDTO update(@PathVariable("id") Long id, @RequestBody FornecedorDTO fornecedorDTO){

        LOGGER.info("Recebendo Update para fornecedor de ID: {}", id);
        LOGGER.debug("PayLoad: {}", fornecedorDTO);
        return this.fornecedorService.atualizar(fornecedorDTO, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id){

        LOGGER.info("Recebendo delete para fornecedor de ID: {}", id);
        this.fornecedorService.deletar(id);
    }

}








