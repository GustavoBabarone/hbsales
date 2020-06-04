package br.com.hbsis.periodovendas;

import br.com.hbsis.fornecedor.Fornecedor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "seg_periodo_vendas")
public class PeriodoVendas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_inicio", unique = false, length = 10, nullable = false)
    private LocalDate dataInicio;

    @Column(name = "data_fim", unique = false, length = 10, nullable = false)
    private LocalDate dataFim;

    @ManyToOne
    @JoinColumn(name = "id_fornecedor", referencedColumnName = "id", nullable = false)
    private Fornecedor fornecedor;

    @Column(name = "data_retirada", unique = false, length = 10, nullable = false)
    private LocalDate dataRetirada;

    @Column(name = "descricao", unique = false, length = 50, nullable = false)
    private String descricao;

    public PeriodoVendas() {
    }

    public PeriodoVendas(LocalDate dataInicio, LocalDate dataFim, Fornecedor fornecedor, LocalDate dataRetirada, String descricao) {
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.fornecedor = fornecedor;
        this.dataRetirada = dataRetirada;
        this.descricao = descricao;
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

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public LocalDate getDataRetirada() {
        return dataRetirada;
    }

    public void setDataRetirada(LocalDate dataRetirada) {
        this.dataRetirada = dataRetirada;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return "PeriodoVendas{" +
                "id=" + id +
                ", dataInicio=" + dataInicio +
                ", dataFim=" + dataFim +
                ", fornecedor=" + fornecedor +
                ", dataRetirada=" + dataRetirada +
                ", descricao='" + descricao + '\'' +
                '}';
    }
}
