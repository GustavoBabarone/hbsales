package br.com.hbsis.pedido;

import java.time.LocalDate;

public class PedidoDTO {

    private Long id;
    private String codigoPedido;
    private Long idFuncionario;
    private Long idFornecedor;
    private Double precoTotal;
    private String status;
    private LocalDate dataRegistro = LocalDate.now();

    public PedidoDTO() {
    }

    public PedidoDTO(Long id, String codigoPedido, Long idFuncionario, Long idFornecedor,
                     Double precoTotal, String status, LocalDate dataRegistro) {
        this.id = id;
        this.codigoPedido = codigoPedido;
        this.idFuncionario = idFuncionario;
        this.idFornecedor = idFornecedor;
        this.precoTotal = precoTotal;
        this.status = status;
        this.dataRegistro = dataRegistro;
    }

    public static PedidoDTO of(Pedido pedido){
        return new PedidoDTO(
                pedido.getId(),
                pedido.getCodigoPedido(),
                pedido.getFuncionario().getId(),
                pedido.getFornecedor().getId(),
                pedido.getPrecoTotal(),
                pedido.getStatus(),
                pedido.getDataRegistro()
        );
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

    public Long getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(Long idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public Long getIdFornecedor() {
        return idFornecedor;
    }

    public void setIdFornecedor(Long idFornecedor) {
        this.idFornecedor = idFornecedor;
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
        return "PedidoDTO{" +
                "id=" + id +
                ", codigoPedido='" + codigoPedido + '\'' +
                ", idFuncionario=" + idFuncionario +
                ", idFornecedor=" + idFornecedor +
                ", precoTotal=" + precoTotal +
                ", status='" + status + '\'' +
                ", dataRegistro=" + dataRegistro +
                '}';
    }
}
