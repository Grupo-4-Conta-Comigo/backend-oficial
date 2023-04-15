package school.sptech.exemplojwt.domain.pedido.submodules.item_comanda.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import school.sptech.exemplojwt.domain.pedido.submodules.item_comanda.ItemComanda;

import java.util.List;

public interface ItemComandaRepository extends JpaRepository<ItemComanda, String> {
    List<ItemComanda> findAllByComandaId(String idComanda);
}
