package comigo.conta.backend.oficial.domain.pagamento.repository;

import comigo.conta.backend.oficial.domain.pagamento.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface PagamentoRepository extends JpaRepository<Pagamento, String> {
    List<Pagamento> findByIdRestaurante(String idRestaurante);
    List<Pagamento> findByIdRestauranteAndDataHoraPagamentoBetween(
            String idRestaurante,
            LocalDateTime dataHoraPagamentoBefore,
            LocalDateTime dataHoraPagamentoAfter
    );

    @Query("SELECT p FROM Pagamento p ORDER BY p.dataHoraPagamento DESC")
    List<Pagamento> findByIdRestauranteOrderByDataHoraPagamentoDesc(String idRestaurante);
}
