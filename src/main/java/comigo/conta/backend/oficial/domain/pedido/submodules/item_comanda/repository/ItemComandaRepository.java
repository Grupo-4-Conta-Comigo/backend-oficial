package comigo.conta.backend.oficial.domain.pedido.submodules.item_comanda.repository;

import comigo.conta.backend.oficial.domain.pedido.submodules.item_comanda.ItemComanda;
import comigo.conta.backend.oficial.domain.produto.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemComandaRepository extends JpaRepository<ItemComanda, String> {
    List<ItemComanda> findByComanda_IdAndProduto_Categoria(String id, Categoria categoria);
    List<ItemComanda> findAllByComandaId(String idComanda);
}
