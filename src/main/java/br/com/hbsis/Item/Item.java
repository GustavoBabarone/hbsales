package br.com.hbsis.Item;

import br.com.hbsis.pedido.Pedido;
import br.com.hbsis.produto.Produto;

import javax.persistence.*;

@Entity
@Table(name = "seg_item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne /* MUITOS ITENS PARA UM ÚNICO PEDIDO */
    @JoinColumn(name = "id_pedido", referencedColumnName = "id", nullable = false)
    private Pedido pedido;

    @OneToOne /* UM ÚNICO ITEM PARA UM ÚNICO PRODUTO */
    @JoinColumn(name = "nome_produto", referencedColumnName = "nome", nullable = false)
    private Produto produto;

    @Column(name = "quantidade", nullable = false, unique = false)
    private Long quantidade;

    /* GETTER & SETTER */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Long getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Long quantidade) {
        this.quantidade = quantidade;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", pedido=" + pedido +
                ", produto=" + produto +
                ", quantidade=" + quantidade +
                '}';
    }
}
