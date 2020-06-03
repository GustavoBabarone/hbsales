package br.com.hbsis.categoriaproduto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface ICategoriaProdutoRepository extends JpaRepository<CategoriaProduto, Long> {

    Optional<CategoriaProduto> findByCodigoCategoria(String codigo);

    boolean existsByCodigoCategoria(String codigo);
}
