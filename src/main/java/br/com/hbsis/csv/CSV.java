package br.com.hbsis.csv;

import com.opencsv.*;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;

@Component
public class CSV {

    /** PADRÃO DE EXPORTAÇÃO CSV - CATEGORIA - LINHA - PRODUTO */
    public ICSVWriter padraoExportarCsv(HttpServletResponse response, String arquivoCSV, String[] cabecalhoCSV) throws Exception{

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + arquivoCSV + "\"");

        PrintWriter writer = response.getWriter();
        ICSVWriter icsvWriter = new CSVWriterBuilder(writer).withSeparator(';').withEscapeChar(CSVWriter.DEFAULT_ESCAPE_CHARACTER).withLineEnd(CSVWriter.DEFAULT_LINE_END).build();

        icsvWriter.writeNext(cabecalhoCSV);

        return icsvWriter;
    }

    /** PADRÃO DE IMPORTAÇÃO CSV - CATEGORIA - LINHA - PRODUTO */
    public List<String[]> padraoImportarCsv(MultipartFile file) throws Exception{

        InputStreamReader inputStreamReader = new InputStreamReader(file.getInputStream());
        CSVReader leitor = new CSVReaderBuilder(inputStreamReader).withSkipLines(1).build();

        List<String[]> row = leitor.readAll();

        return row;
    }
}
