package comigo.conta.backend.oficial.domain.pagamento.repository;

import comigo.conta.backend.oficial.domain.pagamento.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PagamentoRepository extends JpaRepository<Pagamento, String> {
    List<Pagamento> findByIdRestauranteAndDataHoraPagamentoBetween(
            String idRestaurante,
            LocalDateTime dataHoraPagamentoBefore,
            LocalDateTime dataHoraPagamentoAfter
    );
}
