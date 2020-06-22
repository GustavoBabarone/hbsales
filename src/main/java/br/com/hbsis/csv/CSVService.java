package br.com.hbsis.csv;

import br.com.hbsis.categoriaproduto.CategoriaProduto;
import br.com.hbsis.categoriaproduto.CategoriaProdutoService;
import br.com.hbsis.linhacategoria.LinhaCategoria;
import br.com.hbsis.produto.Produto;
import com.opencsv.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;

@Service
public class CSVService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoriaProdutoService.class);

    public <T> ICSVWriter exportarCsv(
            HttpServletResponse response, Class<T> entidadeGenerica) throws IOException {

        String[] cabecalho = this.definirHttpHeaderECabecalhoCsv(response, entidadeGenerica);
        return this.construirEscritorCsvEAdicionarCabecalho(response, cabecalho);
    }

    public ICSVWriter construirEscritorCsvEAdicionarCabecalho(
            HttpServletResponse response, String[] cabecalho) throws IOException {

        PrintWriter writer = response.getWriter();
        ICSVWriter csvWriter = new CSVWriterBuilder(writer)
                .withSeparator(';')
                .withEscapeChar(CSVWriter.DEFAULT_ESCAPE_CHARACTER)
                .withLineEnd(CSVWriter.DEFAULT_LINE_END)
                .build();

        csvWriter.writeNext(cabecalho);
        return csvWriter;
    }

    public <T> String[] definirHttpHeaderECabecalhoCsv(HttpServletResponse response, Class<T> entidade) {

        String nomeArquivo = "";
        String[] cabecalho = {};
        int status = 200;

        response.setContentType("text/csv");

        if (entidade.equals(CategoriaProduto.class)) {
            nomeArquivo = "categoriaProduto.csv";
            cabecalho = new String[]{"codigo", "nome", "razao_social", "cnpj"};

        } else if (entidade.equals(LinhaCategoria.class)) {
            nomeArquivo = "linhaCategoria.csv";
            cabecalho = new String[]{"codigo_linha", "nome_linha", "codigo_categoria", "nome_categoria"};

        } else if (entidade.equals(Produto.class)) {
            nomeArquivo = "produtos.csv";
            cabecalho = new String[]{"codigo", "nome", "preco", "unidade_caixa", "peso_unidade",
                    "validade", "codigo_linha", "nome_linha", "codigo_categoria", "nome_categoria",
                    "cnpj_fornecedor", "razao_social_fornecedor"};

        } else {
            LOGGER.info("Entidade [{}] não prevista...", entidade.getName());
            status = 400;
        }

        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nomeArquivo + "\"");
        response.setStatus(status);
        return cabecalho;
    }

    public List<String[]> padraoImportarCsv(MultipartFile file) throws Exception{

        InputStreamReader inputStreamReader = new InputStreamReader(file.getInputStream());
        CSVReader leitor = new CSVReaderBuilder(inputStreamReader).withSkipLines(1).build();

        List<String[]> row = leitor.readAll();

        return row;
    }
}
