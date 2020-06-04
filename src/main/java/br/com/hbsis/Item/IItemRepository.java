package br.com.hbsis.Item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface IItemRepository extends JpaRepository<Item, Long> {
}
