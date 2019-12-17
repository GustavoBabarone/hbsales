package br.com.hbsis.linhaCategoria;

import br.com.hbsis.categoriaProduto.CategoriaProduto;
import br.com.hbsis.categoriaProduto.CategoriaProdutoDTO;
import br.com.hbsis.categoriaProduto.CategoriaProdutoService;
import br.com.hbsis.categoriaProduto.ICategoriaProdutoRepository;
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

/**
 * CLASSE RESPONSÁVEL PELO PROCESSAMENTO DA REGRA DE NEGÓCIO
 */
@Service
public class LinhaCategoriaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LinhaCategoriaService.class);

    private final ILinhaCategoriaRepository iLinhaCategoriaRepository;
    private final CategoriaProdutoService categoriaProdutoService;
    private final ICategoriaProdutoRepository categoriaProdutoRepository;

    /* CONSTRUTOR */
    @Autowired
    public LinhaCategoriaService(ILinhaCategoriaRepository iLinhaCategoriaRepository, CategoriaProdutoService categoriaProdutoService, ICategoriaProdutoRepository categoriaProdutoRepository) {
        this.iLinhaCategoriaRepository = iLinhaCategoriaRepository;
        this.categoriaProdutoService = categoriaProdutoService;
        this.categoriaProdutoRepository = categoriaProdutoRepository;
    }

    // MÉTODO PARA SALVAR A LINHA DE CATEGORIA
    public LinhaCategoriaDTO save(LinhaCategoriaDTO linhaCategoriaDTO){

        this.validate(linhaCategoriaDTO);

        LOGGER.info("Salvando linha de categoria...");
        LOGGER.debug("Linha de Categoria: {}", linhaCategoriaDTO);

        // INSTANCIAR OBJETOS
        LinhaCategoria linhaCategoria = new LinhaCategoria();

        String codigo = linhaCategoriaDTO.getCodigoLinha();
        String codigoUpperCase = codigo.toUpperCase();
        String codigoProcessado = codigoZerosEsquerda(codigoUpperCase);
        linhaCategoria.setCodigoLinha(codigoProcessado);

        /* CONVERTER CategoriaDTO PARA Categoria */
        CategoriaProdutoDTO categoriaProdutoDTO = categoriaProdutoService.findById(linhaCategoriaDTO.getIdCategoria());
        CategoriaProduto categoriaProduto = conversor(categoriaProdutoDTO);
        /* TERMINO DA CONVERSÃO */

        linhaCategoria.setCategoriaProduto(categoriaProduto);
        linhaCategoria.setNome(linhaCategoriaDTO.getNome());

        linhaCategoria = this.iLinhaCategoriaRepository.save(linhaCategoria);

        return LinhaCategoriaDTO.of(linhaCategoria);
    }

    // ADICIONAR ZEROS A ESQUERDA
    public String codigoZerosEsquerda(String codigo){

        String codigoProcessado = StringUtils.leftPad(codigo, 10, "0");

        return codigoProcessado;
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

            String codigo = linhaCategoriaDTO.getCodigoLinha();
            String codigoUpperCase = codigo.toUpperCase();
            String codigoProcessado = codigoZerosEsquerda(codigoUpperCase);

            linhaCategoriaExistente.setCodigoLinha(codigoProcessado);

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
        String[] cabecalhoCSV = {"codigo_linha", "nome_linha", "codigo_categoria", "nome_categoria"};

        // ESCREVER O CABEÇALHO
        icsvWriter.writeNext(cabecalhoCSV);

        // LAÇO PARA PREENCHER AS INFORMAÇÕES DO BANCO
        for(LinhaCategoria rows : iLinhaCategoriaRepository.findAll()){

            icsvWriter.writeNext(new String[]{

                    // LINHA COM AS INFOS
                    rows.getCodigoLinha(),
                    rows.getNome(),
                    rows.getCategoriaProduto().getCodigoCategoria(),
                    rows.getCategoriaProduto().getNome()
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

            boolean valida = findByCodigo(vetor[0]);

            if(valida == false){

            linhaCategoria.setCodigoLinha(vetor[0]);

            /* CONVERTER VARIÁVEL TIPO LinhaCategoriaDTO PARA LinhaCategoria */
            CategoriaProduto categoriaProduto = new CategoriaProduto();
            CategoriaProdutoDTO categoriaProdutoDTO = this.categoriaProdutoService.findByCodigoCategoria(vetor[2]);
            categoriaProduto = conversor(categoriaProdutoDTO);
            /* FIM DA CONVERSÃO */

            linhaCategoria.setCategoriaProduto(categoriaProduto);
            linhaCategoria.setNome(vetor[1]);

            // ADICIONAR OBJ LinhaCategoria NO ARRAY LIST
            linhaCategoriaList.add(linhaCategoria);

            }else if(valida == true){
                LOGGER.info("Linha já existente no banco de dados...");
            }
        }
            LOGGER.info("Finalizando importação...");

            return iLinhaCategoriaRepository.saveAll(linhaCategoriaList);
    }

    // OBTER DADOS VIA CÓDIGO DA LINHA DE CATEGORIA
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
}






