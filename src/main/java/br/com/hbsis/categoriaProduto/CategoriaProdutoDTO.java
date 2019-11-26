package br.com.hbsis.categoriaProduto;

public class CategoriaProdutoDTO {

    /* ATRIBUTOS */
    private Long id;
    private String nomeCategoria;
    private Long idFornecedorCategoria;

    /* CONSTRUTORES */
    public CategoriaProdutoDTO(){
    }

    public CategoriaProdutoDTO(Long id, String nomeCategoria, Long idFornecedorCategoria) {
        this.id = id;
        this.nomeCategoria = nomeCategoria;
        this.idFornecedorCategoria = idFornecedorCategoria;
    }

    public static CategoriaProdutoDTO of(Categoria categoria){
        return new CategoriaProdutoDTO(
                categoria.getId(),
                categoria.getNomeCategoria(),
                categoria.getIdFornecedorCategoria()
        );
    }

    /* GETTER & SETTER */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeCategoria() {
        return nomeCategoria;
    }

    public void setNomeCategoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
    }

    public Long getIdFornecedorCategoria() {
        return idFornecedorCategoria;
    }

    public void setIdFornecedorCategoria(Long idFornecedorCategoria) {
        this.idFornecedorCategoria = idFornecedorCategoria;
    }

    @Override
    public String toString(){
        return "CategoriaProdutoDTO{" +
                "id = " + id +
                ", nomeCategoria = " + nomeCategoria + '\'' +
                ", idFornecedorCategoria = " + idFornecedorCategoria + '\'' +
                "}";
    }

}
