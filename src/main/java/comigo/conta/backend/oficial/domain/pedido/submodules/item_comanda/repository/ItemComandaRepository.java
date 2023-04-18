package comigo.conta.backend.oficial.domain.pedido.submodules.item_comanda.repository;

import comigo.conta.backend.oficial.domain.pedido.submodules.item_comanda.ItemComanda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemComandaRepository extends JpaRepository<ItemComanda, String> {
    List<ItemComanda> findAllByComandaId(String idComanda);
}
