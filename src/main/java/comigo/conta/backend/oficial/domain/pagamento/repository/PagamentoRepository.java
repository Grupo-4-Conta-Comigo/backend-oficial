package comigo.conta.backend.oficial.domain.pagamento.repository;

import comigo.conta.backend.oficial.domain.pagamento.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagamentoRepository extends JpaRepository<Pagamento, String> {
}
