package comigo.conta.backend.oficial.service.pedido;

import comigo.conta.backend.oficial.domain.pedido.Pedido;
import comigo.conta.backend.oficial.domain.pedido.repository.PedidoRepository;
import comigo.conta.backend.oficial.domain.shared.Status;
import comigo.conta.backend.oficial.domain.shared.usecases.GenerateRandomIdUsecase;
import comigo.conta.backend.oficial.service.pedido.dto.PedidoCriacaoDto;
import comigo.conta.backend.oficial.service.pedido.dto.PedidoUpdateDto;
import comigo.conta.backend.oficial.service.restaurante.RestauranteService;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {
    private final PedidoRepository repository;
    private final RestauranteService restauranteService;
    private final GenerateRandomIdUsecase generateRandomIdUsecase;

    public PedidoService(PedidoRepository repository, RestauranteService restauranteService, GenerateRandomIdUsecase generateRandomIdUsecase) {
        this.repository = repository;
        this.restauranteService = restauranteService;
        this.generateRandomIdUsecase = generateRandomIdUsecase;
    }

    public Pedido criar(PedidoCriacaoDto pedidoCriacaoDto, String idRestaurante) {
        final Pedido novoPedido = pedidoCriacaoDto.toEntity();

        final String id = generateRandomIdUsecase.execute();
        novoPedido.setId(id);
        novoPedido.setIdRestaurante(idRestaurante);

        return this.repository.save(novoPedido);
    }

    public List<Pedido> getAll(String idRestaurante, Optional<Boolean> active) {
        if (!restauranteService.existe(idRestaurante)) {
            throw new ResponseStatusException(404, "Restaurante não existe!", null);
        }
        if (active.isEmpty()) {
            return repository.findAllByIdRestaurante(idRestaurante);
        }

        return repository.findAllByIdRestauranteAndStatus(idRestaurante, active.get() ? Status.ativo : Status.finalizado);
    }

    public Pedido editar(String idPedido, PedidoUpdateDto pedidoUpdateDto) {
        final Pedido pedidoExistente = repository.findById(idPedido).orElseThrow(
                () -> new ResponseStatusException(404, "Pedido não encontrado...", null)
        );

        pedidoExistente.setMesa(pedidoUpdateDto.getMesa());
        pedidoExistente.setStatus(pedidoUpdateDto.getStatus());

        return repository.save(pedidoExistente);
    }

    public Pedido finalizar(String idPedido) {
        final Pedido pedidoExistente = repository.findById(idPedido).orElseThrow(
                () -> new ResponseStatusException(404, "Pedido não encontrado...", null)
        );

        pedidoExistente.setStatus(Status.finalizado);

        return repository.save(pedidoExistente);
    }

    public void deletar(String idPedido) {
        if (!repository.existsById(idPedido)) {
            throw new ResponseStatusException(404, "Pedido não encontrado...", null);
        }

        repository.deleteById(idPedido);
    }
}
