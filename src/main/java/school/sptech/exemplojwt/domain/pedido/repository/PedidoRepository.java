package school.sptech.exemplojwt.domain.pedido.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import school.sptech.exemplojwt.domain.pedido.Pedido;
import school.sptech.exemplojwt.domain.shared.Status;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, String> {
    List<Pedido> findAllByIdRestauranteAndStatus(String idRestaurante, Status status);

    List<Pedido> findAllByIdRestaurante(String idRestaurante);
}
