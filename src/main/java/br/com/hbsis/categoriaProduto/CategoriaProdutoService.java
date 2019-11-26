package br.com.hbsis.categoriaProduto;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoriaProdutoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoriaProdutoService.class);

    private final ICategoriaProdutoRepository iCategoriaProdutoRepository;

    /* CONSTRUTOR */
    public CategoriaProdutoService(ICategoriaProdutoRepository iCategoriaProdutoRepository) {
        this.iCategoriaProdutoRepository = iCategoriaProdutoRepository;
    }

    public CategoriaProdutoDTO save(CategoriaProdutoDTO categoriaProdutoDTO){

        this.validate(categoriaProdutoDTO);

        LOGGER.info("Salvando categoria");
        LOGGER.debug("Categoria: {}", categoriaProdutoDTO);

        Categoria categoria = new Categoria();
        categoria.setNomeCategoria(categoriaProdutoDTO.getNomeCategoria());
        categoria.setIdFornecedorCategoria(categoriaProdutoDTO.getIdFornecedorCategoria());

        categoria = this.iCategoriaProdutoRepository.save(categoria);

        return CategoriaProdutoDTO.of(categoria);
    }

    public void validate(CategoriaProdutoDTO categoriaProdutoDTO){
        LOGGER.info("Validando categoria");

        /* .toString() -> pois é uma comparação entre tipo 'String' e 'Numérico/Long' */
        if(StringUtils.isEmpty(categoriaProdutoDTO.getIdFornecedorCategoria().toString())){
            throw new IllegalArgumentException("CategoriaDTO não deve ser nulo/vazio");
        }
    }

    public CategoriaProdutoDTO findById(Long id){

        Optional<Categoria> categoriaOptional = this.iCategoriaProdutoRepository.findById(id);

        if(categoriaOptional.isPresent()){
            CategoriaProdutoDTO.of(categoriaOptional.get());
        }

        throw new IllegalArgumentException(String.format("ID %s não existe",  id));

    }

    public CategoriaProdutoDTO update(CategoriaProdutoDTO categoriaProdutoDTO, Long id){

        Optional<Categoria> categoriaExistenteOptional = this.iCategoriaProdutoRepository.findById(id);

        if(categoriaExistenteOptional.isPresent()){
            Categoria categoriaExistente = categoriaExistenteOptional.get();

            LOGGER.info("Atualizando categoria... id: [{}]", categoriaExistente.getId());
            LOGGER.debug("Payload: {}", categoriaProdutoDTO);
            LOGGER.debug("Categoria existente: {}", categoriaExistente);

            categoriaExistente.setNomeCategoria(categoriaProdutoDTO.getNomeCategoria());
            categoriaExistente.setIdFornecedorCategoria(categoriaProdutoDTO.getIdFornecedorCategoria());

            categoriaExistente = this.iCategoriaProdutoRepository.save(categoriaExistente);

            return  CategoriaProdutoDTO.of(categoriaExistente);

        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));

    }

    public void delete(Long id){
        LOGGER.info("Executando  delete para categoria de id: [{}]", id);

        this.iCategoriaProdutoRepository.deleteById(id);
    }

}
