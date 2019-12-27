package br.com.hbsis.categoriaproduto;

import br.com.hbsis.fornecedor.Fornecedor;
import br.com.hbsis.fornecedor.FornecedorDTO;
import br.com.hbsis.fornecedor.FornecedorService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaProdutoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoriaProdutoService.class);
    private final ICategoriaProdutoRepository iCategoriaProdutoRepository;
    private final FornecedorService fornecedorService;

    @Autowired /** CONTRUTOR */
    public CategoriaProdutoService(ICategoriaProdutoRepository iCategoriaProdutoRepository, FornecedorService fornecedorService) {
        this.iCategoriaProdutoRepository = iCategoriaProdutoRepository;
        this.fornecedorService = fornecedorService;
    }

    /** MÉTODOS DE CRUD */
    public CategoriaProdutoDTO salvar(CategoriaProdutoDTO categoriaProdutoDTO) {

        this.validarCamposTexto(categoriaProdutoDTO);

        LOGGER.info("Salvando categoria");
        LOGGER.debug("Categoria: {}", categoriaProdutoDTO);

        CategoriaProduto categoriaProduto = new CategoriaProduto();

        FornecedorDTO fornecedorDTO = fornecedorService.findById(categoriaProdutoDTO.getIdFornecedor());
        Fornecedor fornecedor = fornecedorService.converterObjeto(fornecedorDTO);
        categoriaProduto.setFornecedor(fornecedor);

        String codigoFinal = formatarCodigoCategoria(categoriaProdutoDTO.getCodigoCategoria().toUpperCase(), fornecedorDTO.getCnpj());
        categoriaProduto.setCodigoCategoria(codigoFinal);

        categoriaProduto.setNome(categoriaProdutoDTO.getNome());

        categoriaProduto = this.iCategoriaProdutoRepository.save(categoriaProduto);
        return CategoriaProdutoDTO.of(categoriaProduto);
    }

    public CategoriaProdutoDTO atualizar(CategoriaProdutoDTO categoriaProdutoDTO, Long id) {

        this.validarCamposTexto(categoriaProdutoDTO);

        Optional<CategoriaProduto> categoriaProdutoExistenteOptional = this.iCategoriaProdutoRepository.findById(id);

        if (categoriaProdutoExistenteOptional.isPresent()) {

            CategoriaProduto categoriaProdutoExistente = categoriaProdutoExistenteOptional.get();

            LOGGER.info("Atualizando categoria... id: [{}]", categoriaProdutoExistente.getId());
            LOGGER.debug("Payload: {}", categoriaProdutoDTO);
            LOGGER.debug("Categoria Existente: {}", categoriaProdutoExistente);

            FornecedorDTO fornecedorDTO = fornecedorService.findById(categoriaProdutoDTO.getIdFornecedor());

            String codigoFinal = formatarCodigoCategoria(categoriaProdutoDTO.getCodigoCategoria().toUpperCase(), fornecedorDTO.getCnpj());
            categoriaProdutoExistente.setCodigoCategoria(codigoFinal);

            Fornecedor fornecedor = fornecedorService.converterObjeto(fornecedorDTO);
            categoriaProdutoExistente.setFornecedor(fornecedor);

            categoriaProdutoExistente.setNome(categoriaProdutoDTO.getNome());

            categoriaProdutoExistente = this.iCategoriaProdutoRepository.save(categoriaProdutoExistente);

            return CategoriaProdutoDTO.of(categoriaProdutoExistente);
        }
        throw new IllegalArgumentException(String.format("Id %s não existe", id));
    }

    public void deletar(Long id) {

        LOGGER.info("Executando delete para categoria de id: [{}]", id);

        if(iCategoriaProdutoRepository.existsById(id)){
            this.iCategoriaProdutoRepository.deleteById(id);
        }else {
            throw new IllegalArgumentException("Categoria não cadastrada");
        }

    }

    public CategoriaProdutoDTO findById(Long id) {

        Optional<CategoriaProduto> categoriaProdutoOptional = this.iCategoriaProdutoRepository.findById(id);

        if (categoriaProdutoOptional.isPresent()) {

            CategoriaProduto categoriaProduto = categoriaProdutoOptional.get();
            CategoriaProdutoDTO categoriaProdutoDTO = CategoriaProdutoDTO.of(categoriaProduto);
            return categoriaProdutoDTO;
        }
        String format = String.format("Id %s não existe", id);
        throw new IllegalArgumentException(format);
    }

    private void validarCamposTexto(CategoriaProdutoDTO categoriaProdutoDTO) {

        LOGGER.info("Validando categoria...");

        if(categoriaProdutoDTO == null){
            throw new IllegalArgumentException("CategoriaProdutoDTO não deve ser nulo");
        }

        if (StringUtils.isEmpty(categoriaProdutoDTO.getCodigoCategoria())) {
            throw new IllegalArgumentException("Código não deve ser nulo/vazio");
        }

        if (categoriaProdutoDTO.getCodigoCategoria().length() > 3) {
            throw new IllegalArgumentException("Código deve conter até 3 digitos");
        }

        if (categoriaProdutoDTO.getIdFornecedor() == null) {
            throw new IllegalArgumentException("Id do fornecedor não deve ser nulo");
        }

        if (StringUtils.isEmpty(categoriaProdutoDTO.getNome())) {
            throw new IllegalArgumentException("Nome não deve ser nulo/vazio");
        }

        if (categoriaProdutoDTO.getNome().length() > 50) {
            throw new IllegalArgumentException("Nome deve conter até 50 caracteres");
        }
    }

    public CategoriaProdutoDTO findByCodigoCategoria(String codigo) {

        Optional<CategoriaProduto> categoriaProdutoOptional = this.iCategoriaProdutoRepository.findByCodigoCategoria(codigo);

        if (categoriaProdutoOptional.isPresent()) {

            CategoriaProduto categoriaProduto = categoriaProdutoOptional.get();
            CategoriaProdutoDTO categoriaProdutoDTO = CategoriaProdutoDTO.of(categoriaProduto);
            return categoriaProdutoDTO;
        }
        String format = String.format("Código %s não existe", codigo);
        throw new IllegalArgumentException(format);
    }

    public boolean findByCodigo(String codigo) {

        boolean valida;
        Optional<CategoriaProduto> categoriaProdutoOptional = this.iCategoriaProdutoRepository.findByCodigoCategoria(codigo);

        if (categoriaProdutoOptional.isPresent()) {
            valida = true;
            return valida;
        }else{
            valida = false;
            return valida;
        }
    }

    /** FORMATAÇÕES GERAL */
    public String formatarCodigoCategoria(String codigoInformado, String cnpjFornecedor){

        String codigoZerosEsquerda = StringUtils.leftPad(codigoInformado, 3, "0");
        String ultimosDigitosCnpj = cnpjFornecedor.substring(cnpjFornecedor.length() - 4);
        String codigoFinal = "CAT"+ultimosDigitosCnpj+codigoZerosEsquerda;
        return codigoFinal;
    }

    public CategoriaProduto converterObjeto(CategoriaProdutoDTO categoriaProdutoDTO){

        CategoriaProduto categoriaProduto = new CategoriaProduto();
        categoriaProduto.setId(categoriaProdutoDTO.getId());
        return categoriaProduto;
    }

    /** CSV - EXPORTAR E IMPORTAR */
    public List<CategoriaProduto> obterCategorias(){
        return this.iCategoriaProdutoRepository.findAll();
    }

    public List<CategoriaProduto> salvarCategorias(List<CategoriaProduto> categoriaProdutoList){
        return this.iCategoriaProdutoRepository.saveAll(categoriaProdutoList);
    }
}









