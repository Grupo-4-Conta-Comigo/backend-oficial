package comigo.conta.backend.oficial.service.autenticacao;

import comigo.conta.backend.oficial.domain.usuario.repository.ClienteRepository;
import comigo.conta.backend.oficial.domain.usuario.repository.UsuarioRepository;
import comigo.conta.backend.oficial.service.autenticacao.dto.ClienteDetalhesDto;
import comigo.conta.backend.oficial.service.autenticacao.dto.UsuarioDetalhesDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AutenticacaoService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;

    public AutenticacaoService(UsuarioRepository usuarioRepository, ClienteRepository clienteRepository) {
        this.usuarioRepository = usuarioRepository;
        this.clienteRepository = clienteRepository;
    }

    // Método da interface implementada
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return usuarioRepository
                .findByEmail(username)
                .map(UsuarioDetalhesDto::new)
                .map(usuarioDetalhesDto -> (UserDetails) usuarioDetalhesDto)
                .or(() -> clienteRepository.findByEmail(username).map(ClienteDetalhesDto::new))
                .orElseThrow(() -> new UsernameNotFoundException(String.format("usuario: %s nao encontrado", username)));
    }
}
