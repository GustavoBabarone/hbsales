package br.com.hbsis.categoriaProduto;

import br.com.hbsis.fornecedor.Fornecedor;

import javax.persistence.*;

@Entity
// DEFINIR NOME DA TABELA QUE CORRESPONDE NO BANCO DA DADOS
@Table(name = "seg_categorias")
public class CategoriaProduto {

    @Id // INSTANCIAR O 'ID' COMO CHAVE PRIMÁRIA

    // DEFINIR O 'ID' COMO AUTO-INCREMENT NO BANCO
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    // ATRIBUTO QUE CORRESPONDE A COLUNA NO BANCO
    private Long id;

    // DEFINIR O NOME DA COLUNA NO BANCO E QUAL ATRIBUTO CORRESPONDE
    @Column(name = "codigo", unique = true, nullable = false)
    private Long codigo;

    // MUITAS CATEGORIAS PARA UM ÚNICO FORNECEDOR
    @ManyToOne
    @JoinColumn(name = "idFornecedor", nullable = false)
    private Fornecedor fornecedor;

    @Column(name = "nome", unique = false, nullable = false, length = 70)
    private String nome;

    /* GETTER & SETTER */
    public Long getId() {
        return id;
    }

    /*public void setId(Long id) {
        this.id = id;
    }*/

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
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

    // ? ? ?
    @Override
    public String toString(){
        return "Categoria{ " +
                "id = "             +   id          +
                ", codigo = "       +    codigo     + '\'' +
                ", idFornecedor = " +   fornecedor  + '\'' +
                ", nome = "         +   nome        + '\'' +
                "}";
    }

}