package comigo.conta.backend.oficial.api.controller.pedido;

import comigo.conta.backend.oficial.domain.pedido.submodules.comanda.Comanda;
import comigo.conta.backend.oficial.service.pedido.PedidoService;
import comigo.conta.backend.oficial.service.pedido.submodules.comanda.ComandaService;
import comigo.conta.backend.oficial.service.pedido.submodules.comanda.dto.ComandaCriacaoDto;
import comigo.conta.backend.oficial.service.pedido.submodules.comanda.dto.ComandaUpdateDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/comandas")
@Tag(name = "Comandas", description = "Grupo de requisições de Comandas")
public class ComandaController {
    private final ComandaService comandaService;

    public ComandaController(ComandaService comandaService, PedidoService pedidoService) {
        this.comandaService = comandaService;
    }

    @PostMapping("/{idPedido}")
    public ResponseEntity<Comanda> postComanda(
            @RequestBody @Validated ComandaCriacaoDto comandaCriacaoDto,
            @PathVariable String idPedido
    ) {
        return ResponseEntity.status(201).body(this.comandaService.criar(comandaCriacaoDto, idPedido));
    }

    @GetMapping("/{idPedido}")
    public ResponseEntity<List<Comanda>> getAll(
            @PathVariable String idPedido,
            @RequestParam("ativos") Optional<Boolean> ativos
    ) {
        final List<Comanda> comandas = this.comandaService.getAll(idPedido, ativos);
        return comandas.isEmpty() ? ResponseEntity.status(204).build() : ResponseEntity.ok(comandas);
    }

    @GetMapping("/preco/{idComanda}")
    public ResponseEntity<Double> getPreco(@PathVariable String idComanda) {
        return ResponseEntity.ok(comandaService.getPreco(idComanda));
    }

    @PutMapping("/editar/{idComanda}")
    public ResponseEntity<Comanda> putComanda(
            @PathVariable String idComanda,
            @RequestBody ComandaUpdateDto comandaUpdateDto
            ) {
        return ResponseEntity.ok(this.comandaService.editar(idComanda, comandaUpdateDto));
    }

    @PatchMapping("/finalizar/{idComanda}")
    public ResponseEntity<Comanda> finalizarComanda(@PathVariable String idComanda) {
        return ResponseEntity.ok(this.comandaService.finalizar(idComanda));
    }

    @DeleteMapping("/{idComanda}")
    public ResponseEntity<Void> deletarComanda(@PathVariable String idComanda) {
        this.comandaService.deletar(idComanda);
        return ResponseEntity.status(200).build();
    }

}
