package br.com.hbsis.categoriaproduto;

public class CategoriaProdutoDTO {

    /** ATRIBUTOS */
    private Long id;
    private String codigoCategoria;
    private Long idFornecedor;
    private String nome;

    /** CONSTRUTORES */
    public CategoriaProdutoDTO() {
    }

    public CategoriaProdutoDTO(Long id, String codigo, Long idFornecedor, String nome) {
        this.id = id;
        this.codigoCategoria = codigo;
        this.idFornecedor = idFornecedor;
        this.nome = nome;
    }

    public static CategoriaProdutoDTO of(CategoriaProduto categoriaProduto){
        return new CategoriaProdutoDTO(

                categoriaProduto.getId(),
                categoriaProduto.getCodigoCategoria(),
                categoriaProduto.getFornecedor().getId(),
                categoriaProduto.getNome()
        );
    }

    /** GETTER & SETTER */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoCategoria() {
        return codigoCategoria;
    }

    public void setCodigoCategoria(String codigoCategoria) {
        this.codigoCategoria = codigoCategoria;
    }

    public Long getIdFornecedor() {
        return idFornecedor;
    }

    public void setIdFornecedor(Long idFornecedor) {
        this.idFornecedor = idFornecedor;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "CategoriaProdutoDTO{" +
                "id=" + id +
                ", codigo=" + codigoCategoria +
                ", idFornecedor=" + idFornecedor +
                ", nome='" + nome + '\'' +
                '}';
    }
}
