package comigo.conta.backend.oficial.service.autenticacao;

import comigo.conta.backend.oficial.service.autenticacao.dto.RestauranteDetalhesDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import comigo.conta.backend.oficial.domain.restaurante.repository.RestauranteRepository;

@Service
public class AutenticacaoService implements UserDetailsService {

    private final RestauranteRepository restauranteRepository;

    public AutenticacaoService(RestauranteRepository restauranteRepository) {
        this.restauranteRepository = restauranteRepository;
    }

    // Método da interface implementada
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        final var restauranteOpt = restauranteRepository.findByEmail(username);

        if (restauranteOpt.isEmpty()) {
            throw new UsernameNotFoundException(String.format("usuario: %s nao encontrado", username));
        }

        return new RestauranteDetalhesDto(restauranteOpt.get());
    }
}
