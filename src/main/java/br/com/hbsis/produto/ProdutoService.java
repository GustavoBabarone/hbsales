package br.com.hbsis.produto;

import br.com.hbsis.linhaCategoria.LinhaCategoria;
import br.com.hbsis.linhaCategoria.LinhaCategoriaDTO;
import br.com.hbsis.linhaCategoria.LinhaCategoriaService;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.spi.LoggerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * CLASSE RESPONSÁVEL PELO PROCESSAMENTO DA REGRA DE NEGÓCIO
 */
@Service
public class ProdutoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProdutoService.class);

    private final IProdutoRepository iProdutoRepository;
    private final LinhaCategoriaService linhaCategoriaService;

    /* CONSTRUTOR */
    @Autowired
    public ProdutoService(IProdutoRepository iProdutoRepository, LinhaCategoriaService linhaCategoriaService) {
        this.iProdutoRepository = iProdutoRepository;
        this.linhaCategoriaService = linhaCategoriaService;
    }

    // MÉTODO DE CADASTRAMENTO DO PRODUTO
    public ProdutoDTO save(ProdutoDTO produtoDTO){

        this.validate(produtoDTO);

        LOGGER.info("Salvando produto...");
        LOGGER.debug("Produto: {}", produtoDTO);

        Produto produto = new Produto();
        produto.setCodigo(produtoDTO.getCodigo());
        produto.setNome(produtoDTO.getNome());
        produto.setPreco(produtoDTO.getPreco());

        /* CONERSÃO DE OBJ */
        LinhaCategoriaDTO linhaCategoriaDTO = linhaCategoriaService.findById(produtoDTO.getIdLinha());

        LinhaCategoria linhaCategoria = converter(linhaCategoriaDTO);
        /* FIM DE CONVERSÃO */

        produto.setLinhaCategoria(linhaCategoria);
        produto.setUnidadeCaixa(produtoDTO.getUnidadeCaixa());
        produto.setPesoUnidade(produtoDTO.getPesoUnidade());
        produto.setValidade(produtoDTO.getValidade());

        produto = this.iProdutoRepository.save(produto);

        return ProdutoDTO.of(produto);

    }

    // MÉTODO DE VALIDAÇÃO DOS CAMPOS DO PRODUTO
    public void validate(ProdutoDTO produtoDTO){

        LOGGER.info("Validando produto...");

        // CONDICIONAIS DE VALIDAÇÃO - OBS: .toString() PARA CONVERTER TIPO 'Long' PARA 'String'
        if(produtoDTO == null){
            throw new IllegalArgumentException("ProtudoDTO não deve ser nulo");
        }

        if(produtoDTO.getId() == null){
            throw new IllegalArgumentException("Id não deve ser nulo");
        }

        if(produtoDTO.getCodigo() == null){
            throw new IllegalArgumentException("Código não deve ser nulo");
        }

        if(StringUtils.isEmpty(produtoDTO.getNome())){
            throw new IllegalArgumentException("Nome não deve ser nulo/vazio");
        }

        if(StringUtils.isEmpty(produtoDTO.getPreco())){
            throw new IllegalArgumentException("Preco não deve ser nulo/vazio");
        }

        if(produtoDTO.getIdLinha() == null){
            throw new IllegalArgumentException("Id da linha não deve ser nulo");
        }

        if(produtoDTO.getUnidadeCaixa() == null){
            throw new IllegalArgumentException("Unidade por caixa não deve ser nula");
        }

        if(produtoDTO.getPesoUnidade() == null){
            throw new IllegalArgumentException("Peso por unidade não deve ser nulo");
        }

        if(StringUtils.isEmpty(produtoDTO.getValidade())){
            throw new IllegalArgumentException("Validade não deve ser nula");
        }
    }

    // MÉTODO DE CONVERSÃO DE VARIÁVEL linhaCategoriaDTO EM linhaCategoria
    public LinhaCategoria converter(LinhaCategoriaDTO linhaCategoriaDTO){

        LinhaCategoria linhaCategoria = new LinhaCategoria();

        linhaCategoria.setId(linhaCategoriaDTO.getId());

        return linhaCategoria;
    }

    public ProdutoDTO findById(Long id){

        Optional<Produto> produtoOptional = this.iProdutoRepository.findById(id);

        if(produtoOptional.isPresent()){

            Produto produto = produtoOptional.get();
            ProdutoDTO produtoDTO = ProdutoDTO.of(produto);

            return produtoDTO;
        }

        String format = String.format("Id %s não existe", id);

        throw new IllegalArgumentException(format);

    }

    public ProdutoDTO update(ProdutoDTO produtoDTO, Long id){

        // OBTER PRODUTO EXISTENTE NO BANCO DE DADOS
        Optional<Produto> produtoExistenteOptional = this.iProdutoRepository.findById(id);

        if(produtoExistenteOptional.isPresent()){

            Produto produtoExistente = produtoExistenteOptional.get();

            LOGGER.info("Atualizando produto... id: [{}]", produtoExistente.getId());
            LOGGER.debug("Payload: {}", produtoDTO);
            LOGGER.debug("Produto existente: {}", produtoExistente);

            produtoExistente.setCodigo(produtoDTO.getCodigo());
            produtoExistente.setNome(produtoDTO.getNome());
            produtoExistente.setPreco(produtoDTO.getPreco());

            /* CONVERSÃO */
            LinhaCategoriaDTO linhaCategoriaDTO = linhaCategoriaService.findById(produtoDTO.getIdLinha());
            LinhaCategoria linhaCategoria = converter(linhaCategoriaDTO);
            /* FIM CONVERSÃO */

            produtoExistente.setLinhaCategoria(linhaCategoria);
            produtoExistente.setUnidadeCaixa(produtoDTO.getUnidadeCaixa());
            produtoExistente.setPesoUnidade(produtoDTO.getPesoUnidade());
            produtoExistente.setValidade(produtoDTO.getValidade());

            produtoExistente = this.iProdutoRepository.save(produtoExistente);

            return ProdutoDTO.of(produtoExistente);
        }

        String format = String.format("Id %s não existe", id);

        throw new IllegalArgumentException(format);
    }

    public void delete(Long id){

        LOGGER.info("Executando delete para produto de id: [{}]", id);

        this.iProdutoRepository.deleteById(id);
    }
}










