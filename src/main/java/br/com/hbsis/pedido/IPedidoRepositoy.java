package br.com.hbsis.pedido;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface IPedidoRepositoy extends JpaRepository<Pedido, Long> {
}
