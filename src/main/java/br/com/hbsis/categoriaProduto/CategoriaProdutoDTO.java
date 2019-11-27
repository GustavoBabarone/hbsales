package br.com.hbsis.categoriaProduto;

import br.com.hbsis.fornecedor.Fornecedor;

public class CategoriaProdutoDTO {

    /* ATRIBUTOS */
    private Long codigoCategoria;
    private Long idFornecedorCategoria;
    private String nomeCategoria;

    /* CONSTRUTORES INICIO */
    public CategoriaProdutoDTO(){
    }

    public CategoriaProdutoDTO(Long codigoCategoria, Long idFornecedorCategoria, String nomeCategoria) {
        this.codigoCategoria = codigoCategoria;
        this.idFornecedorCategoria = idFornecedorCategoria;
        this.nomeCategoria = nomeCategoria;
    }
    /* CONSTRUTORES FIM */

    /* GETTER & SETTER INICIO */
    public Long getCodigoCategoria() {
        return codigoCategoria;
    }

    public void setCodigoCategoria(Long codigoCategoria) {
        this.codigoCategoria = codigoCategoria;
    }

    public Long getIdFornecedorCategoria() {
        return idFornecedorCategoria;
    }

    public void setIdFornecedorCategoria(Long idFornecedorCategoria) {
        this.idFornecedorCategoria = idFornecedorCategoria;
    }

    public String getNomeCategoria() {
        return nomeCategoria;
    }

    public void setNomeCategoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
    }
    /* GETTER & SETTER FIM */

    // ? ? ?
    @Override
    public String toString(){
        return "CategoriaProdutoDTO{" +
                "codigoCategoria = " + codigoCategoria +
                ", nomeCategoria = " + nomeCategoria + '\'' +
                ", idFornecedorCategoria = " + idFornecedorCategoria + '\'' +
                "}";
    }

}
