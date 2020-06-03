package br.com.hbsis.linhacategoria;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface ILinhaCategoriaRepository extends JpaRepository<LinhaCategoria, Long> {

    Optional<LinhaCategoria> findByCodigoLinha(String codigoLinha);

    boolean existsByCodigoLinha(String codigoLinha);
}
