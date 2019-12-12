package br.com.hbsis.produto;

import br.com.hbsis.linhaCategoria.ILinhaCategoriaRepository;
import br.com.hbsis.linhaCategoria.LinhaCategoria;
import br.com.hbsis.linhaCategoria.LinhaCategoriaDTO;
import br.com.hbsis.linhaCategoria.LinhaCategoriaService;
import com.opencsv.*;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.DoubleExpression;
import net.bytebuddy.asm.Advice;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sun.awt.util.IdentityLinkedList;
import sun.java2d.pipe.SpanShapeRenderer;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * CLASSE RESPONSÁVEL PELO PROCESSAMENTO DA REGRA DE NEGÓCIO
 */
@Service
public class ProdutoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProdutoService.class);

    private final IProdutoRepository iProdutoRepository;
    private final LinhaCategoriaService linhaCategoriaService;
    private final ILinhaCategoriaRepository iLinhaCategoriaRepository;

    /* CONSTRUTOR */
    @Autowired
    public ProdutoService(IProdutoRepository iProdutoRepository, LinhaCategoriaService linhaCategoriaService, ILinhaCategoriaRepository iLinhaCategoriaRepository) {
        this.iProdutoRepository = iProdutoRepository;
        this.linhaCategoriaService = linhaCategoriaService;
        this.iLinhaCategoriaRepository = iLinhaCategoriaRepository;
    }

    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

    // MÉTODO DE CADASTRAMENTO DO PRODUTO
    public ProdutoDTO save(ProdutoDTO produtoDTO){

        this.validate(produtoDTO);

        LOGGER.info("Salvando produto...");
        LOGGER.debug("Produto: {}", produtoDTO);

        Produto produto = new Produto();

        // ADICIONAR ZEROS E LETRAS MAUISCULAS
        String codigo = produtoDTO.getCodigo();
        String codigoUpperCase = codigo.toUpperCase();
        String codigoProcessado = codigoZerosEsquerda(codigoUpperCase);

        // int conatdor = findByCodigo(codigoProcessado); // VALIDANDO CÓDIGO

        produto.setCodigo(codigoProcessado); // VALOR FINAL

        produto.setNome(produtoDTO.getNome());
        produto.setPreco(produtoDTO.getPreco());

        /* CONERSÃO DE OBJ */
        LinhaCategoriaDTO linhaCategoriaDTO = linhaCategoriaService.findById(produtoDTO.getIdLinha());

        LinhaCategoria linhaCategoria = converter(linhaCategoriaDTO);
        /* FIM DE CONVERSÃO */

        produto.setLinhaCategoria(linhaCategoria);
        produto.setUnidadeCaixa(produtoDTO.getUnidadeCaixa());

        produto.setPesoUnidade(produtoDTO.getPesoUnidade());

        String unidadeDePeso = produtoDTO.getUnidadeDePeso();

        if(unidadeDePeso.equals("mg") || unidadeDePeso.equals("g") || unidadeDePeso.equals("Kg") ||
           unidadeDePeso.equals("Mg") || unidadeDePeso.equals("kg")){

            produto.setUnidadeDePeso(unidadeDePeso);

        }else{
            throw new IllegalArgumentException("Informe peso em 'mg' 'g' ou 'Kg'");
        }

        produto.setValidade(produtoDTO.getValidade());

        produto = this.iProdutoRepository.save(produto);

        return ProdutoDTO.of(produto);

    }

    // ADICIONAR ZEROS A ESQUERDA
    public String codigoZerosEsquerda(String codigo){

        String codigoProcessado = StringUtils.leftPad(codigo, 10, "0");

        return codigoProcessado;
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

        if(produtoDTO.getValidade() == null){
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

    public void delete(Long id){

        LOGGER.info("Executando delete para produto de id: [{}]", id);

        this.iProdutoRepository.deleteById(id);
    }

    // EXPORTAR PARA CSV - ATIVIDADE 9
    public void findAll(HttpServletResponse response) throws Exception {

        // VARIÁVEL COM NOME DO ARQUIVO DO EXCEL
        String arquivoCSV = "produtos.csv";

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + arquivoCSV + "\"");

        PrintWriter writer = response.getWriter();

        ICSVWriter icsvWriter = new CSVWriterBuilder(writer).withSeparator(';').withEscapeChar(CSVWriter.DEFAULT_ESCAPE_CHARACTER).withLineEnd(CSVWriter.DEFAULT_LINE_END).build();

        String[] cabecalhoCSV = {"codigo", "nome", "preco", "unidade_caixa", "peso_unidade", "validade",
                "codigo_linha", "nome_linha", "codigo_categoria", "nome_categoria", "cnpj_fornecedor", "razao_social_fornecedor"};

        icsvWriter.writeNext(cabecalhoCSV);

        for(Produto rows : iProdutoRepository.findAll()){

            icsvWriter.writeNext(new String[]{

                // LINHAS COM AS INFORMAÇÕES
                rows.getCodigo(),
                rows.getNome(),

                formatarPreco(rows.getPreco()), // PREÇO FORMATADO

                rows.getUnidadeCaixa().toString(),

                formatarPeso(rows.getPesoUnidade(), rows.getUnidadeDePeso()), // PESO FORMATADO
                formatarValidade(rows.getValidade()), // VALIDADE FORMATADA

                rows.getLinhaCategoria().getCodigoLinha(),
                rows.getLinhaCategoria().getNome(),
                rows.getLinhaCategoria().getCategoriaProduto().getCodigo(),
                rows.getLinhaCategoria().getCategoriaProduto().getNome(),

                formatarCnpj(rows.getLinhaCategoria().getCategoriaProduto().getFornecedor().getCnpj()), // CNPJ FORMATADO

                rows.getLinhaCategoria().getCategoriaProduto().getFornecedor().getRazaoSocial()
            });
        }
    }

    // FORMATAR PREÇO PARA EXPORTAR PARA CSV
    public String formatarPreco(Double preco){

        String precoFormatado = "R$"+preco;

        return precoFormatado;
    }

    // FORMATAR PESO PARA EXPORTAR PARA CSV
    public String formatarPeso(Double peso, String unidadePeso){

        String pesoFormatado = peso+""+unidadePeso;

        return pesoFormatado;
    }

    // FORMATAR CNPJ PARA EXPORTAR PARA CSV
    public String formatarCnpj(String cnpj){

        String cnpjFormado =    cnpj.substring(0, 2)+ "."+
                                cnpj.substring(2, 5)+"."+
                                cnpj.substring(5, 8)+ "/"+
                                cnpj.substring(8, 12)+"-"+
                                cnpj.substring(12, 14);

        return cnpjFormado;
    }

    // FORMATAR VALIDADE PARA EXPORTAR PARA CSV
    public String formatarValidade(Date validade) {

        String validadeRecebida = String.valueOf(validade);

        String ano = ""+validadeRecebida.charAt(0)+validadeRecebida.charAt(1)+validadeRecebida.charAt(2)+validadeRecebida.charAt(3);
        String mes = ""+validadeRecebida.charAt(5)+validadeRecebida.charAt(6);
        String dia = ""+validadeRecebida.charAt(8)+validadeRecebida.charAt(9);
        String validadeFormatada = dia+"/"+mes+"/"+ano;

        return validadeFormatada;
    }

    // IMPORTAR DE UM CSV - ATIVIDADE 10
    public List<Produto> obterTudo(MultipartFile file) throws Exception {

        InputStreamReader inputStreamReader = new InputStreamReader(file.getInputStream());

        CSVReader leitor = new CSVReaderBuilder(inputStreamReader).withSkipLines(1).build();

        List<String[]> row = leitor.readAll();
        List<Produto> produtoList = new ArrayList<>();

        for(String[] linha : row){

            String[] vetor = linha[0].replaceAll("\"", "").split(";");

            // OBJETOS DAS CLASSES Produto e LinhaCategoria
            Produto produto = new Produto();
            produto.setCodigo(vetor[0]);
            produto.setNome(vetor[1]);
            produto.setPreco(Double.parseDouble(vetor[2].replace("R", "").replace("$", "").replaceAll(",", ".")));

            /* CONVERTENDO VARIÁVEL */
            LinhaCategoria linhaCategoria = new LinhaCategoria();
            LinhaCategoriaDTO linhaCategoriaDTO = linhaCategoriaService.findByCodigo(vetor[6]);
            linhaCategoria = converter(linhaCategoriaDTO);

            produto.setLinhaCategoria(linhaCategoria);
            /* FIM DA CONVERSÃO */

            produto.setUnidadeCaixa(Long.parseLong(vetor[3]));
            produto.setPesoUnidade(Double.parseDouble(vetor[4].replace("m", "").replace("g", "").replace("k", "").replace("K", "").replaceAll(",", "")));

            String validadeCSV = vetor[5];
            String dataSemBarra = validadeCSV.replaceAll("/", "");
            String dia = ""+dataSemBarra.charAt(0)+dataSemBarra.charAt(1);
            String mes = ""+dataSemBarra.charAt(2)+dataSemBarra.charAt(3);
            String ano = ""+dataSemBarra.charAt(4)+dataSemBarra.charAt(5)+dataSemBarra.charAt(6)+dataSemBarra.charAt(7);
            String validadePadrao = ano+"-"+mes+"-"+dia;
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date data = format.parse(validadePadrao);
            produto.setValidade(data);

            produto.setUnidadeDePeso(vetor[4].replaceAll("\\d", "").replace(".", ""));

            // ADICIONANDO NO ARRYLIST
            produtoList.add(produto);
        }

        LOGGER.info("Finalizando importação...");

        return iProdutoRepository.saveAll(produtoList);
    }

    public ProdutoDTO update(ProdutoDTO produtoDTO, Long id){

        // OBTER PRODUTO EXISTENTE NO BANCO DE DADOS
        Optional<Produto> produtoExistenteOptional = this.iProdutoRepository.findById(id);

        if(produtoExistenteOptional.isPresent()){

            Produto produtoExistente = produtoExistenteOptional.get();

            LOGGER.info("Atualizando produto... id: [{}]", produtoExistente.getId());
            LOGGER.debug("Payload: {}", produtoDTO);
            LOGGER.debug("Produto existente: {}", produtoExistente);

            // ADICIONAR ZEROS E LETRAS MAUISCULAS
            String codigo = produtoDTO.getCodigo();
            String codigoUpperCase = codigo.toUpperCase();
            String codigoProcessado = codigoZerosEsquerda(codigoUpperCase);
            produtoExistente.setCodigo(codigoProcessado); // VALOR FINAL

            produtoExistente.setNome(produtoDTO.getNome());
            produtoExistente.setPreco(produtoDTO.getPreco());

            /* CONVERSÃO */
            LinhaCategoriaDTO linhaCategoriaDTO = linhaCategoriaService.findById(produtoDTO.getIdLinha());
            LinhaCategoria linhaCategoria = converter(linhaCategoriaDTO);
            /* FIM CONVERSÃO */

            produtoExistente.setLinhaCategoria(linhaCategoria);
            produtoExistente.setUnidadeCaixa(produtoDTO.getUnidadeCaixa());
            produtoExistente.setPesoUnidade(produtoDTO.getPesoUnidade());

            String unidadeDePeso = produtoDTO.getUnidadeDePeso();

            if(unidadeDePeso.equals("mg") || unidadeDePeso.equals("g") || unidadeDePeso.equals("Kg") ||
                    unidadeDePeso.equals("Mg") || unidadeDePeso.equals("kg")){

                produtoExistente.setUnidadeDePeso(unidadeDePeso);

            }else{
                throw new IllegalArgumentException("Informe peso em 'mg' 'g' ou 'Kg'");
            }

            produtoExistente.setValidade(produtoDTO.getValidade());

            produtoExistente = this.iProdutoRepository.save(produtoExistente);

            return ProdutoDTO.of(produtoExistente);
        }

        String format = String.format("Id %s não existe", id);

        throw new IllegalArgumentException(format);
    }
}










