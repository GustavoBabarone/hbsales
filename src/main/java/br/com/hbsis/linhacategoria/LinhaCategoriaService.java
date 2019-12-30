package br.com.hbsis.linhacategoria;

import br.com.hbsis.categoriaproduto.CategoriaProduto;
import br.com.hbsis.categoriaproduto.CategoriaProdutoDTO;
import br.com.hbsis.categoriaproduto.CategoriaProdutoService;
import com.opencsv.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
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
        String codigo = formatarCodigoLinha(linhaCategoriaDTO.getCodigoLinha().toUpperCase());
        linhaCategoria.setCodigoLinha(codigo);

        CategoriaProdutoDTO categoriaProdutoDTO = categoriaProdutoService.findById(linhaCategoriaDTO.getIdCategoria());
        CategoriaProduto categoriaProduto = categoriaProdutoService.converterObjeto(categoriaProdutoDTO);
        linhaCategoria.setCategoriaProduto(categoriaProduto);

        linhaCategoria.setNome(linhaCategoriaDTO.getNome());

        linhaCategoria = this.iLinhaCategoriaRepository.save(linhaCategoria);
        return LinhaCategoriaDTO.of(linhaCategoria);
    }

    public LinhaCategoriaDTO atualizar(LinhaCategoriaDTO linhaCategoriaDTO, Long id){

        this.validarCamposTexto(linhaCategoriaDTO);

        Optional<LinhaCategoria> linhaCategoriaExistenteOptional = this.iLinhaCategoriaRepository.findById(id);

        if(linhaCategoriaExistenteOptional.isPresent()){

            LinhaCategoria linhaCategoriaExistente = linhaCategoriaExistenteOptional.get();

            LOGGER.info("Atualizando linha de categoria... id: [{}]", linhaCategoriaExistente.getId());
            LOGGER.debug("Payload: {}", linhaCategoriaDTO);
            LOGGER.debug("Linha de categoria existente: {}", linhaCategoriaExistente);

            String codigoFinal = formatarCodigoLinha(linhaCategoriaDTO.getCodigoLinha().toUpperCase());
            linhaCategoriaExistente.setCodigoLinha(codigoFinal);

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

        if(iLinhaCategoriaRepository.existsById(id)){
            this.iLinhaCategoriaRepository.deleteById(id);
        }else {
            throw new IllegalArgumentException("Linha de categoria não cadastrada");
        }
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
    public String formatarCodigoLinha(String codigo){

        String codigoFinal = StringUtils.leftPad(codigo, 10, "0");
        return codigoFinal;
    }

    public LinhaCategoria converterObjeto(LinhaCategoriaDTO linhaCategoriaDTO){

        LinhaCategoria linhaCategoria = new LinhaCategoria();
        linhaCategoria.setId(linhaCategoriaDTO.getId());
        return linhaCategoria;
    }

    /** CSV - EXPORTAR E IMPORTAR */
    public void exportarLinha(HttpServletResponse response) throws  Exception {

        String arquivoCSV = "linhaCategoria.csv";
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + arquivoCSV + "\"");

        PrintWriter writer = response.getWriter();
        ICSVWriter icsvWriter = new CSVWriterBuilder(writer).withSeparator(';').withEscapeChar(CSVWriter.DEFAULT_ESCAPE_CHARACTER).withLineEnd(CSVWriter.DEFAULT_LINE_END).build();

        String[] cabecalhoCSV = {"codigo_linha", "nome_linha", "codigo_categoria", "nome_categoria"};
        icsvWriter.writeNext(cabecalhoCSV);

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

    public List<LinhaCategoria> importarLinha(MultipartFile file) throws Exception {

        InputStreamReader inputStreamReader = new InputStreamReader(file.getInputStream());
        CSVReader leitor = new CSVReaderBuilder(inputStreamReader).withSkipLines(1).build();

        List<String[]> row = leitor.readAll();
        List<LinhaCategoria> linhaCategoriaList = new ArrayList<>();

        for(String[] linha : row){

            String[] vetor = linha[0].replaceAll("\"", "").split(";");

            LinhaCategoria linhaCategoria = new LinhaCategoria();
            boolean valida = findByCodigo(vetor[0]);

            if(valida == false){

                linhaCategoria.setCodigoLinha(vetor[0]);
                linhaCategoria.setNome(vetor[1]);

                CategoriaProdutoDTO categoriaProdutoDTO = this.categoriaProdutoService.findByCodigoCategoria(vetor[2]);
                CategoriaProduto categoriaProduto = categoriaProdutoService.converterObjeto(categoriaProdutoDTO);
                linhaCategoria.setCategoriaProduto(categoriaProduto);

                linhaCategoriaList.add(linhaCategoria);

            }else if(valida == true){
                LOGGER.info("Linha já existente no banco de dados...");
            }
        }
        LOGGER.info("Finalizando importação de linha...");
        return iLinhaCategoriaRepository.saveAll(linhaCategoriaList);
    }

    /** CSV - IMPORTAR POR FORNECEDOR - ATIVIDADE 11 */
    public LinhaCategoria executarSaveNaRepository(LinhaCategoria linhaCategoria){
        return this.iLinhaCategoriaRepository.save(linhaCategoria);
    }

    public Optional<LinhaCategoria> findByCodigoLinhaImportProdutoPorFornecedor(String codigoLinha){
        return this.iLinhaCategoriaRepository.findByCodigoLinha(codigoLinha);
    }
}
