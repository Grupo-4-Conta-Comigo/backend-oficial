package comigo.conta.backend.oficial.api.controller.mobile;

import comigo.conta.backend.oficial.service.mobile.dtos.PedidoGenerico;
import comigo.conta.backend.oficial.service.usuario.IntegracaoMobileService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mobile/pedido")
@AllArgsConstructor
public class PedidosMobileController {
    private final IntegracaoMobileService integracaoMobileService;

    @GetMapping
    public ResponseEntity<PedidoGenerico> getPedidos(
            @RequestParam String restauranteId,
            @RequestParam int mesa
    ) {
        final var pedido = integracaoMobileService.getPedido(restauranteId, mesa);
        return ResponseEntity.ok(pedido);
    }
}
