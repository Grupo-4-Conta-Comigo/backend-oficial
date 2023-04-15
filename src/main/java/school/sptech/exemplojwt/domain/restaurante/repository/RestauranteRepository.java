package school.sptech.exemplojwt.domain.restaurante.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import school.sptech.exemplojwt.domain.restaurante.Restaurante;

import java.util.Optional;

public interface RestauranteRepository extends JpaRepository<Restaurante, String> {
    Optional<Restaurante> findByEmail(String email);
}
