package comigo.conta.backend.oficial.domain.pagamento.repository;

import comigo.conta.backend.oficial.domain.pagamento.DetalhesPagamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetalhesPagamentoRepository extends JpaRepository<DetalhesPagamento, String> {
}
