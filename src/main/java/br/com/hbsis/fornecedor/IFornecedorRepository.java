package br.com.hbsis.fornecedor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Classe responsável pela comunciação com o banco de dados
 */
@Repository
public interface IFornecedorRepository extends JpaRepository<Fornecedor, Long> {

    // MÉTODO DE ENCONTRAR VIA CNPJ
    Optional<Fornecedor> findByCnpj(String cnpj);
}
