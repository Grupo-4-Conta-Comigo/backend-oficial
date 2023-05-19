package comigo.conta.backend.oficial.domain.usuario.repository;

import comigo.conta.backend.oficial.domain.usuario.Usuario;
import comigo.conta.backend.oficial.service.usuario.dto.UsuarioNomeDocumentoDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    List<Usuario> findByRestauranteId(String restauranteId);
    Optional<Usuario> findByEmail(String email);

    @Query("SELECT new comigo.conta.backend.oficial.service.usuario.dto.UsuarioNomeDocumentoDto(u.nome, u.registro) FROM Usuario u WHERE u.id = :id")
    Optional<UsuarioNomeDocumentoDto> findNomeRegistroById(String id);
}
