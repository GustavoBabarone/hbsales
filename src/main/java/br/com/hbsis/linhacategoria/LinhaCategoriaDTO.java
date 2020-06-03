package br.com.hbsis.linhacategoria;

public class LinhaCategoriaDTO {

    private Long id;
    private String codigoLinha;
    private Long idCategoria;
    private String nome;

    public LinhaCategoriaDTO() {
    }

    public LinhaCategoriaDTO(Long id, String codigoLinha, Long idCategoria, String nome) {
        this.id = id;
        this.codigoLinha = codigoLinha;
        this.idCategoria = idCategoria;
        this.nome = nome;
    }

    public static LinhaCategoriaDTO of(LinhaCategoria linhaCategoria){
        return new LinhaCategoriaDTO(
                linhaCategoria.getId(),
                linhaCategoria.getCodigoLinha(),
                linhaCategoria.getCategoriaProduto().getId(),
                linhaCategoria.getNome()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoLinha() {
        return codigoLinha;
    }

    public void setCodigoLinha(String codigoLinha) {
        this.codigoLinha = codigoLinha;
    }

    public Long getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Long idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "LinhaCategoriaDTO{" +
                "id=" + id +
                ", codigoLinha='" + codigoLinha + '\'' +
                ", idCategoria=" + idCategoria +
                ", nome='" + nome + '\'' +
                '}';
    }
}
