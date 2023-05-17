package comigo.conta.backend.oficial.service.pagamento;

import comigo.conta.backend.oficial.domain.pagamento.DetalhesPagamento;
import comigo.conta.backend.oficial.domain.pagamento.repository.DetalhesPagamentoRepository;
import comigo.conta.backend.oficial.domain.shared.usecases.GenerateRandomIdUsecase;
import comigo.conta.backend.oficial.service.pagamento.dto.DetalhesPagamentoCriacaoDto;
import comigo.conta.backend.oficial.service.usuario.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class GerenciaDetalhesPagamentoService {
    final private UsuarioService usuarioService;
    final private DetalhesPagamentoRepository repository;
    final private GenerateRandomIdUsecase generateRandomIdUsecase;

    public GerenciaDetalhesPagamentoService(UsuarioService usuarioService, DetalhesPagamentoRepository repository, GenerateRandomIdUsecase generateRandomIdUsecase) {
        this.usuarioService = usuarioService;
        this.repository = repository;
        this.generateRandomIdUsecase = generateRandomIdUsecase;
    }

    public DetalhesPagamento criarNovo(DetalhesPagamentoCriacaoDto detalhesPagamentoCriacaoDto, String idRestaurante) {
        if (usuarioService.naoExiste(idRestaurante)) {
            throw new ResponseStatusException(404, "Usuário não existe...", null);
        }
        DetalhesPagamento detalhesPagamento = getDetalhesPagamento(idRestaurante).orElseGet(() -> criarDetalhesPagamentoDoZero(idRestaurante));
        detalhesPagamento.setChavePix(detalhesPagamentoCriacaoDto.getChavePix());
        detalhesPagamento.setClientId(detalhesPagamentoCriacaoDto.getClientId());
        detalhesPagamento.setClientSecret(detalhesPagamentoCriacaoDto.getClientSecret());
        System.out.println(detalhesPagamento.getId());

        return repository.save(detalhesPagamento);
    }

    public Optional<DetalhesPagamento> getDetalhesPagamento(String idRestaurante) {
        return repository.findByUsuarioId(idRestaurante);
    }

    private DetalhesPagamento criarDetalhesPagamentoDoZero(String idRestaurante) {
        DetalhesPagamento detalhesPagamento = new DetalhesPagamento();
        detalhesPagamento.setUsuarioId(idRestaurante);
        detalhesPagamento.setId(generateRandomIdUsecase.execute());
        return repository.save(detalhesPagamento);
    }

    public void deletar(String idRestaurante) {
        if (!repository.existsByUsuarioId(idRestaurante)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Detalhes de pagamento não encontrados pra esse usuário");
        }
        repository.deleteByUsuarioId(idRestaurante);
    }
}
