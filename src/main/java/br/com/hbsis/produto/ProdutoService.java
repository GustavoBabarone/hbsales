package br.com.hbsis.produto;

import br.com.hbsis.categoriaproduto.CategoriaProduto;
import br.com.hbsis.categoriaproduto.CategoriaProdutoDTO;
import br.com.hbsis.categoriaproduto.CategoriaProdutoService;
import br.com.hbsis.csv.CSV;
import br.com.hbsis.fornecedor.Fornecedor;
import br.com.hbsis.fornecedor.FornecedorDTO;
import br.com.hbsis.fornecedor.FornecedorService;
import br.com.hbsis.linhacategoria.LinhaCategoria;
import br.com.hbsis.linhacategoria.LinhaCategoriaDTO;
import br.com.hbsis.linhacategoria.LinhaCategoriaService;
import com.opencsv.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProdutoService.class);

    private final IProdutoRepository iProdutoRepository;
    private final FornecedorService fornecedorService;
    private final CategoriaProdutoService categoriaProdutoService;
    private final LinhaCategoriaService linhaCategoriaService;
    private final CSV csv;

    @Autowired
    public ProdutoService(IProdutoRepository iProdutoRepository, FornecedorService fornecedorService, CategoriaProdutoService categoriaProdutoService, LinhaCategoriaService linhaCategoriaService, CSV csv) {
        this.iProdutoRepository = iProdutoRepository;
        this.fornecedorService = fornecedorService;
        this.categoriaProdutoService = categoriaProdutoService;
        this.linhaCategoriaService = linhaCategoriaService;
        this.csv = csv;
    }

    public ProdutoDTO salvar(ProdutoDTO produtoDTO){

        this.validarProduto(produtoDTO);

        LOGGER.info("Executando save de produto");

        String codigo = gerarCodigo(produtoDTO.getCodigoProduto());
        LinhaCategoriaDTO linhaCategoriaDTO = linhaCategoriaService.findById(produtoDTO.getIdLinha());
        LinhaCategoria linhaCategoria = new LinhaCategoria(linhaCategoriaDTO.getId());

        Produto produto = new Produto(
                codigo,
                produtoDTO.getNome(),
                produtoDTO.getPreco(),
                linhaCategoria,
                produtoDTO.getUnidadeCaixa(),
                produtoDTO.getPesoUnidade(),
                produtoDTO.getUnidadeDePeso(),
                produtoDTO.getValidade()
        );

        produto = this.iProdutoRepository.save(produto);
        return ProdutoDTO.of(produto);
    }

    public ProdutoDTO atualizar(ProdutoDTO produtoDTO, Long id){

        this.validarProduto(produtoDTO);

        Optional<Produto> produtoExistenteOptional = this.iProdutoRepository.findById(id);

        if(produtoExistenteOptional.isPresent()){

            Produto produtoExistente = produtoExistenteOptional.get();

            LOGGER.info("Executando update de produto de id: [{}]", produtoExistente.getId());

            String codigo = gerarCodigo(produtoDTO.getCodigoProduto());
            LinhaCategoriaDTO linhaCategoriaDTO = linhaCategoriaService.findById(produtoDTO.getIdLinha());
            LinhaCategoria linhaCategoria = new LinhaCategoria(linhaCategoriaDTO.getId());

            produtoExistente.setCodigoProduto(codigo);
            produtoExistente.setNome(produtoDTO.getNome());
            produtoExistente.setPreco(produtoDTO.getPreco());
            produtoExistente.setLinhaCategoria(linhaCategoria);
            produtoExistente.setUnidadeCaixa(produtoDTO.getUnidadeCaixa());
            produtoExistente.setPesoUnidade(produtoDTO.getPesoUnidade());
            produtoExistente.setValidade(produtoDTO.getValidade());

            produtoExistente = this.iProdutoRepository.save(produtoExistente);
            return ProdutoDTO.of(produtoExistente);
        }

        throw new IllegalArgumentException(String.format("Produto de iId [%s] não encontrado", id));
    }

    public void deletar(Long id){

        LOGGER.info("Executando delete para produto de id: [{}]", id);

        if(iProdutoRepository.existsById(id)){
            this.iProdutoRepository.deleteById(id);
        }else {
            throw new IllegalArgumentException(String.format("Produto de id [%s] não encontrado", id));
        }
    }

    public ProdutoDTO findById(Long id){

        LOGGER.info("Executando findById para produto de id: [{}]", id);

        Optional<Produto> produtoOptional = this.iProdutoRepository.findById(id);

        if(produtoOptional.isPresent()){
            return ProdutoDTO.of(produtoOptional.get());
        }

        throw new IllegalArgumentException(String.format("Produto de id [%s] não encontrado", id));
    }

    public void validarProduto(ProdutoDTO produtoDTO){

        LOGGER.info("Validando produto");

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

    public ProdutoDTO findByCodigo(String codigo) {

        LOGGER.info("Executando findByCodigo para produto de codigo: [{}]", codigo);

        Optional<Produto> produtoOptional = this.iProdutoRepository.findByCodigoProduto(codigo);

        if(produtoOptional.isPresent()){
            return ProdutoDTO.of(produtoOptional.get());
        }

        throw new IllegalArgumentException(String.format("Produto de codigo [%s] não encontrado", codigo));
    }

    public boolean existsByCodigo(String codigoProduto) {
        return this.iProdutoRepository.existsByCodigoProduto(codigoProduto);
    }

    public String gerarCodigo(String codigo){

        codigo = codigo.toUpperCase();
        return StringUtils.leftPad(codigo, 10, "0");
    }

    public String adicionarUnidadeMonetariaAoPreco(Double preco){
        return "R$" + preco;
    }

    public String adicionarUnidadeDeMedidaAoPeso(Double peso, String unidadePeso){
        return String.format("%s%s", peso, unidadePeso);
    }

    public String substituirHifenPorBarraNaValidade(Date validade) {
        return String.valueOf(validade).replace("-", "/");
    }

    public Date transformarValidadeParaPadraoInternacional(String data) {

        data = data.replaceAll("/", "");
        Date validade = null;
        try{
            String dia = data.substring(0, 2);
            String mes = data.substring(2, 4);
            String ano = data.substring(4, 8);
            String validadeConcat = ano.concat("-").concat(mes).concat("-").concat(dia);

            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            validade = format.parse(validadeConcat);

        }catch (ParseException e){
            LOGGER.error("ERRO! Padrão de validade inválido (yyyy-MM-dd)");
        }

        return validade;
    }

    public void exportarProduto(HttpServletResponse response) throws Exception {

        String arquivoCSV = "produtos.csv";
        String[] cabecalhoCSV = {"codigo", "nome", "preco", "unidade_caixa", "peso_unidade",
                "validade", "codigo_linha", "nome_linha", "codigo_categoria", "nome_categoria",
                "cnpj_fornecedor", "razao_social_fornecedor"};
        ICSVWriter icsvWriter = csv.padraoExportarCsv(response, arquivoCSV, cabecalhoCSV);

        for(Produto rows : iProdutoRepository.findAll()){

            icsvWriter.writeNext(new String[]{

                    rows.getCodigoProduto(),
                    rows.getNome(),
                    adicionarUnidadeMonetariaAoPreco(rows.getPreco()),
                    rows.getUnidadeCaixa().toString(),
                    adicionarUnidadeDeMedidaAoPeso(rows.getPesoUnidade(), rows.getUnidadeDePeso()),
                    substituirHifenPorBarraNaValidade(rows.getValidade()),
                    rows.getLinhaCategoria().getCodigoLinha(),
                    rows.getLinhaCategoria().getNome(),
                    rows.getLinhaCategoria().getCategoriaProduto().getCodigoCategoria(),
                    rows.getLinhaCategoria().getCategoriaProduto().getNome(),
                    fornecedorService.formatarCnpj(rows.getLinhaCategoria().getCategoriaProduto().getFornecedor().getCnpj()),
                    rows.getLinhaCategoria().getCategoriaProduto().getFornecedor().getRazaoSocial()
            });
        }
        LOGGER.info("Finalizando exportação de produto");
    }

    public List<Produto> importarProduto(MultipartFile file) throws Exception {

       List<String[]> row = csv.padraoImportarCsv(file);
        List<Produto> produtoList = new ArrayList<>();

        for(String[] linha : row){

            String[] vetor = linha[0].replaceAll("\"", "").split(";");

            if(existsByCodigo(vetor[0])){

                LinhaCategoriaDTO linhaCategoriaDTO = linhaCategoriaService.findByCodigoLinha(vetor[6]);
                LinhaCategoria linhaCategoria = new LinhaCategoria(linhaCategoriaDTO.getId());

                Produto produto = new Produto(
                        vetor[0],
                        vetor[1],
                        Double.parseDouble(vetor[2].replace("R", "").replace("$", "").replaceAll(",", ".")),
                        linhaCategoria,
                        Long.parseLong(vetor[3]),
                        Double.parseDouble(vetor[4].replace("m", "").replace("g", "").replace("k", "").replace("K", "").replaceAll(",", "")),
                        vetor[4].replaceAll("\\d", "").replace(".", ""),
                        transformarValidadeParaPadraoInternacional(vetor[5])
                );

                produtoList.add(produto);

            }else {
                LOGGER.info("Produto já existente no banco de dados...");
            }
        }
        LOGGER.info("Finalizando importação de produtos...");
        return iProdutoRepository.saveAll(produtoList);
    }

    /** CSV - ATIVIDADE 11*/
    public void importarProdutoPorFornecedor(MultipartFile file, Long idFornecedor) throws Exception {

        InputStreamReader inputStreamReader = new InputStreamReader(file.getInputStream());
        CSVReader csvReader = new CSVReaderBuilder(inputStreamReader).withSkipLines(1).build();

        List<String[]> row = csvReader.readAll();

        for(String[] linha : row){

            String[] vetor = linha[0].replaceAll("\"", "").split(";");

            String codigoProduto = vetor[0].toUpperCase();
            String nomeProduto = vetor[1];
            String precoProduto = vetor[2].replace("R", "").replace("$", "").replace(",", ".");
            String unidadePorCaixa = vetor[3];
            String pesoPorUnidade = vetor[4].replace("m", "").replace("g", "").replace("K", "").replace(",", ".");
            String unidadeMedida = vetor[4].replaceAll("\\d", "").replace(".", "");
            String validadeProduto = vetor[5].replaceAll("/", "");;
            String codigoLinha = vetor[6].toUpperCase();
            String nomeLinha = vetor[7];
            String codigoCategoria = vetor[8].toUpperCase();
            String nomeCategoria = vetor[9];
            String cnpjFornecedor = fornecedorService.desformatarCnpj(vetor[10]);
            /* String razaoSocialFornecedor = vetor[11]; -> Não utilizada */

            /** FORNECEDOR EXISTE? -> COMPARA 'ID' COM OS 'IDS' CADASTRADO POR PRODUTO */
            if(fornecedorService.existsById(idFornecedor)){

                if(idFornecedor == fornecedorService.findByCnpj(cnpjFornecedor).getId()) {

                /** TEM FORNECEDOR CADASTRADO? -> CONTINUA... */

                    if(categoriaProdutoService.existByCodigo(codigoCategoria)){

                        /** TEM CATEGORIA CADASTRADA? -> ATUALIZA */
                        atualizarCategoriaNoImportarPorFornecedor(codigoCategoria, nomeCategoria, cnpjFornecedor);

                        if(linhaCategoriaService.existsByCodigo(codigoLinha)){

                            /** TEM LINHA CADASTRADA? -> ATUALIZA */
                            atualizarLinhaNoImportarPorFornecedor(codigoLinha, codigoCategoria, nomeLinha);

                            if(existsByCodigo(codigoProduto)){

                                /** TEM PRODUTO CADASTRADO? -> ATUALIZA */
                                atualizarProdutoNoImportarPorFornecedor(codigoProduto, nomeProduto, precoProduto, unidadePorCaixa, pesoPorUnidade, unidadeMedida, validadeProduto, codigoLinha);

                            }else {

                                /** NÃO TEM PRODUTO CADASTRADO? -> CADASTRA */
                                salvarProdutoNoImportarPorFornecedor(codigoProduto, nomeProduto, precoProduto, unidadePorCaixa, pesoPorUnidade, unidadeMedida, validadeProduto, codigoLinha);
                            }

                        }else {

                            /** NÃO TEM LINHA CADASTRADA? -> CADASTRA */
                            salvarLinhaNoImportarPorFornecedor(codigoLinha, codigoCategoria, nomeLinha);
                        }

                    }else {

                        /** NÃO TEM CATEGORIA CADASTRADA? -> CADASTRA */
                        salvarCategoriaNoImportarPorFornecedor(codigoCategoria, nomeCategoria, cnpjFornecedor);
                    }
                }

            }else {

                /** NÃO TEM FORNECEDOR CADASTRADO? -> ERRO */
                throw new IllegalArgumentException(String.format("Fornecedor de id [%s] não encontrado", idFornecedor));
            }
        }

        LOGGER.info("Finalizando importação de CSV por fornecedor");
    }

    /** PRODUTO */
    public ProdutoDTO salvarProdutoNoImportarPorFornecedor(String codigoProduto, String nome, String preco,
    String unidadeCaixa, String pesoPorUnidade, String unidadeMedida, String validade, String codigoLinha) throws ParseException {

        LOGGER.info("Produto não cadastrado...  código produto: [{}]", codigoProduto);
        LOGGER.info("Cadastrando novo produto...");

        Produto produto = new Produto();
        String codigoFinal = gerarCodigo(codigoProduto);
        produto.setCodigoProduto(codigoFinal);
        produto.setNome(nome);
        produto.setPreco(Double.parseDouble(preco));

        LinhaCategoriaDTO linhaCategoriaDTO = linhaCategoriaService.findByCodigoLinha(codigoLinha);
        LinhaCategoria linhaCategoria = new LinhaCategoria(linhaCategoriaDTO.getId());
        produto.setLinhaCategoria(linhaCategoria);

        produto.setUnidadeCaixa(Long.parseLong(unidadeCaixa));
        produto.setPesoUnidade(Double.parseDouble(pesoPorUnidade));
        produto.setValidade(transformarValidadeParaPadraoInternacional(validade));
        produto.setUnidadeDePeso(unidadeMedida);

        produto = this.iProdutoRepository.save(produto);
        return ProdutoDTO.of(produto);
    }

    public ProdutoDTO atualizarProdutoNoImportarPorFornecedor(String codigoProduto, String nome, String preco,
    String unidadeCaixa, String pesoPorUnidade, String unidadeMedida, String validade, String codigoLinha) throws ParseException {

        ProdutoDTO produtoDTO = findByCodigo(codigoProduto);
        Optional<Produto> produtoExistenteOptional = this.iProdutoRepository.findById(produtoDTO.getId());
        Produto produtoExistente = produtoExistenteOptional.get();

        LOGGER.info("Atualizando produto... id: [{}]", produtoExistente.getId());

        String codigoFinal = gerarCodigo(codigoProduto);
        produtoExistente.setCodigoProduto(codigoFinal);
        produtoExistente.setNome(nome);
        produtoExistente.setPreco(Double.parseDouble(preco));

        LinhaCategoriaDTO linhaCategoriaDTO = linhaCategoriaService.findByCodigoLinha(codigoLinha);
        LinhaCategoria linhaCategoria = new LinhaCategoria(linhaCategoriaDTO.getId());
        produtoExistente.setLinhaCategoria(linhaCategoria);

        produtoExistente.setUnidadeCaixa(Long.parseLong(unidadeCaixa));
        produtoExistente.setPesoUnidade(Double.parseDouble(pesoPorUnidade));
        produtoExistente.setValidade(transformarValidadeParaPadraoInternacional(validade));
        produtoExistente.setUnidadeDePeso(unidadeMedida);

        produtoExistente = this.iProdutoRepository.save(produtoExistente);
        return ProdutoDTO.of(produtoExistente);
    }

    /** LINHA */
    public LinhaCategoriaDTO salvarLinhaNoImportarPorFornecedor(String codigoLinha, String codigoCategoria, String nome){

        LOGGER.info("Linha de categoria não cadastrada... código linha: [{}]", codigoLinha);
        LOGGER.info("Cadastrando nova linha de categoria...");

        LinhaCategoria linhaCategoria = new LinhaCategoria();
        linhaCategoria.setNome(nome);

        String codigoFinal = linhaCategoriaService.gerarCodigo(codigoLinha);
        linhaCategoria.setCodigoLinha(codigoFinal);

        CategoriaProdutoDTO categoriaProdutoDTO = categoriaProdutoService.findByCodigoCategoria(codigoCategoria);
        linhaCategoria.setCategoriaProduto(new CategoriaProduto(categoriaProdutoDTO.getId()));

        linhaCategoria = this.linhaCategoriaService.executarSaveNaRepository(linhaCategoria);
        return LinhaCategoriaDTO.of(linhaCategoria);
    }

    public LinhaCategoriaDTO atualizarLinhaNoImportarPorFornecedor(String codigoLinha, String codigoCategoria, String nome){

        Optional<LinhaCategoria> linhaCategoriaOptionalExistente = linhaCategoriaService.findByCodigoLinhaImportProdutoPorFornecedor(codigoLinha);
        LinhaCategoria linhaCategoria = linhaCategoriaOptionalExistente.get();

        LOGGER.info("Atualizando Linha de Categoria... id: [{}]", linhaCategoria.getId());

        String codigoFinal = linhaCategoriaService.gerarCodigo(codigoLinha.toUpperCase());
        linhaCategoria.setCodigoLinha(codigoFinal);

        CategoriaProdutoDTO categoriaProdutoDTO = categoriaProdutoService.findByCodigoCategoria(codigoCategoria);
        linhaCategoria.setCategoriaProduto(new CategoriaProduto(categoriaProdutoDTO.getId()));

        linhaCategoria.setNome(nome);

        linhaCategoria = this.linhaCategoriaService.executarSaveNaRepository(linhaCategoria);
        return LinhaCategoriaDTO.of(linhaCategoria);
    }

    /** CATEGORIA */
    public CategoriaProdutoDTO salvarCategoriaNoImportarPorFornecedor(String codigoCategoria, String nomeCategoria, String cnpjForneceodor){

        LOGGER.info("Categoria de produto não cadastrada... código de categoria: [{}]", codigoCategoria);
        LOGGER.info("Cadastrando nova categoria de produto...");

        CategoriaProduto categoriaProduto = new CategoriaProduto();
        categoriaProduto.setCodigoCategoria(codigoCategoria);
        categoriaProduto.setNome(nomeCategoria);

        FornecedorDTO fornecedorDTO = fornecedorService.findByCnpj(cnpjForneceodor);
        categoriaProduto.setFornecedor(Fornecedor.of(fornecedorDTO));

        categoriaProduto = this.categoriaProdutoService.executarSaveNaRepository(categoriaProduto);
        return CategoriaProdutoDTO.of(categoriaProduto);
    }

    public CategoriaProdutoDTO atualizarCategoriaNoImportarPorFornecedor(String codigoCategoria, String nomeCategoria, String cnpjForneceodor){

        Optional<CategoriaProduto> categoriaProdutoOptionalexistente = categoriaProdutoService.findByCodigoCategoriaImportProdutoPorFornecedor(codigoCategoria);
        CategoriaProduto categoriaProdutoExistente = categoriaProdutoOptionalexistente.get();

        LOGGER.info("Atualizando Categoria de Produto... id: [{}]", categoriaProdutoExistente.getId());

        String codigo = codigoCategoria.substring(codigoCategoria.length() - 3);
        String codigoFinal = categoriaProdutoService.gerarCodigo(codigo.toUpperCase(), cnpjForneceodor);
        categoriaProdutoExistente.setCodigoCategoria(codigoFinal);

        categoriaProdutoExistente.setNome(nomeCategoria);

        FornecedorDTO fornecedorDTO = fornecedorService.findByCnpj(cnpjForneceodor);
        categoriaProdutoExistente.setFornecedor(Fornecedor.of(fornecedorDTO));

        categoriaProdutoExistente = this.categoriaProdutoService.executarSaveNaRepository(categoriaProdutoExistente);
        return CategoriaProdutoDTO.of(categoriaProdutoExistente);
    }
}










