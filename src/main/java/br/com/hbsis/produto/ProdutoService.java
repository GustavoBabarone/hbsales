package br.com.hbsis.produto;

import br.com.hbsis.linhacategoria.LinhaCategoria;
import br.com.hbsis.linhacategoria.LinhaCategoriaDTO;
import br.com.hbsis.linhacategoria.LinhaCategoriaService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProdutoService.class);
    private final IProdutoRepository iProdutoRepository;
    private final LinhaCategoriaService linhaCategoriaService;

    @Autowired /** CONSTRUTOR */
    public ProdutoService(IProdutoRepository iProdutoRepository, LinhaCategoriaService linhaCategoriaService) {
        this.iProdutoRepository = iProdutoRepository;
        this.linhaCategoriaService = linhaCategoriaService;
    }

    /** MÉTODOS DE CRUD */
    public ProdutoDTO salvar(ProdutoDTO produtoDTO){

        this.validarCamposTexto(produtoDTO);

        LOGGER.info("Salvando produto...");
        LOGGER.debug("Produto: {}", produtoDTO);

        Produto produto = new Produto();
        String codigoProcessado = formatarCodigoProduto(produtoDTO.getCodigoProduto().toUpperCase());
        produto.setCodigoProduto(codigoProcessado);

        produto.setNome(produtoDTO.getNome());
        produto.setPreco(produtoDTO.getPreco());

        LinhaCategoriaDTO linhaCategoriaDTO = linhaCategoriaService.findById(produtoDTO.getIdLinha());
        LinhaCategoria linhaCategoria = linhaCategoriaService.converterObjeto(linhaCategoriaDTO);
        produto.setLinhaCategoria(linhaCategoria);

        produto.setUnidadeCaixa(produtoDTO.getUnidadeCaixa());
        produto.setPesoUnidade(produtoDTO.getPesoUnidade());
        produto.setUnidadeDePeso(produtoDTO.getUnidadeDePeso());
        produto.setValidade(produtoDTO.getValidade());

        produto = this.iProdutoRepository.save(produto);
        return ProdutoDTO.of(produto);
    }

    public ProdutoDTO atualizar(ProdutoDTO produtoDTO, Long id){

        this.validarCamposTexto(produtoDTO);

        Optional<Produto> produtoExistenteOptional = this.iProdutoRepository.findById(id);

        if(produtoExistenteOptional.isPresent()){

            Produto produtoExistente = produtoExistenteOptional.get();

            LOGGER.info("Atualizando produto... id: [{}]", produtoExistente.getId());
            LOGGER.debug("Payload: {}", produtoDTO);
            LOGGER.debug("Produto existente: {}", produtoExistente);

            String codigoFinal = formatarCodigoProduto(produtoDTO.getCodigoProduto().toUpperCase());
            produtoExistente.setCodigoProduto(codigoFinal);

            produtoExistente.setNome(produtoDTO.getNome());
            produtoExistente.setPreco(produtoDTO.getPreco());

            LinhaCategoriaDTO linhaCategoriaDTO = linhaCategoriaService.findById(produtoDTO.getIdLinha());
            LinhaCategoria linhaCategoria = linhaCategoriaService.converterObjeto(linhaCategoriaDTO);
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

    public void deletar(Long id){

        LOGGER.info("Executando delete para produto de id: [{}]", id);

        if(iProdutoRepository.existsById(id)){
            this.iProdutoRepository.deleteById(id);
        }
        throw new IllegalArgumentException("Produto não cadastrado");
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

    public void validarCamposTexto(ProdutoDTO produtoDTO){

        LOGGER.info("Validando produto...");

        if(produtoDTO == null){
            throw new IllegalArgumentException("ProtudoDTO não deve ser nulo");
        }

        if(produtoDTO.getCodigoProduto() == null){
            throw new IllegalArgumentException("Código não deve ser nulo");
        }

        if(StringUtils.isEmpty(produtoDTO.getNome())){
            throw new IllegalArgumentException("Nome não deve ser nulo/vazio");
        }

        if(produtoDTO.getNome().length() > 200){
            throw new IllegalArgumentException("Nome deve conter até 200 caracteres");
        }

        if(produtoDTO.getPreco() == null){
            throw new IllegalArgumentException("Preco não deve ser nulo");
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

        if(StringUtils.isEmpty(produtoDTO.getUnidadeDePeso())){
            throw new IllegalArgumentException("Unidade de peso não deve ser nulo/vazio");
        }

        switch (produtoDTO.getUnidadeDePeso().toUpperCase()){
            case "G":
            break;

            case "KG":
            break;

            case "MG":
            break;

            default:
                throw new IllegalArgumentException("Unidade de medida errada! Insira 'mg', 'g' ou 'Kg'...");
        }

        if(produtoDTO.getValidade() == null){
            throw new IllegalArgumentException("Validade não deve ser nula");
        }
    }

    public boolean findByCodigoProduto(String codigo) {

        boolean valida;
        Optional<Produto> produtoOptional = this.iProdutoRepository.findByCodigoProduto(codigo);

        if(produtoOptional.isPresent()){
            valida = true;
            return valida;
        }else {
            valida = false;
            return valida;
        }
    }

    /** FORMATAÇÕES GERAL */
    public String formatarCodigoProduto(String codigo){

        String codigoFinal = StringUtils.leftPad(codigo, 10, "0");
        return codigoFinal;
    }

    /** CSV - EXPORTAR E IMPORTAR */
    public List<Produto> obterProdutos(){
        return this.iProdutoRepository.findAll();
    }

    public List<Produto> salvarProdutos(List<Produto> produtoList){
        return this.iProdutoRepository.saveAll(produtoList);
    }
}










