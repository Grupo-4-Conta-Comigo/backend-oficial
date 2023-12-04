package comigo.conta.backend.oficial.api.controller.mobile;

import comigo.conta.backend.oficial.service.pagamento.RealizarPagamentosService;
import comigo.conta.backend.oficial.service.pagamento.dto.CopiaColaDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/mobile/pagamentos")
@Tag(name = "Pagamentos Mobile")
@AllArgsConstructor
public class PagamentoMobileContoller {
    private final RealizarPagamentosService realizarPagamentosService;

    @GetMapping
    public ResponseEntity<CopiaColaDto> get(
            @RequestParam String restauranteId,
            @RequestParam Double valor
    ) {
        return ok(realizarPagamentosService.getCopiaCola(restauranteId, valor));
    }

}
