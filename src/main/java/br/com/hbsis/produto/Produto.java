package br.com.hbsis.produto;

import br.com.hbsis.linhaCategoria.LinhaCategoria;

import javax.persistence.*;

@Entity // DEFINIR NOME DA TABELA QUE CORRESPONDE NO BANCO DE DADOS
@Table(name = "seg_produtos")
public class Produto {

    @Id // 'id' COMO CHAVE PRIMÁRIA E COM AUTO-INCREMENT NO BANCO
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo", unique = true, nullable = false, length = 10)
    private String codigo;

    @Column(name = "nome", unique = false, nullable = false, length = 200)
    private String nome;

    @Column(name = "preco", unique = false, nullable = false, length = 70)
    private Double preco;

    @ManyToOne
    @JoinColumn(name = "id_linha", referencedColumnName = "id", nullable = false)
    private LinhaCategoria linhaCategoria;

    @Column(name = "unidade_caixa", unique = false, nullable = false)
    private Long unidadeCaixa;

    @Column(name = "peso_unidade", unique = false, nullable = false)
    private Double pesoUnidade;

    @Column(name = "unidade_peso", unique = false, nullable = false, length = 2)
    private String unidadeDePeso;

    @Column(name = "validade", unique = false, nullable = false, length = 10)
    private String validade;

    /* GETTER & SETTER */
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public LinhaCategoria getLinhaCategoria() {
        return linhaCategoria;
    }

    public void setLinhaCategoria(LinhaCategoria linhaCategoria) {
        this.linhaCategoria = linhaCategoria;
    }

    public Long getUnidadeCaixa() {
        return unidadeCaixa;
    }

    public void setUnidadeCaixa(Long unidadeCaixa) {
        this.unidadeCaixa = unidadeCaixa;
    }

    public Double getPesoUnidade() {
        return pesoUnidade;
    }

    public void setPesoUnidade(Double pesoUnidade) {
        this.pesoUnidade = pesoUnidade;
    }

    public String getUnidadeDePeso() {
        return unidadeDePeso;
    }

    public void setUnidadeDePeso(String unidadeDePeso) {
        this.unidadeDePeso = unidadeDePeso;
    }

    public String getValidade() {
        return validade;
    }

    public void setValidade(String validade) {
        this.validade = validade;
    }

    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", codigo='" + codigo + '\'' +
                ", nome='" + nome + '\'' +
                ", preco=" + preco +
                ", linhaCategoria=" + linhaCategoria +
                ", unidadeCaixa=" + unidadeCaixa +
                ", pesoUnidade=" + pesoUnidade +
                ", unidadeDePeso='" + unidadeDePeso + '\'' +
                ", validade='" + validade + '\'' +
                '}';
    }
}
