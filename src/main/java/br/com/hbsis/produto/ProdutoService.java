package br.com.hbsis.produto;

import br.com.hbsis.categoriaproduto.CategoriaProduto;
import br.com.hbsis.categoriaproduto.CategoriaProdutoDTO;
import br.com.hbsis.categoriaproduto.CategoriaProdutoService;
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
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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

    @Autowired /** CONSTRUTOR */
    public ProdutoService(IProdutoRepository iProdutoRepository, FornecedorService fornecedorService, CategoriaProdutoService categoriaProdutoService, LinhaCategoriaService linhaCategoriaService) {
        this.iProdutoRepository = iProdutoRepository;
        this.fornecedorService = fornecedorService;
        this.categoriaProdutoService = categoriaProdutoService;
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
        }else {
            throw new IllegalArgumentException("Produto não cadastrado");
        }
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

    public ProdutoDTO findByCodigo(String codigoProduto) {

        Optional<Produto> produtoOptional = this.iProdutoRepository.findByCodigoProduto(codigoProduto);

        if(produtoOptional.isPresent()){

            Produto produto = produtoOptional.get();
            ProdutoDTO produtoDTO = ProdutoDTO.of(produto);
            return produtoDTO;
        }
        String format = String.format("Código %s não existe", codigoProduto);
        throw new IllegalArgumentException(format);
    }

    public boolean findByCodigoProduto(String codigoProduto) {

        boolean valida;
        Optional<Produto> produtoOptional = this.iProdutoRepository.findByCodigoProduto(codigoProduto);

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

    public String formatarPrecoProduto(Double preco){

        String precoFormatado = "R$"+preco;
        return precoFormatado;
    }

    public String formatarPesoProduto(Double peso, String unidadePeso){

        String pesoFormatado = peso+""+unidadePeso;
        return pesoFormatado;
    }

    public String formatarValidadeProduto(Date validade) {

        String validadeRecebida = String.valueOf(validade);
        String ano = ""+validadeRecebida.charAt(0)+validadeRecebida.charAt(1)+validadeRecebida.charAt(2)+validadeRecebida.charAt(3);
        String mes = ""+validadeRecebida.charAt(5)+validadeRecebida.charAt(6);
        String dia = ""+validadeRecebida.charAt(8)+validadeRecebida.charAt(9);
        String validadeFormatada = dia+"/"+mes+"/"+ano;
        return validadeFormatada;
    }

    public Date desformatarValidadeProduto(String dataSemBarra) throws ParseException {

        String dia = ""+dataSemBarra.charAt(0)+dataSemBarra.charAt(1);
        String mes = ""+dataSemBarra.charAt(2)+dataSemBarra.charAt(3);
        String ano = ""+dataSemBarra.charAt(4)+dataSemBarra.charAt(5)+dataSemBarra.charAt(6)+dataSemBarra.charAt(7);
        String validadePadrao = ano+"-"+mes+"-"+dia;
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date data = format.parse(validadePadrao);
        return data;
    }

    /** CSV - EXPORTAR E IMPORTAR */
    public void exportarProduto(HttpServletResponse response) throws Exception {

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

                    rows.getCodigoProduto(),
                    rows.getNome(),
                    formatarPrecoProduto(rows.getPreco()),
                    rows.getUnidadeCaixa().toString(),
                    formatarPesoProduto(rows.getPesoUnidade(), rows.getUnidadeDePeso()),
                    formatarValidadeProduto(rows.getValidade()),
                    rows.getLinhaCategoria().getCodigoLinha(),
                    rows.getLinhaCategoria().getNome(),
                    rows.getLinhaCategoria().getCategoriaProduto().getCodigoCategoria(),
                    rows.getLinhaCategoria().getCategoriaProduto().getNome(),
                    fornecedorService.formatarCnpjFornecedor(rows.getLinhaCategoria().getCategoriaProduto().getFornecedor().getCnpj()),
                    rows.getLinhaCategoria().getCategoriaProduto().getFornecedor().getRazaoSocial()
            });
        }
        LOGGER.info("Finalizando exportação de produto...");
    }

    public List<Produto> importarProduto(MultipartFile file) throws Exception {

        InputStreamReader inputStreamReader = new InputStreamReader(file.getInputStream());
        CSVReader leitor = new CSVReaderBuilder(inputStreamReader).withSkipLines(1).build();

        List<String[]> row = leitor.readAll();
        List<Produto> produtoList = new ArrayList<>();

        for(String[] linha : row){

            String[] vetor = linha[0].replaceAll("\"", "").split(";");

            Produto produto = new Produto();
            boolean valida = findByCodigoProduto(vetor[0]);

            if(valida == false){

                produto.setCodigoProduto(vetor[0]);
                produto.setNome(vetor[1]);
                produto.setPreco(Double.parseDouble(vetor[2].replace("R", "").replace("$", "").replaceAll(",", ".")));

                LinhaCategoriaDTO linhaCategoriaDTO = linhaCategoriaService.findByCodigoLinha(vetor[6]);
                LinhaCategoria linhaCategoria = linhaCategoriaService.converterObjeto(linhaCategoriaDTO);
                produto.setLinhaCategoria(linhaCategoria);

                produto.setUnidadeCaixa(Long.parseLong(vetor[3]));
                produto.setPesoUnidade(Double.parseDouble(vetor[4].replace("m", "").replace("g", "").replace("k", "").replace("K", "").replaceAll(",", "")));

                String dataSemBarra = vetor[5].replaceAll("/", "");
                produto.setValidade(desformatarValidadeProduto(dataSemBarra));

                produto.setUnidadeDePeso(vetor[4].replaceAll("\\d", "").replace(".", ""));

                produtoList.add(produto);

            }else if(valida == true) {
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
            String cnpjFornecedor = fornecedorService.desformatarCnpjFornecedor(vetor[10]);
            // String razaoSocialFornecedor = vetor[11]; -> Não utilizada

            boolean validaProduto = findByCodigoProduto(codigoProduto);
            boolean validaLinha  = linhaCategoriaService.findByCodigo(codigoLinha);
            boolean validaCategoria = categoriaProdutoService.findByCodigo(codigoCategoria);
            boolean validaFornecedor = fornecedorService.findByIdFornecedor(idFornecedor);

            /** FORNECEDOR EXISTE? -> COMPARA 'ID' COM OS 'IDS' CADASTRADO POR PRODUTO */
            if(validaFornecedor == true){

                if(idFornecedor == fornecedorService.findByCnpj(cnpjFornecedor).getId()) {

                /** TEM FORNECEDOR CADASTRADO? -> VALIDA O RESTO... */

                    if(validaCategoria == true){

                        /** TEM CATEGORIA CADASTRADA? -> ATUALIZA CATEGORIA */
                        atualizarCategoriaNoImportarPorFornecedor(codigoCategoria, nomeCategoria, cnpjFornecedor);

                        if(validaLinha == true){

                            /** TEM LINHA CADASTRADA? -> ATUALIZA LINHA */
                            atualizarLinhaNoImportarPorFornecedor(codigoLinha, codigoCategoria, nomeLinha);

                            if(validaProduto == true){

                                /** TEM PRODUTO CADASTRADO? -> ATUALIZA PRODUTO */
                                atualizarProdutoNoImportarPorFornecedor(codigoProduto, nomeProduto, precoProduto, unidadePorCaixa, pesoPorUnidade, unidadeMedida, validadeProduto, codigoLinha);

                            }else if(validaProduto == false){

                                /** NÃO TEM PRODUTO CADASTRADO? -> CADASTRA PRODUTO */
                                salvarProdutoNoImportarPorFornecedor(codigoProduto, nomeProduto, precoProduto, unidadePorCaixa, pesoPorUnidade, unidadeMedida, validadeProduto, codigoLinha);
                            }

                        }else if(validaLinha == false){

                            /** NÃO TEM LINHA CADASTRADA? -> CADASTRA LINHA */
                            salvarLinhaNoImportarPorFornecedor(codigoLinha, codigoCategoria, nomeLinha);
                        }

                    }else if(validaCategoria == false){

                        /** NÃO TEM CATEGORIA CADASTRADA? -> CADASTRA CATEGORIA */
                        salvarCategoriaNoImportarPorFornecedor(codigoCategoria, nomeCategoria, cnpjFornecedor);
                    }

                    LOGGER.info("Fornecedor de id [{}] não pertence ao produto de id do fornecedor [{}]", fornecedorService.findByCnpj(cnpjFornecedor).getId(), idFornecedor);

                }

            }else if(validaFornecedor == false){

                /** NÃO TEM FORNECEDOR CADASTRADO? -> NÃO FAZ NADA */
                throw new IllegalArgumentException(String.format("Fornecedor não cadastrado! id: ", idFornecedor));
            }

        }/** FIM DO FOR/LAÇO */
            LOGGER.info("Finalizando importação CSV por fornecedor...");
    }

    /** PRODUTO - ATIVIDADE 11 */
    public ProdutoDTO salvarProdutoNoImportarPorFornecedor(String codigoProduto, String nome, String preco,
    String unidadeCaixa, String pesoPorUnidade, String unidadeMedida, String validade, String codigoLinha) throws ParseException {

        LOGGER.info("Produto não cadastrado...  código produto: [{}]", codigoProduto);
        LOGGER.info("Cadastrando novo produto...");

        Produto produto = new Produto();
        String codigoFinal = formatarCodigoProduto(codigoProduto);
        produto.setCodigoProduto(codigoFinal);
        produto.setNome(nome);
        produto.setPreco(Double.parseDouble(preco));

        LinhaCategoriaDTO linhaCategoriaDTO = linhaCategoriaService.findByCodigoLinha(codigoLinha);
        LinhaCategoria linhaCategoria = linhaCategoriaService.converterObjeto(linhaCategoriaDTO);
        produto.setLinhaCategoria(linhaCategoria);

        produto.setUnidadeCaixa(Long.parseLong(unidadeCaixa));
        produto.setPesoUnidade(Double.parseDouble(pesoPorUnidade));
        produto.setValidade(desformatarValidadeProduto(validade));
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
        LOGGER.debug("Produto existente: {}", produtoExistente);

        String codigoFinal = formatarCodigoProduto(codigoProduto);
        produtoExistente.setCodigoProduto(codigoFinal);
        produtoExistente.setNome(nome);
        produtoExistente.setPreco(Double.parseDouble(preco));

        LinhaCategoriaDTO linhaCategoriaDTO = linhaCategoriaService.findByCodigoLinha(codigoLinha);
        LinhaCategoria linhaCategoria = linhaCategoriaService.converterObjeto(linhaCategoriaDTO);
        produtoExistente.setLinhaCategoria(linhaCategoria);

        produtoExistente.setUnidadeCaixa(Long.parseLong(unidadeCaixa));
        produtoExistente.setPesoUnidade(Double.parseDouble(pesoPorUnidade));
        produtoExistente.setValidade(desformatarValidadeProduto(validade));
        produtoExistente.setUnidadeDePeso(unidadeMedida);

        produtoExistente = this.iProdutoRepository.save(produtoExistente);
        return ProdutoDTO.of(produtoExistente);
    }

    /** LINHA - ATIVIDADE 11 */
    public LinhaCategoriaDTO salvarLinhaNoImportarPorFornecedor(String codigoLinha, String codigoCategoria, String nome){

        LOGGER.info("Linha de categoria não cadastrada... código linha: [{}]", codigoLinha);
        LOGGER.info("Cadastrando nova linha de categoria...");

        LinhaCategoria linhaCategoria = new LinhaCategoria();
        linhaCategoria.setNome(nome);

        String codigoFinal = linhaCategoriaService.formatarCodigoLinha(codigoLinha);
        linhaCategoria.setCodigoLinha(codigoFinal);

        CategoriaProdutoDTO categoriaProdutoDTO = categoriaProdutoService.findByCodigoCategoria(codigoCategoria);
        CategoriaProduto categoriaProduto = categoriaProdutoService.converterObjeto(categoriaProdutoDTO);
        linhaCategoria.setCategoriaProduto(categoriaProduto);

        linhaCategoria = this.linhaCategoriaService.executarSaveNaRepository(linhaCategoria);
        return LinhaCategoriaDTO.of(linhaCategoria);
    }

    public LinhaCategoriaDTO atualizarLinhaNoImportarPorFornecedor(String codigoLinha, String codigoCategoria, String nome){

        Optional<LinhaCategoria> linhaCategoriaOptionalExistente = linhaCategoriaService.findByCodigoLinhaImportProdutoPorFornecedor(codigoLinha);
        LinhaCategoria linhaCategoria = linhaCategoriaOptionalExistente.get();

        LOGGER.info("Atualizando Linha de Categoria... id: [{}]", linhaCategoria.getId());
        LOGGER.debug("Linha de categoria existente: {}", linhaCategoriaOptionalExistente);

        String codigoFinal = linhaCategoriaService.formatarCodigoLinha(codigoLinha.toUpperCase());
        linhaCategoria.setCodigoLinha(codigoFinal);

        CategoriaProdutoDTO categoriaProdutoDTO = categoriaProdutoService.findByCodigoCategoria(codigoCategoria);
        CategoriaProduto categoriaProduto = categoriaProdutoService.converterObjeto(categoriaProdutoDTO);
        linhaCategoria.setCategoriaProduto(categoriaProduto);

        linhaCategoria.setNome(nome);

        linhaCategoria = this.linhaCategoriaService.executarSaveNaRepository(linhaCategoria);
        return LinhaCategoriaDTO.of(linhaCategoria);
    }

    /** CATEGORIA - ATIVIDADE 11 */
    public CategoriaProdutoDTO salvarCategoriaNoImportarPorFornecedor(String codigoCategoria, String nomeCategoria, String cnpjForneceodor){

        LOGGER.info("Categoria de produto não cadastrada... código de categoria: [{}]", codigoCategoria);
        LOGGER.info("Cadastrando nova categoria de produto...");

        CategoriaProduto categoriaProduto = new CategoriaProduto();
        categoriaProduto.setCodigoCategoria(codigoCategoria);
        categoriaProduto.setNome(nomeCategoria);

        FornecedorDTO fornecedorDTO = fornecedorService.findByCnpj(cnpjForneceodor);
        Fornecedor fornecedor = fornecedorService.converterObjeto(fornecedorDTO);
        categoriaProduto.setFornecedor(fornecedor);

        categoriaProduto = this.categoriaProdutoService.executarSaveNaRepository(categoriaProduto);
        return CategoriaProdutoDTO.of(categoriaProduto);
    }

    public CategoriaProdutoDTO atualizarCategoriaNoImportarPorFornecedor(String codigoCategoria, String nomeCategoria, String cnpjForneceodor){

        Optional<CategoriaProduto> categoriaProdutoOptionalexistente = categoriaProdutoService.findByCodigoCategoriaImportProdutoPorFornecedor(codigoCategoria);
        CategoriaProduto categoriaProdutoExistente = categoriaProdutoOptionalexistente.get();

        LOGGER.info("Atualizando Categoria de Produto... id: [{}]", categoriaProdutoExistente.getId());
        LOGGER.debug("Linha de categoria existente: {}", categoriaProdutoOptionalexistente);

        String codigo = codigoCategoria.substring(codigoCategoria.length() - 3);
        String codigoFinal = categoriaProdutoService.formatarCodigoCategoria(codigo.toUpperCase(), cnpjForneceodor);
        categoriaProdutoExistente.setCodigoCategoria(codigoFinal);

        categoriaProdutoExistente.setNome(nomeCategoria);

        FornecedorDTO fornecedorDTO = fornecedorService.findByCnpj(cnpjForneceodor);
        Fornecedor fornecedor = fornecedorService.converterObjeto(fornecedorDTO);
        categoriaProdutoExistente.setFornecedor(fornecedor);

        categoriaProdutoExistente = this.categoriaProdutoService.executarSaveNaRepository(categoriaProdutoExistente);
        return CategoriaProdutoDTO.of(categoriaProdutoExistente);
    }
}










