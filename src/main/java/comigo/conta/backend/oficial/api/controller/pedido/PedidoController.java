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
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping("/pedidos")
@Tag(name = "Pedidos", description = "Grupo de requisições de Pedidos")
@SecurityRequirement(name = "Bearer")
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

    @CrossOrigin
    @PostMapping("/criar/{idRestaurante}")
    public ResponseEntity<PedidoResponseDto> criar(
            @RequestBody @Validated PedidoCriacaoDto pedidoCriacaoDto,
            @PathVariable String idRestaurante
    ) {
        return status(201).body(
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

    @CrossOrigin
    @GetMapping("/todos/{idRestaurante}")
//    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<PedidoResponseDto>> getAll(
            @PathVariable String idRestaurante,
            @RequestParam("ativos") Optional<Boolean> ativos,
            @RequestParam("orderByOldest") Optional<Boolean> orderByOldest
    ) {
        final var pedidos = pedidoService.getAll(idRestaurante, ativos, orderByOldest);
        return pedidos.isEmpty() ?
                status(204).build() :
                ok(pedidosToPedidosResponse(pedidos));
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
        return of(
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
        return ok(pedidoService.count(idRestaurante, ativos));
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
        return ok(pedidoService.getPreco(idPedido));
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
        return ok(
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
    @PatchMapping("/fechar/{idPedido}")
    public ResponseEntity<PedidoResponseDto> finalizar(@PathVariable String idPedido) {
        return ok(
                new PedidoResponseDto(
                        pedidoService.fechar(idPedido)
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
        return ok(
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
        return status(200).build();
    }

    private List<PedidoResponseDto> pedidosToPedidosResponse(List<Pedido> pedidos) {
        return pedidos.stream().map(PedidoResponseDto::new).toList();
    }
}
