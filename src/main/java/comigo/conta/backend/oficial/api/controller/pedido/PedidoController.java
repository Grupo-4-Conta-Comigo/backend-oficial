package comigo.conta.backend.oficial.api.controller.pedido;

import comigo.conta.backend.oficial.domain.pedido.Pedido;
import comigo.conta.backend.oficial.service.pedido.PedidoService;
import comigo.conta.backend.oficial.service.pedido.dto.PedidoCriacaoDto;
import comigo.conta.backend.oficial.service.pedido.dto.PedidoUpdateDto;
import comigo.conta.backend.oficial.service.pedido.submodules.comanda.ComandaService;
import comigo.conta.backend.oficial.service.pedido.submodules.item_comanda.ItemComandaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pedidos")
@Tag(name = "Pedidos", description = "Grupo de requisições de Pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping("/criar/{idRestaurante}")
    public ResponseEntity<Pedido> criar(
            @RequestBody @Validated PedidoCriacaoDto pedidoCriacaoDto,
            @PathVariable String idRestaurante
    ) {
        return ResponseEntity.status(201).body(this.pedidoService.criar(pedidoCriacaoDto, idRestaurante));
    }

    @GetMapping("/{idRestaurante}")
    public ResponseEntity<List<Pedido>> getAll(
            @PathVariable String idRestaurante,
            @RequestParam("ativo") Optional<Boolean> active
    ) {
        final var pedidos = pedidoService.getAll(idRestaurante, active);
        return pedidos.isEmpty() ? ResponseEntity.status(204).build() : ResponseEntity.ok(pedidos);
    }

    @PutMapping("/editar/{idPedido}")
    public ResponseEntity<Pedido> editar(
            @PathVariable String idPedido,
            @RequestBody PedidoUpdateDto pedidoCriacaoDto
    ) {
        return ResponseEntity.ok(pedidoService.editar(idPedido, pedidoCriacaoDto));
    }

    @PutMapping("/finalizar/{idPedido}")
    public ResponseEntity<Pedido> finzalizar(@PathVariable String idPedido) {
        return ResponseEntity.ok(pedidoService.finalizar(idPedido));
    }

    @DeleteMapping("/{idPedido}")
    public ResponseEntity<Void> remover(
            @PathVariable String idPedido
    ) {
        pedidoService.deletar(idPedido);
        return ResponseEntity.status(200).build();
    }
}
