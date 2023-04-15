package school.sptech.exemplojwt.service.restaurante;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import school.sptech.exemplojwt.api.configuration.security.jwt.GerenciadorTokenJwt;
import school.sptech.exemplojwt.domain.restaurante.repository.RestauranteRepository;
import school.sptech.exemplojwt.domain.shared.usecases.GenerateRandomIdUsecase;
import school.sptech.exemplojwt.service.autenticacao.dto.RestauranteLoginDto;
import school.sptech.exemplojwt.service.autenticacao.dto.RestauranteTokenDto;
import school.sptech.exemplojwt.service.restaurante.dto.RestauranteCriacaoDto;
import school.sptech.exemplojwt.service.restaurante.dto.RestauranteMapper;

import java.util.UUID;

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

    public boolean existe(String idRestaurante) {
        return repository.existsById(idRestaurante);
    }

}
