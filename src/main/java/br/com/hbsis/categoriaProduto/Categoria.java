package br.com.hbsis.categoriaProduto;

import br.com.hbsis.fornecedor.Fornecedor;

import javax.persistence.*;

@Entity

// DEFINIR NOME DA TABELA QUE CORRESPONDE NO BANCO DA DADOS
@Table(name = "seg_categorias")
public class Categoria {

    @Id // INSTANCIAR O 'ID' COMO CHAVE PRIMÁRIA

    // DEFINIR O 'ID' COMO AUTO-INCREMENT NO BANCO
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    // ATRIBUTO QUE CORRESPONDE A COLUNA NO BANCO
    private Long id;

    // DEFINIR O NOME DA COLUNA NO BANCO E QUAL ATRIBUTO CORRESPONDE
    @Column(name = "nomeCategoria", unique = false, nullable = false, length = 70)
    private String nomeCategoria;

    // MUITAS CATEGORIAS PARA UM ÚNICO FORNECEDOR
    @ManyToOne
    @JoinColumn(name = "idFornecedorCategoria", nullable = false)
    private Fornecedor fornecedor;

    /* GETTER */
    public Long getId() {
        return id;
    }

    public String getNomeCategoria() {
        return nomeCategoria;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    /* SETTER */
    public void setId(Long id) {
        this.id = id;
    }

    public void setNomeCategoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    // ? ? ?
    @Override
    public String toString(){
        return "Categoria{ " +
                "id = " +  id +
                ", nomeCategoria = " + nomeCategoria + '\'' +
                ", idFornecedorCategoria = " + fornecedor + '\'' +
                "}";
    }

}
