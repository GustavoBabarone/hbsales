package br.com.hbsis.periodovendas;

import java.time.LocalDate;

public class PeriodoVendasDTO {

    private Long id;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private LocalDate dataRetirada;
    private Long idFornecedor;
    private String descricao;

    public PeriodoVendasDTO() {
    }

    public PeriodoVendasDTO(Long id, LocalDate dataInicio, LocalDate dataFim, LocalDate dataRetirada,
                            Long idFornecedor, String descricao) {
        this.id = id;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.dataRetirada = dataRetirada;
        this.idFornecedor = idFornecedor;
        this.descricao = descricao;
    }

    public static PeriodoVendasDTO of(PeriodoVendas periodoVendas){
        return new PeriodoVendasDTO(
                periodoVendas.getId(),
                periodoVendas.getDataInicio(),
                periodoVendas.getDataFim(),
                periodoVendas.getDataRetirada(),
                periodoVendas.getFornecedor().getId(),
                periodoVendas.getDescricao()
        );
    }

    public Long getId() {
        return id;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public LocalDate getDataRetirada() {
        return dataRetirada;
    }

    public void setDataRetirada(LocalDate dataRetirada) {
        this.dataRetirada = dataRetirada;
    }

    public Long getIdFornecedor() {
        return idFornecedor;
    }

    public void setIdFornecedor(Long idFornecedor) {
        this.idFornecedor = idFornecedor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return "PeriodoVendasDTO{" +
                "id=" + id +
                ", dataInicio=" + dataInicio +
                ", dataFim=" + dataFim +
                ", dataRetirada=" + dataRetirada +
                ", idFornecedor=" + idFornecedor +
                ", descricao='" + descricao + '\'' +
                '}';
    }
}
