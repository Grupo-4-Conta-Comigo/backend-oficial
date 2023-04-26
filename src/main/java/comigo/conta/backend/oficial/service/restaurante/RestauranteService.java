package comigo.conta.backend.oficial.service.restaurante;

import comigo.conta.backend.oficial.api.configuration.security.jwt.GerenciadorTokenJwt;
import comigo.conta.backend.oficial.domain.restaurante.Restaurante;
import comigo.conta.backend.oficial.domain.restaurante.repository.RestauranteRepository;
import comigo.conta.backend.oficial.domain.shared.usecases.GenerateRandomIdUsecase;
import comigo.conta.backend.oficial.service.autenticacao.dto.RestauranteLoginDto;
import comigo.conta.backend.oficial.service.autenticacao.dto.RestauranteMudarSenhaDto;
import comigo.conta.backend.oficial.service.autenticacao.dto.RestauranteTokenDto;
import comigo.conta.backend.oficial.service.restaurante.dto.RestauranteCriacaoDto;
import comigo.conta.backend.oficial.service.restaurante.dto.RestauranteMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RestauranteService {
    private final PasswordEncoder passwordEncoder;
    private final RestauranteRepository repository;
    private final GerenciadorTokenJwt gerenciadorTokenJwt;
    private final AuthenticationManager authenticationManager;
    private final GenerateRandomIdUsecase generateRandomIdUsecase;

    public RestauranteService(PasswordEncoder passwordEncoder, RestauranteRepository repository, GerenciadorTokenJwt gerenciadorTokenJwt, AuthenticationManager authenticationManager, GenerateRandomIdUsecase generateRandomIdUsecase) {
        this.passwordEncoder = passwordEncoder;
        this.repository = repository;
        this.gerenciadorTokenJwt = gerenciadorTokenJwt;
        this.authenticationManager = authenticationManager;
        this.generateRandomIdUsecase = generateRandomIdUsecase;
    }

    public void criar(RestauranteCriacaoDto restauranteCriacaoDto) {
        if (this.repository.findByEmail(restauranteCriacaoDto.getEmail()).isPresent()) {
            throw new ResponseStatusException(400, "Email já existe!", null);
        }
        final var novoRestaurante = RestauranteMapper.of(restauranteCriacaoDto);

        final String senhaCriptografada = passwordEncoder.encode(novoRestaurante.getSenha());
        novoRestaurante.setSenha(senhaCriptografada);

        final String id = generateRandomIdUsecase.execute();
        novoRestaurante.setId(id);

        this.repository.save(novoRestaurante);
    }

    public RestauranteTokenDto autenticar(RestauranteLoginDto restauranteLoginDto) {
        final var credentials = new UsernamePasswordAuthenticationToken(
                restauranteLoginDto.getEmail(),
                restauranteLoginDto.getSenha()
        );
        final var authentication = authenticationManager.authenticate(credentials);
        final var restauranteAutenticado = repository.findByEmail(
                restauranteLoginDto.getEmail()
        ).orElseThrow(
                () -> new ResponseStatusException(404, "Email do usuário não cadastrado", null)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final var token = gerenciadorTokenJwt.generateToken(authentication);
        return RestauranteMapper.of(restauranteAutenticado, token);
    }

    public void mudarSenha(RestauranteMudarSenhaDto restauranteMudarSenhaDto) {
        final Restaurante restaurante = repository
                .findByEmail(restauranteMudarSenhaDto.getEmail())
                .orElseThrow(
                        () -> new ResponseStatusException(404, "Email não encontrado!", null)
                );

        final String senhaCriptografada = passwordEncoder.encode(restauranteMudarSenhaDto.getSenhaNova());
        restaurante.setSenha(senhaCriptografada);

        repository.save(restaurante);
    }

    public boolean existe(String idRestaurante) {
        return repository.existsById(idRestaurante);
    }

}
