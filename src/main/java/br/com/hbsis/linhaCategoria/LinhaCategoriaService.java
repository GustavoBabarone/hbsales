package br.com.hbsis.linhaCategoria;

import br.com.hbsis.categoriaProduto.CategoriaProduto;
import br.com.hbsis.categoriaProduto.CategoriaProdutoDTO;
import br.com.hbsis.categoriaProduto.CategoriaProdutoService;
import com.opencsv.*;
import javafx.print.Printer;
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

/**
 * CLASSE RESPONSÁVEL PELO PROCESSAMENTO DA REGRA DE NEGÓCIO
 */
@Service
public class LinhaCategoriaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LinhaCategoriaService.class);

    private final ILinhaCategoriaRepository iLinhaCategoriaRepository;
    private final CategoriaProdutoService categoriaProdutoService;

    /* CONSTRUTOR */
    @Autowired
    public LinhaCategoriaService(ILinhaCategoriaRepository iLinhaCategoriaRepository, CategoriaProdutoService categoriaProdutoService) {
        this.iLinhaCategoriaRepository = iLinhaCategoriaRepository;
        this.categoriaProdutoService = categoriaProdutoService;
    }

    // MÉTODO PARA SALVAR A LINHA DE CATEGORIA
    public LinhaCategoriaDTO save(LinhaCategoriaDTO linhaCategoriaDTO){

        this.validate(linhaCategoriaDTO);

        LOGGER.info("Salvando linha de categoria...");
        LOGGER.debug("Linha de Categoria: {}", linhaCategoriaDTO);

        // INSTANCIAR OBJETOS
        LinhaCategoria linhaCategoria = new LinhaCategoria();
        linhaCategoria.setCodigoLinha(linhaCategoriaDTO.getCodigoLinha());

        /* CONVERTER CategoriaDTO PARA Categoria */
        CategoriaProdutoDTO categoriaProdutoDTO = categoriaProdutoService.findById(linhaCategoriaDTO.getId());
        CategoriaProduto categoriaProduto = conversor(categoriaProdutoDTO);
        /* TERMINO DA CONVERSÃO */

        linhaCategoria.setCategoriaProduto(categoriaProduto);
        linhaCategoria.setNome(linhaCategoriaDTO.getNome());

        linhaCategoria = this.iLinhaCategoriaRepository.save(linhaCategoria);

        return LinhaCategoriaDTO.of(linhaCategoria);
    }

    // MÉTODO DE VALIDAR OBJ DE LINHA DE CATEGORIA
    public void validate(LinhaCategoriaDTO linhaCategoriaDTO){

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

    // MÉTODO DE CONVERSÃO DE OBJ CategoriaDTO EM Categoria
    public CategoriaProduto conversor(CategoriaProdutoDTO categoriaProdutoDTO){

        // OBJETO DA CLASSE Categoria
        CategoriaProduto categoriaProduto = new CategoriaProduto();

        // ATRIBUIR VALOR DO 'ID' PARA Categoria
        categoriaProduto.setId(categoriaProdutoDTO.getId());

        // RETORNO COM A VARIÁVEL PROCESSADA
        return categoriaProduto;
    }

    // MÉTODO DE OBTER INFOS PELO 'ID'
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

    // MÉTODO DE ALTERAÇÃO DO OBJETO
    public LinhaCategoriaDTO alterar(LinhaCategoriaDTO linhaCategoriaDTO, Long id){

        Optional<LinhaCategoria> linhaCategoriaExistenteOptional = this.iLinhaCategoriaRepository.findById(id);

        if(linhaCategoriaExistenteOptional.isPresent()){

            LinhaCategoria linhaCategoriaExistente = linhaCategoriaExistenteOptional.get();

            LOGGER.info("Atualizando linha de categoria... id: [{}]", linhaCategoriaExistente.getId());
            LOGGER.debug("Payload: {}", linhaCategoriaDTO);
            LOGGER.debug("Linha de categoria existente: {}", linhaCategoriaExistente);

            linhaCategoriaExistente.setCodigoLinha(linhaCategoriaDTO.getCodigoLinha());

            /* CONVERSÃO */
            CategoriaProdutoDTO categoriaProdutoDTO = categoriaProdutoService.findById(linhaCategoriaDTO.getIdCategoria());
            CategoriaProduto categoriaProduto = conversor(categoriaProdutoDTO);
            /* FIM DA CONVERSÃO */

            linhaCategoriaExistente.setCategoriaProduto(categoriaProduto);
            linhaCategoriaExistente.setNome(linhaCategoriaDTO.getNome());

            linhaCategoriaExistente = this.iLinhaCategoriaRepository.save(linhaCategoriaExistente);

            return LinhaCategoriaDTO.of(linhaCategoriaExistente);
        }

        throw new IllegalArgumentException(String.format("Id %s não existe", id));
    }

    // MÉTODO DE EXCLUSÃO
    public void excluir(Long id){

        LOGGER.info("Executando exclusão para linha de categoria de id: [{}]", id);

        this.iLinhaCategoriaRepository.deleteById(id);

    }

    // EXPORTAR PARA CSV - ATIVIDADE 6
    public void findAll(HttpServletResponse response) throws  Exception {

        // VARIÁVEL COM O NOME DO ARQUIVO EXCEL
        String arquivoCSV = "linhaCategoria.csv";

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + arquivoCSV + "\"");

        PrintWriter writer = response.getWriter();

        ICSVWriter icsvWriter = new CSVWriterBuilder(writer).withSeparator(';').withEscapeChar(CSVWriter.DEFAULT_ESCAPE_CHARACTER).withLineEnd(CSVWriter.DEFAULT_LINE_END).build();

        // VEOTR COM OS NOMES DAS COLUNAS
        String[] cabecalhoCSV = {"id", "codigo_linha", "id_categoria", "nome"};

        // ESCREVER O CABEÇALHO
        icsvWriter.writeNext(cabecalhoCSV);

        // LAÇO PARA PREENCHER AS INFORMAÇÕES DO BANCO
        for(LinhaCategoria rows : iLinhaCategoriaRepository.findAll()){

            icsvWriter.writeNext(new String[]{

                    // LINHA COM AS INFOS
                    rows.getId().toString(),
                    rows.getCodigoLinha().toString(),
                    rows.getCategoriaProduto().getId().toString(),
                    rows.getNome()
            });
        }
    }

    // IMPORTAR DE UM CSV - ATIVIDADE 7
    public List<LinhaCategoria> obterTudo(MultipartFile file) throws Exception {

        InputStreamReader inputStreamReader = new InputStreamReader(file.getInputStream());

        CSVReader leitor = new CSVReaderBuilder(inputStreamReader).withSkipLines(1).build();

        List<String[]> row = leitor.readAll();
        List<LinhaCategoria> linhaCategoriaList = new ArrayList<>();

        for(String[] linha : row){

            String[] vetor = linha[0].replaceAll("\"", "").split(";");

            // OBJETOS DAS CLASSES LinhaCategoria e CategoriaProduto
            LinhaCategoria linhaCategoria = new LinhaCategoria();
            linhaCategoria.setId(Long.parseLong(vetor[0]));
            linhaCategoria.setCodigoLinha(Long.parseLong(vetor[1]));

            /* CONVERTER VARIÁVEL TIPO LinhaCategoriaDTO PARA LinhaCategoria */
            CategoriaProduto categoriaProduto = new CategoriaProduto();
            CategoriaProdutoDTO categoriaProdutoDTO = categoriaProdutoService.findById(Long.parseLong(vetor[2]));
            categoriaProduto = conversor(categoriaProdutoDTO);
            /* FIM DA CONVERSÃO */

            linhaCategoria.setCategoriaProduto(categoriaProduto);
            linhaCategoria.setNome(vetor[3]);

            // ADICIONAR OBJ LinhaCategoria NO ARRAY LIST
            linhaCategoriaList.add(linhaCategoria);
        }

        LOGGER.info("Finalizando importação...");

        return iLinhaCategoriaRepository.saveAll(linhaCategoriaList);
    }
}






