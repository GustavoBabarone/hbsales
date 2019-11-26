package br.com.hbsis.categoriaProduto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * CLASSE RESPONSÁVEL PELA COMUNICAÇÃO COM O BANCO DE DADOS
 */
@Repository
public interface ICategoriaProdutoRepository extends JpaRepository<Categoria, Long> {
}
