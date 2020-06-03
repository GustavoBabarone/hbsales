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

    @ManyToOne
    @JoinColumn(name = "id_categoria", referencedColumnName = "id", nullable = false)
    private CategoriaProduto categoriaProduto;

    @Column(name = "nome", nullable = false, length = 50)
    private String nome;

    public LinhaCategoria() {
    }

    public LinhaCategoria(Long id) {
        this.id = id;
    }

    public LinhaCategoria(String codigoLinha, CategoriaProduto categoriaProduto, String nome) {
        this.codigoLinha = codigoLinha;
        this.categoriaProduto = categoriaProduto;
        this.nome = nome;
    }

    public Long getId() {
        return id;
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
                ", codigoLinha='" + codigoLinha + '\'' +
                ", categoriaProduto=" + categoriaProduto +
                ", nome='" + nome + '\'' +
                '}';
    }
}
