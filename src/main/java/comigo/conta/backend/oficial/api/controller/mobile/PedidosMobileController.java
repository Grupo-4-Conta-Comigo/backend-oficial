package comigo.conta.backend.oficial.api.controller.mobile;

import comigo.conta.backend.oficial.service.mobile.dtos.PedidoGenerico;
import comigo.conta.backend.oficial.service.usuario.IntegracaoMobileService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mobile/pedido")
@Tag(name = "Pedidos Externos")
@AllArgsConstructor
public class PedidosMobileController {
    private final IntegracaoMobileService integracaoMobileService;

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
    @GetMapping
    public ResponseEntity<PedidoGenerico> getPedidos(
            @RequestParam String restauranteId,
            @RequestParam int mesa
    ) {
        final var pedido = integracaoMobileService.getPedido(restauranteId, mesa);
        return ResponseEntity.ok(pedido);
    }
}
