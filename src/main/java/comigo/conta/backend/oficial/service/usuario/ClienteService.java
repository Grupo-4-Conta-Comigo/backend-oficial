package comigo.conta.backend.oficial.service.usuario;

import comigo.conta.backend.oficial.api.configuration.security.jwt.GerenciadorTokenJwt;
import comigo.conta.backend.oficial.domain.usuario.repository.ClienteRepository;
import comigo.conta.backend.oficial.service.usuario.dto.ClienteCriacaoDto;
import comigo.conta.backend.oficial.service.usuario.dto.ClienteLoginDto;
import comigo.conta.backend.oficial.service.usuario.dto.ClienteTokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ClienteService {
    private final PasswordEncoder passwordEncoder;
    private final ClienteRepository clienteRepository;
    private final GerenciadorTokenJwt gerenciadorTokenJwt;
    private final AuthenticationManager authenticationManager;

    public void criarCliente(ClienteCriacaoDto clienteCriacaoDto) {
        if (this.clienteRepository.existsByEmail(clienteCriacaoDto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email existente");
        }
        final var novoCliente = clienteCriacaoDto.toCliente();
        final var senhaCriptografada = passwordEncoder.encode(novoCliente.getSenha());

        novoCliente.setSenha(senhaCriptografada);

        clienteRepository.save(novoCliente);
    }

    public ClienteTokenDto autenticar(ClienteLoginDto clienteLoginDto) {
        final var credentials = new UsernamePasswordAuthenticationToken(
                clienteLoginDto.getEmail(),
                clienteLoginDto.getSenha()
        );
        final var authentication = authenticationManager.authenticate(credentials);
        final var clienteAutenticado = clienteRepository
                .findByEmail(clienteLoginDto.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email do usuário não cadastrado"));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final var token = gerenciadorTokenJwt.generateToken(authentication);
        return new ClienteTokenDto(token, clienteAutenticado);
    }
}
