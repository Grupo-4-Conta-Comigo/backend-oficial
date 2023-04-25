package comigo.conta.backend.oficial.domain.pedido.repository;

import comigo.conta.backend.oficial.domain.pedido.Pedido;
import comigo.conta.backend.oficial.domain.shared.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, String> {
    List<Pedido> findAllByIdRestauranteAndStatus(String idRestaurante, Status status);

    List<Pedido> findAllByIdRestaurante(String idRestaurante);

    Long countByIdRestauranteAndStatus(String idRestaurante, Status status);

    Long countByIdRestaurante(String idRestaurante);
}
