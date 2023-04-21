package comigo.conta.backend.oficial.domain.pedido.submodules.comanda.repository;

import comigo.conta.backend.oficial.domain.shared.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import comigo.conta.backend.oficial.domain.pedido.submodules.comanda.Comanda;

import java.util.List;

public interface ComandaRepository extends JpaRepository<Comanda, String> {
    List<Comanda> findAllByPedidoId(String idPedido);
    List<Comanda> findAllByPedidoIdAndStatus(String pedido_id, Status status);
}
