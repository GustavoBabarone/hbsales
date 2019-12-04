package br.com.hbsis.produto;

import javafx.beans.binding.FloatExpression;

public class ProdutoDTO {

    /* ATRIBUTOS */
    private Long id;
    private Long codigo;
    private String nome;
    private String preco;
    private Long idLinha;
    private Long unidadeCaixa;
    private Float pesoUnidade;
    private String validade;

    /* CONSTRUTORES */
    public ProdutoDTO() {
    }

    public ProdutoDTO(Long id, Long codigo, String nome, String preco,
                      Long idLinha, Long unidadeCaixa, Float pesoUnidade, String validade) {
        this.id = id;
        this.codigo = codigo;
        this.nome = nome;
        this.preco = preco;
        this.idLinha = idLinha;
        this.unidadeCaixa = unidadeCaixa;
        this.pesoUnidade = pesoUnidade;
        this.validade = validade;
    }

    public static ProdutoDTO of(Produto produto){
        return new ProdutoDTO(

                produto.getId(),
                produto.getCodigo(),
                produto.getNome(),
                produto.getPreco(),
                produto.getLinhaCategoria().getId(),
                produto.getUnidadeCaixa(),
                produto.getPesoUnidade(),
                produto.getValidade()
        );
    }

    /* GETTER & SETTER */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPreco() {
        return preco;
    }

    public void setPreco(String preco) {
        this.preco = preco;
    }

    public Long getIdLinha() {
        return idLinha;
    }

    public void setIdLinha(Long idLinha) {
        this.idLinha = idLinha;
    }

    public Long getUnidadeCaixa() {
        return unidadeCaixa;
    }

    public void setUnidadeCaixa(Long unidadeCaixa) {
        this.unidadeCaixa = unidadeCaixa;
    }

    public Float getPesoUnidade() {
        return pesoUnidade;
    }

    public void setPesoUnidade(Float pesoUnidade) {
        this.pesoUnidade = pesoUnidade;
    }

    public String getValidade() {
        return validade;
    }

    public void setValidade(String validade) {
        this.validade = validade;
    }

    @Override
    public String toString() {
        return "ProdutoDTO{" +
                "id=" + id +
                ", codigo=" + codigo +
                ", nome='" + nome + '\'' +
                ", preco='" + preco + '\'' +
                ", idLinha=" + idLinha +
                ", unidadeCaixa=" + unidadeCaixa +
                ", pesoUnidade=" + pesoUnidade +
                ", validade='" + validade + '\'' +
                '}';
    }
}
