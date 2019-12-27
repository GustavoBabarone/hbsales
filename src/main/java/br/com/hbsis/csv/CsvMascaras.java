package br.com.hbsis.csv;

import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class CsvMascaras {

    /** CATEGORIAS */
    public String formatarCnpjFornecedor(String cnpj){

        String cnpjFormatado =  cnpj.substring(0, 2)+ "."+cnpj.substring(2, 5)+"."+
                                cnpj.substring(5, 8)+ "/"+cnpj.substring(8, 12)+"-"+
                                cnpj.substring(12, 14);
        return cnpjFormatado;
    }

    public String desformatarCnpjFornecedor(String cnpj) {

        String cnpjDesformatado = ""+cnpj.charAt(0)+cnpj.charAt(1)+cnpj.charAt(3)+cnpj.charAt(4)+cnpj.charAt(5)+
                                    cnpj.charAt(7)+cnpj.charAt(8)+cnpj.charAt(9)+cnpj.charAt(11)+cnpj.charAt(12)+
                                    cnpj.charAt(13)+cnpj.charAt(14)+cnpj.charAt(16)+cnpj.charAt(17);
        return cnpjDesformatado;
    }

    /** PRODUTOS */
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
}
