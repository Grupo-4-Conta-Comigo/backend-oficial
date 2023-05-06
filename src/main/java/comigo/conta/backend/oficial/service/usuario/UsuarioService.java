package comigo.conta.backend.oficial.service.usuario;

import comigo.conta.backend.oficial.api.configuration.security.jwt.GerenciadorTokenJwt;
import comigo.conta.backend.oficial.domain.usuario.Usuario;
import comigo.conta.backend.oficial.domain.usuario.repository.UsuarioRepository;
import comigo.conta.backend.oficial.domain.shared.usecases.GenerateRandomIdUsecase;
import comigo.conta.backend.oficial.service.autenticacao.dto.UsuarioLoginDto;
import comigo.conta.backend.oficial.service.autenticacao.dto.RestauranteMudarSenhaDto;
import comigo.conta.backend.oficial.service.autenticacao.dto.UsuarioTokenDto;
import comigo.conta.backend.oficial.service.usuario.dto.GarcomCriacaoDto;
import comigo.conta.backend.oficial.service.usuario.dto.GarcomEdicaoDto;
import comigo.conta.backend.oficial.service.usuario.dto.RestauranteCriacaoDto;
import comigo.conta.backend.oficial.service.usuario.dto.UsuarioMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository repository;
    private final GerenciadorTokenJwt gerenciadorTokenJwt;
    private final AuthenticationManager authenticationManager;
    private final GenerateRandomIdUsecase generateRandomIdUsecase;

    public UsuarioService(PasswordEncoder passwordEncoder, UsuarioRepository repository, GerenciadorTokenJwt gerenciadorTokenJwt, AuthenticationManager authenticationManager, GenerateRandomIdUsecase generateRandomIdUsecase) {
        this.passwordEncoder = passwordEncoder;
        this.repository = repository;
        this.gerenciadorTokenJwt = gerenciadorTokenJwt;
        this.authenticationManager = authenticationManager;
        this.generateRandomIdUsecase = generateRandomIdUsecase;
    }

    public void criarRestaurante(RestauranteCriacaoDto restauranteCriacaoDto) {
        if (this.repository.findByEmail(restauranteCriacaoDto.getEmail()).isPresent()) {
            throw new ResponseStatusException(400, "Email já existe!", null);
        }
        final var novoRestaurante = UsuarioMapper.restauranteCriacaoDtoToUsuario(restauranteCriacaoDto);

        final String senhaCriptografada = passwordEncoder.encode(novoRestaurante.getSenha());
        novoRestaurante.setSenha(senhaCriptografada);

        final String id = generateRandomIdUsecase.execute();
        novoRestaurante.setId(id);
        novoRestaurante.setRestauranteId(id);

        this.repository.save(novoRestaurante);
    }

    public UsuarioTokenDto autenticar(UsuarioLoginDto usuarioLoginDto) {
        final var credentials = new UsernamePasswordAuthenticationToken(
                usuarioLoginDto.getEmail(),
                usuarioLoginDto.getSenha()
        );
        final var authentication = authenticationManager.authenticate(credentials);
        final var usuarioAutenticado = repository.findByEmail(
                usuarioLoginDto.getEmail()
        ).orElseThrow(
                () -> new ResponseStatusException(404, "Email do usuário não cadastrado", null)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final var token = gerenciadorTokenJwt.generateToken(authentication);
        return UsuarioMapper.UsuarioAutenticadoToUsuarioTokenDto(usuarioAutenticado, token);
    }

    public void criarGarcom(GarcomCriacaoDto garcomCriacaoDto) {
        if (this.repository.findByEmail(garcomCriacaoDto.getEmail()).isPresent()) {
            throw new ResponseStatusException(400, "Email já existe!", null);
        }
        if (naoExiste(garcomCriacaoDto.getRestauranteId())) {
            throw new ResponseStatusException(404, "Restaurante não encontrado...", null);
        }

        final var novoGarcom = UsuarioMapper.garcomCriacaoDtoToUsuario(garcomCriacaoDto);

        final String senhaCriptografada = passwordEncoder.encode(novoGarcom.getSenha());
        novoGarcom.setSenha(senhaCriptografada);

        final String id = generateRandomIdUsecase.execute();
        novoGarcom.setId(id);

        repository.save(novoGarcom);
    }

    public void mudarSenha(RestauranteMudarSenhaDto restauranteMudarSenhaDto) {
        final Usuario usuario = repository
                .findByEmail(restauranteMudarSenhaDto.getEmail())
                .orElseThrow(
                        () -> new ResponseStatusException(404, "Email não encontrado!", null)
                );

        final String senhaCriptografada = passwordEncoder.encode(restauranteMudarSenhaDto.getSenhaNova());
        usuario.setSenha(senhaCriptografada);

        repository.save(usuario);
    }

    public List<Usuario> findGarconsByRestauranteId(String idRestaurante) {
        if (naoExiste(idRestaurante)) {
            throw new ResponseStatusException(404, "Restaurante não encontrado!", null);
        }
        return repository.findByRestauranteId(idRestaurante);
    }

    public boolean naoExiste(String idRestaurante) {
        return !repository.existsById(idRestaurante);
    }

    public Optional<Usuario> findGarcomById(String idGarcom) {
        return repository.findById(idGarcom);
    }

    public Usuario editarGarcom(GarcomEdicaoDto garcomEdicaoDto, String idGarcom) {
        final Usuario usuarioAtual = getOrThrow404(idGarcom);

        usuarioAtual.setNome(garcomEdicaoDto.getNome());
        usuarioAtual.setRegistro(garcomEdicaoDto.getCpf());
        usuarioAtual.setCargo(garcomEdicaoDto.getCargo());
        usuarioAtual.setEmail(garcomEdicaoDto.getEmail());
        usuarioAtual.setSenha(garcomEdicaoDto.getSenha());

        return repository.save(usuarioAtual);
    }

    private Usuario getOrThrow404(String idUsuario) {
        return repository.findById(idUsuario)
                .orElseThrow(
                        () -> new ResponseStatusException(404, "Garçom não encontrado!", null)
                );
    }

    public void deletarGarcom(String idGarcom) {
        if (naoExiste(idGarcom)) {
            throw new ResponseStatusException(404, "Garçom não encontrado!", null);
        }
        repository.deleteById(idGarcom);
    }
}
