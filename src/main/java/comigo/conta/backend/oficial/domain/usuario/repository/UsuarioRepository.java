package comigo.conta.backend.oficial.domain.usuario.repository;

import comigo.conta.backend.oficial.domain.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    List<Usuario> findByRestauranteId(String restauranteId);
    Optional<Usuario> findByEmail(String email);
}
