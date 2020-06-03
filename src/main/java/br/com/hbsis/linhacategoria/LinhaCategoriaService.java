package br.com.hbsis.linhacategoria;

import br.com.hbsis.categoriaproduto.CategoriaProduto;
import br.com.hbsis.categoriaproduto.CategoriaProdutoDTO;
import br.com.hbsis.categoriaproduto.CategoriaProdutoService;
import br.com.hbsis.csv.CSV;
import com.opencsv.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LinhaCategoriaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LinhaCategoriaService.class);
    private final ILinhaCategoriaRepository iLinhaCategoriaRepository;
    private final CategoriaProdutoService categoriaProdutoService;
    private final CSV csv;

    @Autowired
    public LinhaCategoriaService(ILinhaCategoriaRepository iLinhaCategoriaRepository, CategoriaProdutoService categoriaProdutoService, CSV csv) {
        this.iLinhaCategoriaRepository = iLinhaCategoriaRepository;
        this.categoriaProdutoService = categoriaProdutoService;
        this.csv = csv;
    }

    public LinhaCategoriaDTO salvar(LinhaCategoriaDTO linhaCategoriaDTO){

        this.validarLinhaCategoria(linhaCategoriaDTO);

        LOGGER.info("Executando save de linha de categoria");

        CategoriaProdutoDTO categoriaProdutoDTO = categoriaProdutoService.findById(linhaCategoriaDTO.getIdCategoria());

        LinhaCategoria linhaCategoria = new LinhaCategoria(
                gerarCodigo(linhaCategoriaDTO.getCodigoLinha()),
                new CategoriaProduto(categoriaProdutoDTO.getId()),
                linhaCategoriaDTO.getNome()
        );

        linhaCategoria = this.iLinhaCategoriaRepository.save(linhaCategoria);
        return LinhaCategoriaDTO.of(linhaCategoria);
    }

    public LinhaCategoriaDTO atualizar(LinhaCategoriaDTO linhaCategoriaDTO, Long id){

        this.validarLinhaCategoria(linhaCategoriaDTO);

        Optional<LinhaCategoria> linhaCategoriaExistenteOptional = this.iLinhaCategoriaRepository.findById(id);

        if(linhaCategoriaExistenteOptional.isPresent()){

            LinhaCategoria linhaCategoriaExistente = linhaCategoriaExistenteOptional.get();

            LOGGER.info("Executando update de linha de categoria de id: [{}]", linhaCategoriaExistente.getId());

            CategoriaProdutoDTO categoriaProdutoDTO = categoriaProdutoService.findById(linhaCategoriaDTO.getIdCategoria());
            String codigo = gerarCodigo(linhaCategoriaDTO.getCodigoLinha());

            linhaCategoriaExistente.setCodigoLinha(codigo);
            linhaCategoriaExistente.setCategoriaProduto(new CategoriaProduto(categoriaProdutoDTO.getId()));
            linhaCategoriaExistente.setNome(linhaCategoriaDTO.getNome());

            linhaCategoriaExistente = this.iLinhaCategoriaRepository.save(linhaCategoriaExistente);
            return LinhaCategoriaDTO.of(linhaCategoriaExistente);
        }

        throw new IllegalArgumentException(String.format("Linha de categoria de id [%s] não encontrada", id));
    }

    public void deletar(Long id){

        LOGGER.info("Executando delete de linha de categoria de id: [{}]", id);

        if(iLinhaCategoriaRepository.existsById(id)){
            this.iLinhaCategoriaRepository.deleteById(id);
        }else {
            throw new IllegalArgumentException(String.format("Linha de categoria de id [%s] não cadastrada", id));
        }
    }

    public LinhaCategoriaDTO findById(Long id){

        LOGGER.info("Executando findById de linha de categoria de id: [{}]", id);

        Optional<LinhaCategoria> linhaCategoriaOptional = this.iLinhaCategoriaRepository.findById(id);

        if(linhaCategoriaOptional.isPresent()){
            return LinhaCategoriaDTO.of(linhaCategoriaOptional.get());
        }

        throw new IllegalArgumentException(String.format("Linha de categoria de id %s não encontrada", id));
    }

    public void validarLinhaCategoria(LinhaCategoriaDTO linhaCategoriaDTO){

        LOGGER.info("Validando linha de categoria");

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

        if(linhaCategoriaDTO.getNome().length() > 50 ){
            throw new IllegalArgumentException("Nome deve conter até 50 caracteres");
        }
    }

    public LinhaCategoriaDTO findByCodigoLinha(String codigo) {

        LOGGER.info("Executando findByCodigo de linha de categoria de codigo: [{}]", codigo);

        Optional<LinhaCategoria> linhaCategoriaOptional = this.iLinhaCategoriaRepository.findByCodigoLinha(codigo);

        if(linhaCategoriaOptional.isPresent()){
            return LinhaCategoriaDTO.of(linhaCategoriaOptional.get());
        }

        throw new IllegalArgumentException(String.format("Linha de categoria de codigo [%s] não encontrada", codigo));
    }

    public boolean existsByCodigo(String codigoLinha) {
        return this.iLinhaCategoriaRepository.existsByCodigoLinha(codigoLinha);
    }

    public String gerarCodigo(String codigo){
        codigo = codigo.toUpperCase();
        return StringUtils.leftPad(codigo, 10, "0");
    }

    public void exportar(HttpServletResponse response) throws  Exception {

        String arquivoCSV = "linhaCategoria.csv";
        String[] cabecalhoCSV = {"codigo_linha", "nome_linha", "codigo_categoria", "nome_categoria"};
        ICSVWriter icsvWriter = csv.padraoExportarCsv(response, arquivoCSV, cabecalhoCSV);

        for(LinhaCategoria rows : iLinhaCategoriaRepository.findAll()){

            icsvWriter.writeNext(new String[]{
                    rows.getCodigoLinha(),
                    rows.getNome(),
                    rows.getCategoriaProduto().getCodigoCategoria(),
                    rows.getCategoriaProduto().getNome()
            });
        }
        LOGGER.info("Finalizando exportação de linha...");
    }

    public List<LinhaCategoria> importar(MultipartFile file) throws Exception {

        List<String[]> row = csv.padraoImportarCsv(file);
        List<LinhaCategoria> linhaCategoriaList = new ArrayList<>();

        for(String[] linha : row){

            String[] vetor = linha[0].replaceAll("\"", "").split(";");

            if(existsByCodigo(vetor[0])){

                CategoriaProdutoDTO categoriaProdutoDTO = this.categoriaProdutoService.findByCodigoCategoria(vetor[2]);

                LinhaCategoria linhaCategoria = new LinhaCategoria(
                        vetor[0],
                        new CategoriaProduto(categoriaProdutoDTO.getId()),
                        vetor[1]
                );

                linhaCategoriaList.add(linhaCategoria);

            }else {
                LOGGER.info("Linha já existente no banco de dados...");
            }
        }
        LOGGER.info("Finalizando importação de linha...");
        return iLinhaCategoriaRepository.saveAll(linhaCategoriaList);
    }

    /** IMPORTAR POR FORNECEDOR */
    public LinhaCategoria executarSaveNaRepository(LinhaCategoria linhaCategoria){
        return this.iLinhaCategoriaRepository.save(linhaCategoria);
    }

    public Optional<LinhaCategoria> findByCodigoLinhaImportProdutoPorFornecedor(String codigoLinha){
        return this.iLinhaCategoriaRepository.findByCodigoLinha(codigoLinha);
    }
}
