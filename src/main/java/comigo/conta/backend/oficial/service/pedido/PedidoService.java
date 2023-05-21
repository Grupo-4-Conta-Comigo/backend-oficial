package comigo.conta.backend.oficial.service.pedido;

import comigo.conta.backend.oficial.domain.pedido.Pedido;
import comigo.conta.backend.oficial.domain.pedido.repository.PedidoRepository;
import comigo.conta.backend.oficial.domain.shared.Status;
import comigo.conta.backend.oficial.domain.shared.object_utils.lista.ListaObj;
import comigo.conta.backend.oficial.domain.shared.object_utils.lista.list_sorting.SortPedidosOldestToNewests;
import comigo.conta.backend.oficial.domain.shared.usecases.GenerateRandomIdUsecase;
import comigo.conta.backend.oficial.domain.shared.usecases.GetPriceFromPedidoUsecase;
import comigo.conta.backend.oficial.service.pedido.dto.PedidoCriacaoDto;
import comigo.conta.backend.oficial.service.pedido.dto.PedidoUpdateDto;
import comigo.conta.backend.oficial.service.usuario.UsuarioService;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {
    private final PedidoRepository repository;
    private final UsuarioService usuarioService;
    private final GetPriceFromPedidoUsecase getPriceFromPedidoUsecase;
    private final SortPedidosOldestToNewests sortPedidosOldestToNewests;
    private final GenerateRandomIdUsecase generateRandomIdUsecase;

    public PedidoService(PedidoRepository repository, UsuarioService usuarioService, GetPriceFromPedidoUsecase getPriceFromPedidoUsecase, SortPedidosOldestToNewests sortPedidosOldestToNewests, GenerateRandomIdUsecase generateRandomIdUsecase) {
        this.repository = repository;
        this.usuarioService = usuarioService;
        this.getPriceFromPedidoUsecase = getPriceFromPedidoUsecase;
        this.sortPedidosOldestToNewests = sortPedidosOldestToNewests;
        this.generateRandomIdUsecase = generateRandomIdUsecase;
    }

    public Pedido criar(PedidoCriacaoDto pedidoCriacaoDto, String idRestaurante) {
        verificaRestauranteExiste(idRestaurante);
        final Pedido novoPedido = pedidoCriacaoDto.toEntity();

        final String id = generateRandomIdUsecase.execute();
        novoPedido.setId(id);
        novoPedido.setIdRestaurante(idRestaurante);

        return this.repository.save(novoPedido);
    }

    public List<Pedido> getAll(
            String idRestaurante,
            Optional<Boolean> active,
            Optional<Boolean> orderByOldest
    ) {
        verificaRestauranteExiste(idRestaurante);
        List<Pedido> pedidos;
        if (active.isEmpty()) {
            pedidos = repository.findAllByIdRestaurante(idRestaurante);
        } else {
            pedidos = repository.findAllByIdRestauranteAndStatus(idRestaurante, active.get() ? Status.ativo : Status.finalizado);
        }
        if (orderByOldest.orElse(false)) {
            pedidos = sortPedidosOldestToNewests.sort(new ListaObj<>(pedidos)).toList();
        }

        System.out.println(pedidos);
        return pedidos;
    }

    public Optional<Pedido> getById(String idPedido) {
        return repository.findById(idPedido);
    }

    public double getPreco(String idPedido) {
        return getPriceFromPedidoUsecase.execute(getPedidoOrElseThrow(idPedido));
    }

    public Long count(String idRestaurante, Optional<Boolean> ativos) {
        verificaRestauranteExiste(idRestaurante);
        if (ativos.isPresent()) {
            return repository.countByIdRestauranteAndStatus(idRestaurante, ativos.get() ? Status.ativo : Status.finalizado);
        }
        return repository.countByIdRestaurante(idRestaurante);
    }

    public Pedido editar(String idPedido, PedidoUpdateDto pedidoUpdateDto) {
        final Pedido pedidoExistente = getPedidoOrElseThrow(idPedido);

        pedidoExistente.setMesa(pedidoUpdateDto.getMesa());
        pedidoExistente.setStatus(pedidoUpdateDto.getStatus());

        return repository.save(pedidoExistente);
    }

    public Pedido finalizar(String idPedido) {
        final Pedido pedidoExistente = getPedidoOrElseThrow(idPedido);
        pedidoExistente.setStatus(Status.finalizado);
        return repository.save(pedidoExistente);
    }

    public void deletar(String idPedido) {
        verificaPedidoExiste(idPedido);
        repository.deleteById(idPedido);
    }

    public boolean naoExiste(String idPedido) {
        return !repository.existsById(idPedido);
    }

    private Pedido getPedidoOrElseThrow(String idPedido) {
        return repository.findById(idPedido).orElseThrow(() -> new ResponseStatusException(404, "Pedido não encontrado...", null));
    }

    private void verificaRestauranteExiste(final String idRestaurante) {
        if (usuarioService.naoExiste(idRestaurante)) {
            throw new ResponseStatusException(404, "Restaurante não encontrado!", null);
        }
    }

    private void verificaPedidoExiste(final String idPedido) {
        if (naoExiste(idPedido)) {
            throw new ResponseStatusException(404, "Pedido não encontrado...", null);
        }
    }
}