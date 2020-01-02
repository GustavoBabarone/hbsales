package br.com.hbsis.periodovendas;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface IPeriodoVendasRepository extends JpaRepository<PeriodoVendas, Long> {

    List<PeriodoVendas> findAllByFornecedor_Id(Long idFornecedor);
}
