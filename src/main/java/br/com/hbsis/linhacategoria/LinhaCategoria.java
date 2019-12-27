package br.com.hbsis.linhacategoria;

import br.com.hbsis.categoriaproduto.CategoriaProduto;

import javax.persistence.*;

@Entity
@Table(name = "seg_linha_categoria")
public class LinhaCategoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_linha", unique = true, nullable = false, length = 10)
    private String codigoLinha;

    /** MUITAS LINHAS DE CATEGORIA PARA UMA ÚNICA CATEGORIA DE PRODUTO */
    @ManyToOne
    @JoinColumn(name = "id_categoria", referencedColumnName = "id", nullable = false)
    private CategoriaProduto categoriaProduto;

    @Column(name = "nome", nullable = false, length = 50)
    private String nome;

    /** GETTER & SETTER */
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

    public CategoriaProduto getCategoriaProduto() {
        return categoriaProduto;
    }

    public void setCategoriaProduto(CategoriaProduto categoriaProduto) {
        this.categoriaProduto = categoriaProduto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "LinhaCategoria{" +
                "id=" + id +
                ", codigoLinha=" + codigoLinha +
                ", categoriaProduto=" + categoriaProduto +
                ", nome='" + nome + '\'' +
                '}';
    }
}
