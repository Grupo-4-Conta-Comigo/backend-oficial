package comigo.conta.backend.oficial.domain.restaurante.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import comigo.conta.backend.oficial.domain.restaurante.Restaurante;

import java.util.Optional;

public interface RestauranteRepository extends JpaRepository<Restaurante, String> {
    Optional<Restaurante> findByEmail(String email);
}
