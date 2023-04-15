package school.sptech.exemplojwt.domain.pedido.submodules.comanda.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import school.sptech.exemplojwt.domain.pedido.submodules.comanda.Comanda;

import java.util.List;

public interface ComandaRepository extends JpaRepository<Comanda, String> {
    List<Comanda> findAllByPedidoId(String idPedido);
}
