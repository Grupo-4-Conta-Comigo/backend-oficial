package comigo.conta.backend.oficial.service.autenticacao;

import comigo.conta.backend.oficial.service.autenticacao.dto.UsuarioDetalhesDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import comigo.conta.backend.oficial.domain.usuario.repository.UsuarioRepository;

@Service
public class AutenticacaoService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public AutenticacaoService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // Método da interface implementada
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        final var restauranteOpt = usuarioRepository.findByEmail(username);

        if (restauranteOpt.isEmpty()) {
            throw new UsernameNotFoundException(String.format("usuario: %s nao encontrado", username));
        }

        return new UsuarioDetalhesDto(restauranteOpt.get());
    }
}
