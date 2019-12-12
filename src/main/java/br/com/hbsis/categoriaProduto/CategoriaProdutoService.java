package br.com.hbsis.categoriaProduto;

import br.com.hbsis.fornecedor.Fornecedor;
import br.com.hbsis.fornecedor.FornecedorDTO;
import br.com.hbsis.fornecedor.FornecedorService;
import br.com.hbsis.fornecedor.IFornecedorRepository;
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * CLASSE RESPONSÁVEL PELO PROCESSAMENTO DA REGRA DE NEGÓCIO
 */
@Service
public class CategoriaProdutoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoriaProdutoService.class);

    private final ICategoriaProdutoRepository iCategoriaProdutoRepository;
    private final FornecedorService fornecedorService;
    private final IFornecedorRepository iFornecedorRepository;

    /* CONTRUTOR */
    @Autowired
    public CategoriaProdutoService(ICategoriaProdutoRepository iCategoriaProdutoRepository, FornecedorService fornecedorService, IFornecedorRepository iFornecedorRepository) {
        this.iCategoriaProdutoRepository = iCategoriaProdutoRepository;
        this.fornecedorService = fornecedorService;
        this.iFornecedorRepository = iFornecedorRepository;
    }

    // MÉTODO DE CADASTRAMENTO DA CATEGORIA
    public CategoriaProdutoDTO save(CategoriaProdutoDTO categoriaProdutoDTO) {

        // EXECUTAR MÉTODO DE VALIDAÇÃO
        this.validate(categoriaProdutoDTO);

        LOGGER.info("Salvando categoria");
        LOGGER.debug("Categoria: {}", categoriaProdutoDTO);

        // INSTANCIAR OBJETOS
        CategoriaProduto categoriaProduto = new CategoriaProduto();

        //  OBTER fornecedorDTO PELO 'ID' ESPECÍFICO
        FornecedorDTO fornecedorDTO = fornecedorService.findById(categoriaProdutoDTO.getIdFornecedor());

            // OBTER O CNPJ DO FORNECEDOR
            String cnpj = fornecedorDTO.getCnpj();

            // OBTER SOMENTE OS 4 ULTIMOS DIGITOS DO CNPJ
            String cnpjProcessado = ultimoDigitoCnpj(cnpj);

            // OBTER CODIGO INFORMADO PELO FORNECEDOR
            String codigo = categoriaProdutoDTO.getCodigo();

            String codigoComZero = validarCodigo(codigo);

            // CONCATENAR CODIGO FINAL
            String codigoProcessado = "CAT"+cnpjProcessado+codigoComZero;

            // DEFINIR O CÓDIGO CONCATENADO
            categoriaProduto.setCodigo(codigoProcessado);

        // EXECUTANDO MÉTODO DE CONVERSÃO
        Fornecedor fornecedor = conversor(fornecedorDTO);

        // PASSAR PARÂMETRO CONVERTIDO
        categoriaProduto.setFornecedor(fornecedor);
        categoriaProduto.setNome(categoriaProdutoDTO.getNome());

        categoriaProduto = this.iCategoriaProdutoRepository.save(categoriaProduto);

        return CategoriaProdutoDTO.of(categoriaProduto);

    }

    // VALIDAR CODIGO INFORMADO PELO FORNECEDOR
    public String validarCodigo(String codigo){

        String codigoComZero = "";

        if(codigo.length() == 3 ){
            codigoComZero = codigo;
        }

        if(codigo.length() == 2 ){
            codigoComZero = "0"+codigo;
        }

        if(codigo.length() == 1 ){
            codigoComZero = "00"+codigo;
        }

        return codigoComZero;
    }

    // MÉTODO DE OBTER SOMENTE OS 4 ÚLTIMOS DIGITOS DO CNPJ
    public String ultimoDigitoCnpj(String cnpj){

        String ultimosDigitos = cnpj.substring(cnpj.length() - 4);

        return ultimosDigitos;

    }

    // MÉTODO DE CONVERSÃO DE VARIÁVEL fornecedorDTO EM fornecedor
    public Fornecedor conversor(FornecedorDTO fornecedorDTO){

        // INSTANCIAR UM OBJ DA CLASSE FORNECEDOR
        Fornecedor fornecedor = new Fornecedor();

        // ATRIBUIR VALOR DE ID PARA FORNECEDOR
        fornecedor.setId(fornecedorDTO.getId());

        // RETORNO DE VARIÁVEL PROCESSADA
        return fornecedor;
    }

    // MÉTODO DE VALIDAÇÃO DOS CAMPOS
    private void validate(CategoriaProdutoDTO categoriaProdutoDTO) {

        LOGGER.info("Validando categoria...");

        // OBS: .toString() PARA CONVERTER TIPO 'Long' PARA 'String'
        if(categoriaProdutoDTO == null){
            throw new IllegalArgumentException("CategoriaProdutoDTO não deve ser nulo");
        }

        if (categoriaProdutoDTO.getIdFornecedor() == null) {
            throw new IllegalArgumentException("ID do fornecedor não deve ser nulo");
        }

        if (StringUtils.isEmpty(categoriaProdutoDTO.getCodigo())) {
            throw new IllegalArgumentException("Código não deve ser nulo/vazio");
        }

        if (StringUtils.isEmpty(categoriaProdutoDTO.getNome())) {
            throw new IllegalArgumentException("Nome não deve ser nulo/vazio");
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

    public CategoriaProdutoDTO update(CategoriaProdutoDTO categoriaProdutoDTO, Long id) {

        // OBTER SE CATEGORIA EXISTENTE NO BANCO DE DADOS
        Optional<CategoriaProduto> categoriaProdutoExistenteOptional = this.iCategoriaProdutoRepository.findById(id);

        if (categoriaProdutoExistenteOptional.isPresent()) {

            CategoriaProduto categoriaProdutoExistente = categoriaProdutoExistenteOptional.get();

            LOGGER.info("Atualizando categoria... id: [{}]", categoriaProdutoExistente.getId());
            LOGGER.debug("Payload: {}", categoriaProdutoDTO);
            LOGGER.debug("Categoria Existente: {}", categoriaProdutoExistente);

            //  OBTER fornecedorDTO PELO 'ID' ESPECÍFICO
            FornecedorDTO fornecedorDTO = fornecedorService.findById(categoriaProdutoDTO.getIdFornecedor());

                // PROCESSO DE CONCATENAÇÃO DO CÓDIGO
                String cnpj = fornecedorDTO.getCnpj();
                String cnpjProcessado = ultimoDigitoCnpj(cnpj);
                String codigo = categoriaProdutoDTO.getCodigo();
                String codigoComZero = validarCodigo(codigo);
                String codigoProcessado = "CAT"+cnpjProcessado+codigoComZero;

                // DEFINIR O CÓDIGO CONCATENADO
                categoriaProdutoExistente.setCodigo(codigoProcessado);

            // EXECUTANDO MÉTODO DE CONVERSÃO
            Fornecedor fornecedor = conversor(fornecedorDTO);

            // PASSAR PARÂMETRO CONVERTIDO
            categoriaProdutoExistente.setFornecedor(fornecedor);

            categoriaProdutoExistente.setNome(categoriaProdutoDTO.getNome());

            categoriaProdutoExistente = this.iCategoriaProdutoRepository.save(categoriaProdutoExistente);

            return CategoriaProdutoDTO.of(categoriaProdutoExistente);
        }

        throw new IllegalArgumentException(String.format("Id %s não existe", id));

    }

    public void delete(Long id) {

        LOGGER.info("Executando delete para categoria de id: [{}]", id);

        this.iCategoriaProdutoRepository.deleteById(id);
    }

    // EXPORTAR PARA CSV - ATIVIDADE 3
    public void findAll(HttpServletResponse response) throws Exception {

        // VARIÁVEL COM NOME DO ARQUIVO DO EXCEL
        String arquivoCSV = "categoriaProduto.csv";

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + arquivoCSV + "\"");

        PrintWriter writer = response.getWriter();

        ICSVWriter icsvWriter = new CSVWriterBuilder(writer).withSeparator(';').withEscapeChar(CSVWriter.DEFAULT_ESCAPE_CHARACTER).withLineEnd(CSVWriter.DEFAULT_LINE_END).build();

        // VETOR COM OS NOMES DAS COLUNAS NO EXCEL
        String[] cabecalhoCSV = {"codigo", "nome", "razao_social", "cnpj"};

        // ESCREVER O CABEÇALHO PRIMEIRO
        icsvWriter.writeNext(cabecalhoCSV);

        // LAÇO PARA PREENCHER AS INFORMAÇÕES
        for(CategoriaProduto rows : iCategoriaProdutoRepository.findAll()){

            icsvWriter.writeNext(new String[]{

                    // LINHAS COM AS INFORMAÇÕES
                    rows.getCodigo(),
                    rows.getNome(),
                    rows.getFornecedor().getRazaoSocial(),
                    mascaraFormatada(rows.getFornecedor().getCnpj()),

            });
        }
    }

    // MÉTODO DE FORMATAR CNPJ PARA EXPORTAR PARA CSV - ATIVIDADE 3
    public String mascaraFormatada(String cnpj){

        /*String cnpjFormatado =  cnpj.charAt(0)+cnpj.charAt(1)+"."+
                                cnpj.charAt(2)+cnpj.charAt(3)+cnpj.charAt(4)+"."+
                                cnpj.charAt(5)+cnpj.charAt(6)+cnpj.charAt(7)+"/"+
                                cnpj.charAt(8)+cnpj.charAt(9)+cnpj.charAt(10)+cnpj.charAt(11)+"-"+
                                cnpj.charAt(12)+cnpj.charAt(13);*/

        String cnpj1formador =  cnpj.substring(0, 2)+ "."+
                                cnpj.substring(2, 5)+"."+
                                cnpj.substring(5, 8)+ "/"+
                                cnpj.substring(8, 12)+"-"+
                                cnpj.substring(12, 14);

        return cnpj1formador;


        /*return cnpjFormatado;*/
    }

    // IMPORTAR DE UM CSV - ATIVIDADE 4
    public List<CategoriaProduto> obterTudo(MultipartFile file) throws Exception {

        InputStreamReader inputStreamReader = new InputStreamReader(file.getInputStream());

        CSVReader leitor = new CSVReaderBuilder(inputStreamReader).withSkipLines(1).build();

        List<String[]> row = leitor.readAll();
        List<CategoriaProduto> categoriaProdutoList = new ArrayList<>();

        for(String[] linha : row){

            String[] vetor = linha[0].replaceAll("\"", "").split(";");

            // OBJETOS DAS CLASSES CategoriaProduto e Fornecedor
            CategoriaProduto categoriaProduto = new CategoriaProduto();

            categoriaProduto.setCodigo(vetor[0]);
            categoriaProduto.setNome(vetor[1]);
            // vetor[2] = razão social -> não utilizada

                // OBTER 'ID' DO CNPJ CADASTRADO DO EXCEL
                // CONVERTANDO VARIÁVEL TIPO FornecedorDTO PARA Fornecedor
                Fornecedor fornecedor = new Fornecedor();

                String cnpjDesformatado = desformatarCnpj(vetor[3]);

                FornecedorDTO fornecedorDTO = fornecedorService.findByCnpj(cnpjDesformatado);
                fornecedor = conversor(fornecedorDTO);

                categoriaProduto.setFornecedor(fornecedor);

            // ADICIONAR OBJ CategoriaProduto NO ARRAY LIST
            categoriaProdutoList.add(categoriaProduto);

        }

        LOGGER.info("Finalizando importação...");

        return iCategoriaProdutoRepository.saveAll(categoriaProdutoList);

    }

    public String desformatarCnpj(String cnpj) {

        String cnpjDesformatado =   cnpj.charAt(0)+""+cnpj.charAt(1)+
                                    cnpj.charAt(3)+cnpj.charAt(4)+cnpj.charAt(5)+
                                    cnpj.charAt(7)+cnpj.charAt(8)+cnpj.charAt(9)+
                                    cnpj.charAt(11)+cnpj.charAt(12)+cnpj.charAt(13)+cnpj.charAt(14)+
                                    cnpj.charAt(16)+cnpj.charAt(17);

        return cnpjDesformatado;

    }

    public CategoriaProdutoDTO findByCodigo(String codigo) {

        Optional<CategoriaProduto> categoriaProdutoOptional = this.iCategoriaProdutoRepository.findByCodigo(codigo);

        if (categoriaProdutoOptional.isPresent()) {
            CategoriaProduto categoriaProduto = categoriaProdutoOptional.get();
            CategoriaProdutoDTO categoriaProdutoDTO = CategoriaProdutoDTO.of(categoriaProduto);

            return categoriaProdutoDTO;

        }

        String format = String.format("Código %s não existe", codigo);

        throw new IllegalArgumentException(format);
    }
}









