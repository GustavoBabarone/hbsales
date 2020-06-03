package br.com.hbsis.categoriaproduto;

import br.com.hbsis.fornecedor.Fornecedor;

import javax.persistence.*;

@Entity
@Table(name = "seg_categorias")
public class CategoriaProduto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo", unique = true, nullable = false, length = 14)
    private String codigoCategoria;

    @ManyToOne
    @JoinColumn(name = "id_fornecedor", referencedColumnName = "id", nullable = false)
    private Fornecedor fornecedor;

    @Column(name = "nome", nullable = false, length = 50)
    private String nome;

    public CategoriaProduto() {
    }

    public CategoriaProduto(Long id) {
        this.id = id;
    }

    public CategoriaProduto(String codigoCategoria, Fornecedor fornecedor, String nome) {
        this.codigoCategoria = codigoCategoria;
        this.fornecedor = fornecedor;
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public String getCodigoCategoria() {
        return codigoCategoria;
    }

    public void setCodigoCategoria(String codigoCategoria) {
        this.codigoCategoria = codigoCategoria;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "CategoriaProduto{" +
                "id=" + id +
                ", codigoCategoria='" + codigoCategoria + '\'' +
                ", fornecedor=" + fornecedor +
                ", nome='" + nome + '\'' +
                '}';
    }
}
