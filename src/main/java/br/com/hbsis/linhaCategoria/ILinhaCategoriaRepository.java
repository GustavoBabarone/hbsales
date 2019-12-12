package br.com.hbsis.linhaCategoria;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * CLASSE RESPONSÁVEL PELA COMUNICAÇÃO COM O BANCO DE DADOS
 */
@Repository
public interface ILinhaCategoriaRepository extends JpaRepository<LinhaCategoria, Long> {

    // OBTER DADOS VIA CÓDIGO DA LINHA DE CATEGORIA
    Optional<LinhaCategoria> findByCodigo(String codigoLinha);
}
