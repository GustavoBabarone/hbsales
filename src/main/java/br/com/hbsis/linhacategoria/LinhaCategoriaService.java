package br.com.hbsis.linhacategoria;

import br.com.hbsis.categoriaproduto.CategoriaProduto;
import br.com.hbsis.categoriaproduto.CategoriaProdutoDTO;
import br.com.hbsis.categoriaproduto.CategoriaProdutoService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LinhaCategoriaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LinhaCategoriaService.class);
    private final ILinhaCategoriaRepository iLinhaCategoriaRepository;
    private final CategoriaProdutoService categoriaProdutoService;

    @Autowired /** CONSTRUTOR */
    public LinhaCategoriaService(ILinhaCategoriaRepository iLinhaCategoriaRepository, CategoriaProdutoService categoriaProdutoService) {
        this.iLinhaCategoriaRepository = iLinhaCategoriaRepository;
        this.categoriaProdutoService = categoriaProdutoService;
    }

    /** MÉTODOS DE CRUD */
    public LinhaCategoriaDTO salvar(LinhaCategoriaDTO linhaCategoriaDTO){

        this.validarCamposTexto(linhaCategoriaDTO);

        LOGGER.info("Salvando linha de categoria...");
        LOGGER.debug("Linha de Categoria: {}", linhaCategoriaDTO);

        LinhaCategoria linhaCategoria = new LinhaCategoria();

        String codigo = linhaCategoriaDTO.getCodigoLinha();
        String codigoUpperCase = codigo.toUpperCase();
        String codigoProcessado = adicionarZerosEsquerdaCodigo(codigoUpperCase);
        linhaCategoria.setCodigoLinha(codigoProcessado);

        CategoriaProdutoDTO categoriaProdutoDTO = categoriaProdutoService.findById(linhaCategoriaDTO.getIdCategoria());
        CategoriaProduto categoriaProduto = categoriaProdutoService.converterObjeto(categoriaProdutoDTO);
        linhaCategoria.setCategoriaProduto(categoriaProduto);

        linhaCategoria.setNome(linhaCategoriaDTO.getNome());

        linhaCategoria = this.iLinhaCategoriaRepository.save(linhaCategoria);

        return LinhaCategoriaDTO.of(linhaCategoria);
    }

    public LinhaCategoriaDTO atualizar(LinhaCategoriaDTO linhaCategoriaDTO, Long id){

        Optional<LinhaCategoria> linhaCategoriaExistenteOptional = this.iLinhaCategoriaRepository.findById(id);

        if(linhaCategoriaExistenteOptional.isPresent()){

            LinhaCategoria linhaCategoriaExistente = linhaCategoriaExistenteOptional.get();

            LOGGER.info("Atualizando linha de categoria... id: [{}]", linhaCategoriaExistente.getId());
            LOGGER.debug("Payload: {}", linhaCategoriaDTO);
            LOGGER.debug("Linha de categoria existente: {}", linhaCategoriaExistente);

            String codigo = linhaCategoriaDTO.getCodigoLinha();
            String codigoUpperCase = codigo.toUpperCase();
            String codigoProcessado = adicionarZerosEsquerdaCodigo(codigoUpperCase);

            linhaCategoriaExistente.setCodigoLinha(codigoProcessado);

            CategoriaProdutoDTO categoriaProdutoDTO = categoriaProdutoService.findById(linhaCategoriaDTO.getIdCategoria());
            CategoriaProduto categoriaProduto = categoriaProdutoService.converterObjeto(categoriaProdutoDTO);
            linhaCategoriaExistente.setCategoriaProduto(categoriaProduto);

            linhaCategoriaExistente.setNome(linhaCategoriaDTO.getNome());

            linhaCategoriaExistente = this.iLinhaCategoriaRepository.save(linhaCategoriaExistente);

            return LinhaCategoriaDTO.of(linhaCategoriaExistente);
        }
        throw new IllegalArgumentException(String.format("Id %s não existe", id));
    }

    public void deletar(Long id){

        LOGGER.info("Executando exclusão para linha de categoria de id: [{}]", id);

        this.iLinhaCategoriaRepository.deleteById(id);

    }

    public LinhaCategoriaDTO findById(Long id){

        Optional<LinhaCategoria> linhaCategoriaOptional = this.iLinhaCategoriaRepository.findById(id);

        if(linhaCategoriaOptional.isPresent()){

            LinhaCategoria linhaCategoria = linhaCategoriaOptional.get();
            LinhaCategoriaDTO linhaCategoriaDTO = LinhaCategoriaDTO.of(linhaCategoria);
            return linhaCategoriaDTO;
        }

        String format = String.format("Id %s não existe", id);
        throw new IllegalArgumentException(format);
    }

    public void validarCamposTexto(LinhaCategoriaDTO linhaCategoriaDTO){

        LOGGER.info("Validando linha de categoria...");

        /* CONDICIONAIS DE VALIDAÇÃO */
        if(linhaCategoriaDTO == null){
            throw new IllegalArgumentException("LinhaCategoriaDTO não deve ser nulo.");
        }

        if(linhaCategoriaDTO.getCodigoLinha() == null){
            throw new IllegalArgumentException("Código da linha não deve ser nulo.");
        }

        if(linhaCategoriaDTO.getIdCategoria() == null){
            throw new IllegalArgumentException("Id da Categoria da linha não deve ser nulo.");
        }

        if(StringUtils.isEmpty(linhaCategoriaDTO.getNome())){
            throw new IllegalArgumentException("Nome não deve ser nulo/vazio.");
        }
    }

    public LinhaCategoriaDTO findByCodigoLinha(String codigoLinha) {

        Optional<LinhaCategoria> linhaCategoriaOptional = this.iLinhaCategoriaRepository.findByCodigoLinha(codigoLinha);

        if(linhaCategoriaOptional.isPresent()){

            LinhaCategoria linhaCategoria = linhaCategoriaOptional.get();
            LinhaCategoriaDTO linhaCategoriaDTO = LinhaCategoriaDTO.of(linhaCategoria);
            return linhaCategoriaDTO;
        }

        throw new IllegalArgumentException(String.format("Código %s não existe", codigoLinha));
    }

    public boolean findByCodigo(String codigoLinha) {

        boolean valida;
        Optional<LinhaCategoria> linhaCategoriaOptional = this.iLinhaCategoriaRepository.findByCodigoLinha(codigoLinha);

        if(linhaCategoriaOptional.isPresent()){
            valida = true;
            return valida;
        }else{
            valida = false;
            return valida;
        }
    }

    /** FORMATAÇÕES GERAL */
    public String adicionarZerosEsquerdaCodigo(String codigo){

        String codigoProcessado = StringUtils.leftPad(codigo, 10, "0");
        return codigoProcessado;
    }

    public LinhaCategoria converterObjeto(LinhaCategoriaDTO linhaCategoriaDTO){

        LinhaCategoria linhaCategoria = new LinhaCategoria();
        linhaCategoria.setId(linhaCategoriaDTO.getId());
        return linhaCategoria;
    }

    /** CSV - EXPORTAR E IMPORTAR */
    public List<LinhaCategoria> obterLinhas(){
        return this.iLinhaCategoriaRepository.findAll();
    }

    public List<LinhaCategoria> salvarLinhas(List<LinhaCategoria> linhaCategoriaList){
        return this.iLinhaCategoriaRepository.saveAll(linhaCategoriaList);
    }
}






