package comigo.conta.backend.oficial.api.controller.pagamento;

import comigo.conta.backend.oficial.domain.pagamento.Pagamento;
import comigo.conta.backend.oficial.domain.pedido.submodules.comanda.Comanda;
import comigo.conta.backend.oficial.service.pagamento.RealizarPagamentosService;
import comigo.conta.backend.oficial.service.pagamento.dto.CobrancaDetailsDto;
import comigo.conta.backend.oficial.service.pagamento.dto.CriarCobrancaDto;
import comigo.conta.backend.oficial.service.pagamento.dto.CriarPagamentoDto;
import comigo.conta.backend.oficial.service.pagamento.dto.GetQRCodeDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/pagamentos")
public class RealizarPagamentoController {
    private final RealizarPagamentosService realizarPagamentosService;

    public RealizarPagamentoController(RealizarPagamentosService realizarPagamentosService) {
        this.realizarPagamentosService = realizarPagamentosService;
    }

    @PutMapping("/criar-cobranca")
    public ResponseEntity<Comanda> criarCobranca(@RequestBody @Validated CriarCobrancaDto criarCobrancaDto) {
        return ok(
                realizarPagamentosService.criarCobranca(
                        criarCobrancaDto.getIdRestaurante(),
                        criarCobrancaDto.getIdComanda(),
                        criarCobrancaDto.getValor()
                )
        );
    }

    @PutMapping(value = "/qr-code", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getQRCode(
            @RequestBody @Validated GetQRCodeDto getQRCodeDto
    ) {
        return ok(
                realizarPagamentosService.getQRCode(
                        getQRCodeDto.getIdComanda(),
                        getQRCodeDto.getIdRestaurante()
                )
        );
    }

    @GetMapping("/restaurante/{idRestaurante}/comanda/{idComanda}/consultar-cobranca")
    public ResponseEntity<List<CobrancaDetailsDto>> getCobrancaDetails(
            @PathVariable String idComanda,
            @PathVariable String idRestaurante
    ) {
        return listToResponseEntity(
                realizarPagamentosService.getCobrancaDetails(
                        idRestaurante,
                        idComanda
                )
        );
    }

    @PostMapping("/criar")
    public ResponseEntity<Pagamento> confirmarpagamento(
            @RequestBody @Validated CriarPagamentoDto criarPagamentoDto
    ) {
        return ok(
                realizarPagamentosService.cadastrarPagamento(criarPagamentoDto)
        );
    }

    @GetMapping("/todos/{idRestaurante}")
    public ResponseEntity<List<Pagamento>> getTodosPagamentos(
            @PathVariable String idRestaurante,
            @RequestParam Optional<Integer> quantidade
    ) {
        return listToResponseEntity(
                realizarPagamentosService.getTodosPagamentos(idRestaurante, quantidade)
        );
    }

    private <T> ResponseEntity<List<T>> listToResponseEntity(List<T> response) {
        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }
}
