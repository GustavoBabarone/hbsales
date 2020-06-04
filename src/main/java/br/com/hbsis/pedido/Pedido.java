package br.com.hbsis.pedido;

import br.com.hbsis.fornecedor.Fornecedor;
import br.com.hbsis.funcionario.Funcionario;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "seg_pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_pedido", nullable = false, length = 10)
    private String codigoPedido;

    @ManyToOne
    @JoinColumn(name = "id_funcionario", referencedColumnName = "id", nullable = false)
    private Funcionario funcionario;

    @ManyToOne
    @JoinColumn(name = "id_fornecedor", referencedColumnName = "id", nullable = false)
    private Fornecedor fornecedor;

    @Column(name = "preco_total", nullable = false)
    private Double precoTotal;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "data_registro", nullable = false)
    private LocalDate dataRegistro = LocalDate.now();

    public Pedido() {
    }

    public Pedido(String codigoPedido, Funcionario funcionario,
                  Fornecedor fornecedor, Double precoTotal, String status) {
        this.codigoPedido = codigoPedido;
        this.funcionario = funcionario;
        this.fornecedor = fornecedor;
        this.precoTotal = precoTotal;
        this.status = status;
    }

    public Long getId() {
        return id;
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
