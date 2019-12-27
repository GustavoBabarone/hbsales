package br.com.hbsis.csv;

import br.com.hbsis.categoriaproduto.CategoriaProduto;
import br.com.hbsis.categoriaproduto.CategoriaProdutoDTO;
import br.com.hbsis.categoriaproduto.CategoriaProdutoService;
import br.com.hbsis.fornecedor.Fornecedor;
import br.com.hbsis.fornecedor.FornecedorDTO;
import br.com.hbsis.fornecedor.FornecedorService;
import br.com.hbsis.linhacategoria.LinhaCategoria;
import br.com.hbsis.linhacategoria.LinhaCategoriaDTO;
import br.com.hbsis.linhacategoria.LinhaCategoriaService;
import br.com.hbsis.produto.Produto;
import br.com.hbsis.produto.ProdutoService;
import com.opencsv.*;
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
public class CsvService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CsvService.class);
    private final CsvMascaras csvMascaras;

    private final FornecedorService fornecedorService;
    private final CategoriaProdutoService categoriaProdutoService;
    private final LinhaCategoriaService linhaCategoriaService;
    private final ProdutoService produtoService;

    @Autowired /** CONTRUTOR */
    public CsvService(CsvMascaras csvMascaras, FornecedorService fornecedorService, CategoriaProdutoService categoriaProdutoService, LinhaCategoriaService linhaCategoriaService, ProdutoService produtoService) {
        this.csvMascaras = csvMascaras;
        this.fornecedorService = fornecedorService;
        this.categoriaProdutoService = categoriaProdutoService;
        this.linhaCategoriaService = linhaCategoriaService;
        this.produtoService = produtoService;
    }

    /** CATEGORIAS */
    public void exportarCategoria(HttpServletResponse response) throws Exception {

        String arquivoCSV = "categoriaProduto.csv";
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + arquivoCSV + "\"");

        PrintWriter writer = response.getWriter();
        ICSVWriter icsvWriter = new CSVWriterBuilder(writer).withSeparator(';').withEscapeChar(CSVWriter.DEFAULT_ESCAPE_CHARACTER).withLineEnd(CSVWriter.DEFAULT_LINE_END).build();

        String[] cabecalhoCSV = {"codigo", "nome", "razao_social", "cnpj"};
        icsvWriter.writeNext(cabecalhoCSV);

        for(CategoriaProduto rows : categoriaProdutoService.obterCategorias()){

            icsvWriter.writeNext(new String[]{
                    rows.getCodigoCategoria(),
                    rows.getNome(),
                    rows.getFornecedor().getRazaoSocial(),
                    csvMascaras.formatarCnpjFornecedor(rows.getFornecedor().getCnpj()),
            });
        }
            LOGGER.info("Finalizando exportação de produto...");
    }

    public List<CategoriaProduto> importarCategoria(MultipartFile file) throws Exception {

        InputStreamReader inputStreamReader = new InputStreamReader(file.getInputStream());
        CSVReader leitor = new CSVReaderBuilder(inputStreamReader).withSkipLines(1).build();

        List<String[]> row = leitor.readAll();
        List<CategoriaProduto> categoriaProdutoList = new ArrayList<>();

        for(String[] linha : row){

            String[] vetor = linha[0].replaceAll("\"", "").split(";");

            CategoriaProduto categoriaProduto = new CategoriaProduto();
            boolean valida = categoriaProdutoService.findByCodigo(vetor[0]);

            if(valida == false) {

                categoriaProduto.setCodigoCategoria(vetor[0]);
                categoriaProduto.setNome(vetor[1]);

                String cnpjDesformatado = csvMascaras.desformatarCnpjFornecedor(vetor[3]);
                FornecedorDTO fornecedorDTO = fornecedorService.findByCnpj(cnpjDesformatado);
                Fornecedor fornecedor = fornecedorService.converterObjeto(fornecedorDTO);
                categoriaProduto.setFornecedor(fornecedor);

                categoriaProdutoList.add(categoriaProduto);

            }else if(valida == true){
                LOGGER.info("Categoria já existente no banco de dados...");
            }
        }
        LOGGER.info("Finalizando importação de categoria...");
        return categoriaProdutoService.salvarCategorias(categoriaProdutoList);
    }

    /** LINHAS DE CATEGORIA */
    public void exportarLinha(HttpServletResponse response) throws  Exception {

        String arquivoCSV = "linhaCategoria.csv";
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + arquivoCSV + "\"");

        PrintWriter writer = response.getWriter();
        ICSVWriter icsvWriter = new CSVWriterBuilder(writer).withSeparator(';').withEscapeChar(CSVWriter.DEFAULT_ESCAPE_CHARACTER).withLineEnd(CSVWriter.DEFAULT_LINE_END).build();

        String[] cabecalhoCSV = {"codigo_linha", "nome_linha", "codigo_categoria", "nome_categoria"};
        icsvWriter.writeNext(cabecalhoCSV);

        for(LinhaCategoria rows : linhaCategoriaService.obterLinhas()){

            icsvWriter.writeNext(new String[]{
                    rows.getCodigoLinha(),
                    rows.getNome(),
                    rows.getCategoriaProduto().getCodigoCategoria(),
                    rows.getCategoriaProduto().getNome()
            });
        }
            LOGGER.info("Finalizando exportação de produto...");
    }

    public List<LinhaCategoria> importarLinha(MultipartFile file) throws Exception {

        InputStreamReader inputStreamReader = new InputStreamReader(file.getInputStream());
        CSVReader leitor = new CSVReaderBuilder(inputStreamReader).withSkipLines(1).build();

        List<String[]> row = leitor.readAll();
        List<LinhaCategoria> linhaCategoriaList = new ArrayList<>();

        for(String[] linha : row){

            String[] vetor = linha[0].replaceAll("\"", "").split(";");

            LinhaCategoria linhaCategoria = new LinhaCategoria();
            boolean valida = linhaCategoriaService.findByCodigo(vetor[0]);

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
        return linhaCategoriaService.salvarLinhas(linhaCategoriaList);
    }

    /** PRODUTOS */
    public void exportarProduto(HttpServletResponse response) throws Exception {

        String arquivoCSV = "produtos.csv";
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + arquivoCSV + "\"");

        PrintWriter writer = response.getWriter();
        ICSVWriter icsvWriter = new CSVWriterBuilder(writer).withSeparator(';').withEscapeChar(CSVWriter.DEFAULT_ESCAPE_CHARACTER).withLineEnd(CSVWriter.DEFAULT_LINE_END).build();

        String[] cabecalhoCSV = {"codigo", "nome", "preco", "unidade_caixa", "peso_unidade", "validade",
                "codigo_linha", "nome_linha", "codigo_categoria", "nome_categoria", "cnpj_fornecedor", "razao_social_fornecedor"};
        icsvWriter.writeNext(cabecalhoCSV);

        for(Produto rows : produtoService.obterProdutos()){

            icsvWriter.writeNext(new String[]{

                    rows.getCodigoProduto(),
                    rows.getNome(),
                    csvMascaras.formatarPrecoProduto(rows.getPreco()),
                    rows.getUnidadeCaixa().toString(),
                    csvMascaras.formatarPesoProduto(rows.getPesoUnidade(), rows.getUnidadeDePeso()),
                    csvMascaras.formatarValidadeProduto(rows.getValidade()),
                    rows.getLinhaCategoria().getCodigoLinha(),
                    rows.getLinhaCategoria().getNome(),
                    rows.getLinhaCategoria().getCategoriaProduto().getCodigoCategoria(),
                    rows.getLinhaCategoria().getCategoriaProduto().getNome(),
                    csvMascaras.formatarCnpjFornecedor(rows.getLinhaCategoria().getCategoriaProduto().getFornecedor().getCnpj()),
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
            boolean valida = produtoService.findByCodigoProduto(vetor[0]);

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
                produto.setValidade(csvMascaras.desformatarValidadeProduto(dataSemBarra));

                produto.setUnidadeDePeso(vetor[4].replaceAll("\\d", "").replace(".", ""));

                produtoList.add(produto);

            }else if(valida == true) {
                LOGGER.info("Produto já existente no banco de dados...");
            }
        }
        LOGGER.info("Finalizando importação de produtos...");
        return produtoService.salvarProdutos(produtoList);
    }

    /** IMPORTAR PRODUTOS POR FORNECEDOR*/
//    public void importarProdutoPorFornecedor(MultipartFile file, Long id) throws Exception {
//
//        InputStreamReader inputStreamReader = new InputStreamReader(file.getInputStream());
//        CSVReader csvReader = new CSVReaderBuilder(inputStreamReader).withSkipLines(1).build();
//
//        List<String[]> row = csvReader.readAll();
//
//        for(String[] linha : row){
//
//            String[] vetor = linha[0].replaceAll("\"", "").split(";");
//
//            // VALIDAR SE Fornecedor - Categoria - Linha - Produto EXISTEM
//            boolean validaProduto = findByCodigoProduto(vetor[0]);
//            boolean validaLinha  = linhaCategoriaService.findByCodigo(vetor[6]);
//            boolean validaCategoria = categoriaProdutoService.findByCodigo(vetor[8]);
//            boolean validaFornecedor = fornecedorService.findByIdFornecedor(id);
//
//            if(validaFornecedor == true){
//
//                // TEM FORNECEDOR CADASTRADO? -> VALIDA O RESTO
//
//                if(validaCategoria == true){
//
//                    // TEM CATEGORIA CADASTRADA? -> ATUALIZA
//                    LOGGER.info("Categoria já cadastrada... código categoria: [{}]", vetor[8]);
//                    LOGGER.info("Atualizando categoria...");
//
//                    Optional<CategoriaProduto> categoriaProdutoOptional = this.iCategoriaProdutoRepository.findByCodigoCategoria(vetor[8]);
//                    CategoriaProduto categoriaProdutoExistente = categoriaProdutoOptional.get();
//
//                    categoriaProdutoExistente.setCodigoCategoria(vetor[8]);
//
//                    FornecedorDTO fornecedorDTO = fornecedorService.findByCnpj(categoriaProdutoService.desformatarCnpj(vetor[10]));
//                    Fornecedor fornecedor = categoriaProdutoService.converterObjeto(fornecedorDTO);
//                    categoriaProdutoExistente.setFornecedor(fornecedor);
//
//                    categoriaProdutoExistente.setNome(vetor[7]);
//
//                    iCategoriaProdutoRepository.save(categoriaProdutoExistente);
//
//                    if(validaLinha == true){
//
//                        // TEM LINHA CADASTRADA? -> ATUALIZA
//                        LOGGER.info("Linha já cadastrada... código linha: [{}]", vetor[6]);
//                        LOGGER.info("Atualizando linha...");
//
//                        Optional<LinhaCategoria> linhaCategoriaOptional = this.iLinhaCategoriaRepository.findByCodigoLinha(vetor[6]);
//                        LinhaCategoria linhaCategoriaExistente = linhaCategoriaOptional.get();
//
//                        linhaCategoriaExistente.setCodigoLinha(vetor[6]);
//                        linhaCategoriaExistente.setCategoriaProduto(categoriaProdutoExistente); // CATEGORIA ATUALIZADA (EXISTENTE)
//                        linhaCategoriaExistente.setNome(vetor[7]);
//
//                        iLinhaCategoriaRepository.save(linhaCategoriaExistente);
//
//                        if(validaProduto == true){
//
//                            // TEM PRODUTO CADASTRADO? -> ATUALIZA
//                            LOGGER.info("Produto já cadastrado... código: [{}]", vetor[0]);
//                            LOGGER.info("Atualizando produto...");
//
//                            Optional<Produto> produtoOptional = this.iProdutoRepository.findByCodigoProduto(vetor[0]);
//                            Produto produtoExistente = produtoOptional.get();
//
//                            String codigoExcel = vetor[0];
//                            String codigoUpperCase = codigoExcel.toUpperCase();
//                            String codigoProcessado = codigoZerosEsquerda(codigoUpperCase);
//                            produtoExistente.setCodigoProduto(codigoProcessado);
//
//                            produtoExistente.setNome(vetor[1]);
//                            produtoExistente.setPreco(Double.parseDouble(vetor[2].replace("R", "").replace("$", "").replace(",", ".")));
//
//                            produtoExistente.setLinhaCategoria(linhaCategoriaExistente); // LINHA ATUALIZADA (EXISTENTE)
//
//                            produtoExistente.setUnidadeCaixa(Long.parseLong(vetor[3]));
//                            produtoExistente.setPesoUnidade(Double.parseDouble(vetor[4].replace("m", "").replace("g", "").replace("K", "").replace("k", "").replace(",", ".")));
//
//                            String validadeCSV = vetor[5];
//                            String dataSemBarra = validadeCSV.replaceAll("/", "");
//                            produtoExistente.setValidade(desformatarValidade(dataSemBarra));
//
//                            produtoExistente.setUnidadeDePeso(vetor[4].replaceAll("\\d", "").replace(".", ""));
//
//                            iProdutoRepository.save(produtoExistente);
//
//                        }else if(validaProduto == false){
//
//                            // NÃO TEM PRODUTO CADASTRADO? -> CADASTRA
//                            LOGGER.info("Produto não cadastrado...  código produto: [{}]", vetor[0]);
//                            LOGGER.info("Cadastrando novo produto...");
//
//                            Produto produto = new Produto();
//
//                            String codigoExcel = vetor[0];
//                            String codigoUpperCase = codigoExcel.toUpperCase();
//                            String codigoProcessado = codigoZerosEsquerda(codigoUpperCase);
//                            produto.setCodigoProduto(codigoProcessado);
//
//                            produto.setNome(vetor[1]);
//                            produto.setPreco(Double.parseDouble(vetor[2].replace("R", "").replace("$", "").replace(",", ".")));
//
//                            produto.setLinhaCategoria(linhaCategoriaExistente); // LINHA ATUALIZADA (EXISTENTE)
//
//                            produto.setUnidadeCaixa(Long.parseLong(vetor[3]));
//                            produto.setPesoUnidade(Double.parseDouble(vetor[4].replace("m", "").replace("g", "").replace("k", "").replace("K", "").replace(",", ".")));
//
//                            String validadeCSV = vetor[5];
//                            String dataSemBarra = validadeCSV.replaceAll("/", "");
//
//                            produto.setValidade(desformatarValidade(dataSemBarra));
//                            produto.setUnidadeDePeso(vetor[4].replaceAll("\\d", "").replace(".", ""));
//
//                            iProdutoRepository.save(produto);
//                        }
//
//                    }else if(validaLinha == false){
//
//                        // NÃO TEM LINHA CADASTRADA? -> CADASTRA
//                        LOGGER.info("Linha de categoria não cadastrada... código linha: [{}]", vetor[6]);
//                        LOGGER.info("Cadastrando nova linha de categoria...");
//
//                        LinhaCategoria linhaCategoria = new LinhaCategoria();
//
//                        linhaCategoria.setCodigoLinha(vetor[6]);
//                        linhaCategoria.setCategoriaProduto(categoriaProdutoExistente); // CATEGORIA ATUALIZADA (EXISTENTE)
//                        linhaCategoria.setNome(vetor[7]);
//
//                        iLinhaCategoriaRepository.save(linhaCategoria);
//
//                    }
//
//                }else if(validaCategoria == false){
//
//                    // NÃO TEM CATEGORIA CADASTRADA? -> CADASTRA
//                    LOGGER.info("Categoria de produto não cadastrada... código de categoria: [{}]", vetor[8]);
//                    LOGGER.info("Cadastrando nova categoria de produto...");
//
//                    CategoriaProduto categoriaProduto = new CategoriaProduto();
//                    categoriaProduto.setCodigoCategoria(vetor[8]);
//
//                    FornecedorDTO fornecedorDTO = fornecedorService.findByCnpj(vetor[10]);
//                    Fornecedor fornecedor = categoriaProdutoService.converterObjeto(fornecedorDTO);
//                    categoriaProduto.setFornecedor(fornecedor);
//
//                    categoriaProduto.setNome(vetor[9]);
//
//                    iCategoriaProdutoRepository.save(categoriaProduto);
//
//                }
//
//            }else if(validaFornecedor == false){
//
//                // NÃO TEM FORNECEDOR CADASTRADO? -> NÃO FAZ NADA
//                LOGGER.info("Fornecedor não cadastrado! id: [{}]", id);
//                throw new IllegalArgumentException("Fornecedor não cadastrado!");
//            }
//
//        }// FIM DO FOR/LAÇO
//
//        LOGGER.info("Finalizando importação CSV por fornecedor...");
//
//    }
}
