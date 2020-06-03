package br.com.hbsis.produto;

import java.util.Date;

public class ProdutoDTO {

    private Long id;
    private String codigoProduto;
    private String nome;
    private Double preco;
    private Long idLinha;
    private Long unidadeCaixa;
    private Double pesoUnidade;
    private String unidadeDePeso;
    private Date validade;

    public ProdutoDTO() {
    }

    public ProdutoDTO(Long id, String codigo, String nome, Double preco,
                      Long idLinha, Long unidadeCaixa, Double pesoUnidade,
                      String unidadeDePeso, Date validade) {
        this.id = id;
        this.codigoProduto = codigo;
        this.nome = nome;
        this.preco = preco;
        this.idLinha = idLinha;
        this.unidadeCaixa = unidadeCaixa;
        this.pesoUnidade = pesoUnidade;
        this.unidadeDePeso = unidadeDePeso;
        this.validade = validade;
    }

    public static ProdutoDTO of(Produto produto){
        return new ProdutoDTO(
                produto.getId(),
                produto.getCodigoProduto(),
                produto.getNome(),
                produto.getPreco(),
                produto.getLinhaCategoria().getId(),
                produto.getUnidadeCaixa(),
                produto.getPesoUnidade(),
                produto.getUnidadeDePeso(),
                produto.getValidade()
        );
    }

    public Long getId() {
        return id;
    }

    public String getCodigoProduto() {
        return codigoProduto;
    }

    public void setCodigoProduto(String codigoProduto) {
        this.codigoProduto = codigoProduto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
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

    public Double getPesoUnidade() {
        return pesoUnidade;
    }

    public void setPesoUnidade(Double pesoUnidade) {
        this.pesoUnidade = pesoUnidade;
    }

    public String getUnidadeDePeso() {
        return unidadeDePeso;
    }

    public void setUnidadeDePeso(String unidadeDePeso) {
        this.unidadeDePeso = unidadeDePeso;
    }

    public Date getValidade() {
        return validade;
    }

    public void setValidade(Date validade) {
        this.validade = validade;
    }

    @Override
    public String toString() {
        return "ProdutoDTO{" +
                "id=" + id +
                ", codigoProduto='" + codigoProduto + '\'' +
                ", nome='" + nome + '\'' +
                ", preco=" + preco +
                ", idLinha=" + idLinha +
                ", unidadeCaixa=" + unidadeCaixa +
                ", pesoUnidade=" + pesoUnidade +
                ", unidadeDePeso='" + unidadeDePeso + '\'' +
                ", validade=" + validade +
                '}';
    }
}
