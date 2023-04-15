package school.sptech.exemplojwt.api.controller.pedido;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import school.sptech.exemplojwt.domain.pedido.Pedido;
import school.sptech.exemplojwt.service.pedido.PedidoService;
import school.sptech.exemplojwt.service.pedido.dto.PedidoCriacaoDto;
import school.sptech.exemplojwt.service.pedido.dto.PedidoUpdateDto;
import school.sptech.exemplojwt.service.pedido.submodules.comanda.ComandaService;
import school.sptech.exemplojwt.service.pedido.submodules.item_comanda.ItemComandaService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;
    private final ComandaService comandaService;
    private final ItemComandaService itemComandaService;

    public PedidoController(PedidoService pedidoService, ComandaService comandaService, ItemComandaService itemComandaService) {
        this.pedidoService = pedidoService;
        this.comandaService = comandaService;
        this.itemComandaService = itemComandaService;
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
