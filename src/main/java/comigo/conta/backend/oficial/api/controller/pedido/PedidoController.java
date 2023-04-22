package comigo.conta.backend.oficial.api.controller.pedido;

import comigo.conta.backend.oficial.domain.pedido.Pedido;
import comigo.conta.backend.oficial.service.pedido.PedidoService;
import comigo.conta.backend.oficial.service.pedido.dto.PedidoCriacaoDto;
import comigo.conta.backend.oficial.service.pedido.dto.PedidoResponseDto;
import comigo.conta.backend.oficial.service.pedido.dto.PedidoUpdateDto;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200"
            ),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "401",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "403",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = @Content(schema = @Schema(hidden = true))
            ),
    })
    @PostMapping("/criar/{idRestaurante}")
    public ResponseEntity<PedidoResponseDto> criar(
            @RequestBody @Validated PedidoCriacaoDto pedidoCriacaoDto,
            @PathVariable String idRestaurante
    ) {
        return ResponseEntity.status(201).body(
                new PedidoResponseDto(
                        this.pedidoService.criar(pedidoCriacaoDto, idRestaurante)
                )
        );
    }

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200"
            ),
            @ApiResponse(
                    responseCode = "204",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "401",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "403",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = @Content(schema = @Schema(hidden = true))
            ),
    })
    @GetMapping("/todos/{idRestaurante}")
    public ResponseEntity<List<PedidoResponseDto>> getAll(
            @PathVariable String idRestaurante,
            @RequestParam("ativos") Optional<Boolean> ativos
    ) {
        final var pedidos = pedidoService.getAll(idRestaurante, ativos);
        return pedidos.isEmpty() ?
                ResponseEntity.status(204).build() :
                ResponseEntity.ok(pedidosToPedidosResponse(pedidos));
    }


    @ApiResponses({
            @ApiResponse(
                    responseCode = "200"
            ),
            @ApiResponse(
                    responseCode = "401",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "403",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = @Content(schema = @Schema(hidden = true))
            ),
    })
    @GetMapping("/{idPedido}")
    public ResponseEntity<PedidoResponseDto> getPedidoById(@PathVariable String idPedido) {
        return ResponseEntity.of(
                pedidoService.getById(idPedido)
                .map(PedidoResponseDto::new)
        );
    }

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200"
            ),
            @ApiResponse(
                    responseCode = "401",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "403",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = @Content(schema = @Schema(hidden = true))
            ),
    })
    @GetMapping("/count/{idRestaurante}")
    public ResponseEntity<Long> count(
            @PathVariable String idRestaurante,
            @RequestParam("ativos") Optional<Boolean> ativos
    ) {
        return ResponseEntity.ok(pedidoService.count(idRestaurante, ativos));
    }

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200"
            ),
            @ApiResponse(
                    responseCode = "401",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "403",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = @Content(schema = @Schema(hidden = true))
            ),
    })
    @GetMapping("/preco/{idPedido}")
    public ResponseEntity<Double> getPreco(@PathVariable String idPedido) {
        return ResponseEntity.ok(pedidoService.getPreco(idPedido));
    }

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200"
            ),
            @ApiResponse(
                    responseCode = "401",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "403",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = @Content(schema = @Schema(hidden = true))
            ),
    })
    @PutMapping("/editar/{idPedido}")
    public ResponseEntity<PedidoResponseDto> editar(
            @PathVariable String idPedido,
            @RequestBody @Validated PedidoUpdateDto pedidoCriacaoDto
    ) {
        return ResponseEntity.ok(
                new PedidoResponseDto(
                        pedidoService.editar(idPedido, pedidoCriacaoDto)
                )
        );
    }

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200"
            ),
            @ApiResponse(
                    responseCode = "401",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "403",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = @Content(schema = @Schema(hidden = true))
            ),
    })
    @PatchMapping("/finalizar/{idPedido}")
    public ResponseEntity<PedidoResponseDto> finzalizar(@PathVariable String idPedido) {
        return ResponseEntity.ok(
                new PedidoResponseDto(
                        pedidoService.finalizar(idPedido)
                )
        );
    }

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200"
            ),
            @ApiResponse(
                    responseCode = "401",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "403",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = @Content(schema = @Schema(hidden = true))
            ),
    })
    @DeleteMapping("/deletar/{idPedido}")
    public ResponseEntity<Void> remover(@PathVariable String idPedido) {
        pedidoService.deletar(idPedido);
        return ResponseEntity.status(200).build();
    }

    private List<PedidoResponseDto> pedidosToPedidosResponse(List<Pedido> pedidos) {
        return pedidos.stream().map(PedidoResponseDto::new).toList();
    }
}
