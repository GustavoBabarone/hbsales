package br.com.hbsis.pedido;

import br.com.hbsis.fornecedor.Fornecedor;
import br.com.hbsis.funcionario.Funcionario;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "seg_pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_pedido", nullable = false, length = 10)
    private String codigoPedido;

    @ManyToOne /* MUITOS PEDIDOS PARA UM ÚNICO FUNCIONÁRIO */
    @JoinColumn(name = "id_funcionario", referencedColumnName = "id", nullable = false)
    private Funcionario funcionario;

    @ManyToOne /* UM ÚNICO FORNECEODOR PARA UM ÚNICO PEDIDO */
    @JoinColumn(name = "id_fornecedor", referencedColumnName = "id", nullable = false)
    private Fornecedor fornecedor;

    @Column(name = "preco_total", nullable = false)
    private Double precoTotal;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "data_registro", nullable = false)
    private LocalDate dataRegistro;

    /* GETTER & SETTER */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoPedido() {
        return codigoPedido;
    }

    public void setCodigoPedido(String codigoPedido) {
        this.codigoPedido = codigoPedido;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public Double getPrecoTotal() {
        return precoTotal;
    }

    public void setPrecoTotal(Double precoTotal) {
        this.precoTotal = precoTotal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(LocalDate dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + id +
                ", codigoPedido='" + codigoPedido + '\'' +
                ", funcionario=" + funcionario +
                ", fornecedor=" + fornecedor +
                ", precoTotal=" + precoTotal +
                ", status='" + status + '\'' +
                ", dataRegistro=" + dataRegistro +
                '}';
    }
}
