package comigo.conta.backend.oficial.api.controller.pagamento;

import comigo.conta.backend.oficial.domain.pagamento.DetalhesPagamento;
import comigo.conta.backend.oficial.service.pagamento.CertificadoPagamentoService;
import comigo.conta.backend.oficial.service.pagamento.GerenciaDetalhesPagamentoService;
import comigo.conta.backend.oficial.service.pagamento.RealizarPagamentosService;
import comigo.conta.backend.oficial.service.pagamento.dto.DetalhesPagamentoCriacaoDto;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/detalhes-pagamento")
public class GerenciaDetalhesPagamentoController {
    private final GerenciaDetalhesPagamentoService gerenciaDetalhesPagamentoService;
    private final CertificadoPagamentoService certificadoPagamentoService;
    private final RealizarPagamentosService realizarPagamentosService;

    public GerenciaDetalhesPagamentoController(GerenciaDetalhesPagamentoService gerenciaDetalhesPagamentoService, CertificadoPagamentoService certificadoPagamentoService, RealizarPagamentosService realizarPagamentosService) {
        this.gerenciaDetalhesPagamentoService = gerenciaDetalhesPagamentoService;
        this.certificadoPagamentoService = certificadoPagamentoService;
        this.realizarPagamentosService = realizarPagamentosService;
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
    @PutMapping("/criar/{idRestaurante}")
    public ResponseEntity<DetalhesPagamento> postDetalhesPagamento(
            @PathVariable String idRestaurante,
            @RequestBody @Validated DetalhesPagamentoCriacaoDto detalhesPagamentoCriacaoDto
    ) {
        return ResponseEntity.ok(
                gerenciaDetalhesPagamentoService.criarNovo(
                        detalhesPagamentoCriacaoDto, idRestaurante
                )
        );
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
    @PutMapping("/certificados/criar/{idRestaurante}")
    public ResponseEntity<DetalhesPagamento> postCertificado(
            @PathVariable String idRestaurante,
            @RequestParam("certificado") MultipartFile certificado
    ) {
        return ResponseEntity.ok(certificadoPagamentoService.cadastrarCertificado(idRestaurante, certificado));
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
    @GetMapping("/certificados/{idRestaurante}")
    public ResponseEntity<Boolean> certificadoExiste(@PathVariable String idRestaurante) {
        return ResponseEntity.ok(certificadoPagamentoService.certificadoExisteNoBanco(idRestaurante));
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
    @GetMapping("/{idRestaurante}")
    public ResponseEntity<DetalhesPagamento> getDetalhesPagamento(@PathVariable String idRestaurante) {
        return ResponseEntity.of(gerenciaDetalhesPagamentoService.getDetalhesPagamento(idRestaurante));
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
    @PutMapping("/testar-pagamentos/{idRestaurante}")
    public ResponseEntity<Boolean> testarPagamento(@PathVariable String idRestaurante) {
        return ResponseEntity.ok(realizarPagamentosService.realizarPagamentoParaTeste(idRestaurante));
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
    @DeleteMapping("/{idRestaurante}")
    public ResponseEntity<Void> deletarDetalhesPagamento(@PathVariable String idRestaurante) {
        gerenciaDetalhesPagamentoService.deletar(idRestaurante);
        return ResponseEntity.ok().build();
    }
}
