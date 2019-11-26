package br.com.hbsis.categoriaProduto;

import javax.persistence.*;

@Entity
@Table(name = "seg_categorias")
class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nomeCategoria", unique = false, nullable = false, length = 70)
    private String nomeCategoria;
    @Column(name = "idFornecedorCategoria", unique = true, nullable = false, length = 999999)
    private Long idFornecedorCategoria;

    /* GETTER */
    public Long getId() {
        return id;
    }

    public String getNomeCategoria() {
        return nomeCategoria;
    }

    public Long getIdFornecedorCategoria() {
        return idFornecedorCategoria;
    }

    /* SETTER */
    public void setId(Long id) {
        this.id = id;
    }

    public void setNomeCategoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
    }

    public void setIdFornecedorCategoria(Long idFornecedorCategoria) {
        this.idFornecedorCategoria = idFornecedorCategoria;
    }

    @Override
    public String toString(){
        return "Categoria{ " +
                "id = " +  id +
                ", nomeCategoria = " + nomeCategoria + '\'' +
                ", idFornecedorCategoria = " + idFornecedorCategoria + '\'' +
                "}";
    }

}
