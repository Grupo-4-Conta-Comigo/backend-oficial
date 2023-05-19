package comigo.conta.backend.oficial.domain.pagamento.repository;

import comigo.conta.backend.oficial.domain.pagamento.DetalhesPagamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DetalhesPagamentoRepository extends JpaRepository<DetalhesPagamento, String> {
    void deleteByUsuarioId(String usuarioId);
    boolean existsByUsuarioId(String usuarioId);
    Optional<DetalhesPagamento> findByUsuarioId(String id);

}
