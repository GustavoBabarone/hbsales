package br.com.hbsis.categoriaProduto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * CLASSE RESPONSÁVEL PELA COMUNICAÇÃO COM O BANCO DE DADOS
 */
@Repository
public interface ICategoriaProdutoRepository extends JpaRepository<CategoriaProduto, Long> {

    // OBTER DADOS VIA CÓDIGO DA CATEGORIA
    Optional<CategoriaProduto> findByCodigo(String codigo);
}
