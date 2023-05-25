package comigo.conta.backend.oficial.api.controller.pagamento;

import comigo.conta.backend.oficial.domain.pedido.submodules.comanda.Comanda;
import comigo.conta.backend.oficial.service.pagamento.RealizarPagamentosService;
import comigo.conta.backend.oficial.service.pagamento.dto.CriarPagamentoDto;
import comigo.conta.backend.oficial.service.pagamento.dto.GetQRCodeDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/pagamentos")
public class RealizarPagamentoController {
    private final RealizarPagamentosService realizarPagamentosService;

    public RealizarPagamentoController(RealizarPagamentosService realizarPagamentosService) {
        this.realizarPagamentosService = realizarPagamentosService;
    }

    @PutMapping("/criar-cobranca")
    public ResponseEntity<Comanda> criarCobranca(@RequestBody @Validated CriarPagamentoDto criarPagamentoDto) {
        return ok(
                realizarPagamentosService.criarPagamento(
                        criarPagamentoDto.getIdRestaurante(),
                        criarPagamentoDto.getIdComanda(),
                        criarPagamentoDto.getValor()
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
}
