package school.sptech.exemplojwt.service.autenticacao;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import school.sptech.exemplojwt.domain.restaurante.repository.RestauranteRepository;
import school.sptech.exemplojwt.service.autenticacao.dto.RestauranteDetalhesDto;

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
