package comigo.conta.backend.oficial.domain.usuario.repository;

import comigo.conta.backend.oficial.domain.usuario.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByEmail(String email);
    boolean existsByEmail(String email);
}
