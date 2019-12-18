package br.com.hbsis.periodoVendas;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface IPeriodoVendasRepository extends JpaRepository<PeriodoVendas, Long> {

    @Query(value = "SELECT COUNT(1) FROM seg_periodo_vendas WHERE data_fim >= :dataInicio AND id_fornecedor = :fornecedor", nativeQuery = true)
    Long existeDataAberta(
            @Param("dataInicio")
            LocalDate dataInicio,
            @Param("fornecedor")
            Long fornecedor
    );

}
