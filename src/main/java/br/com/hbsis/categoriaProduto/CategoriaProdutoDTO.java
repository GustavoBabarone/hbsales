package br.com.hbsis.categoriaProduto;

public class CategoriaProdutoDTO {

    /* ATRIBUTOS */
    private Long id;
    private String codigo;
    private Long idFornecedor;
    private String nome;

    /* CONSTRUTORES INICIO */
    public CategoriaProdutoDTO() {
    }

    public CategoriaProdutoDTO(Long id, String codigo, Long idFornecedor, String nome) {
        this.id = id;
        this.codigo = codigo;
        this.idFornecedor = idFornecedor;
        this.nome = nome;
    }

    public static CategoriaProdutoDTO of(CategoriaProduto categoriaProduto){
        return new CategoriaProdutoDTO(

                categoriaProduto.getId(),
                categoriaProduto.getCodigo(),
                categoriaProduto.getFornecedor().getId(),// * TESTE *
                categoriaProduto.getNome()
        );
    }
    /* CONSTRUTORES FIM */

    /* GETTER & SETTER INICIO */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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
    /* GETTER & SETTER FIM */

    @Override
    public String toString() {
        return "CategoriaProdutoDTO{" +
                "id=" + id +
                ", codigo=" + codigo +
                ", idFornecedor=" + idFornecedor +
                ", nome='" + nome + '\'' +
                '}';
    }
}
