package br.com.hbsis.produto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * CLASSE RESPONSÁVEL PELA COMUNICAÇÃO COM O BANCO DE DADOS
 */
@Repository
public interface IProdutoRepository extends JpaRepository<Produto, Long> {

    // MÉTODO DE OBTER DADOS VIA CÓDIGO DO PRODUTO
    Optional<Produto> findByCodigoProduto(String codigo);
}
