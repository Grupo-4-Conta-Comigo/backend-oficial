package comigo.conta.backend.oficial.api.controller.pagamento;

import comigo.conta.backend.oficial.domain.pagamento.DetalhesPagamento;
import comigo.conta.backend.oficial.service.pagamento.CertificadoPagamentoService;
import comigo.conta.backend.oficial.service.pagamento.GerenciaDetalhesPagamentoService;
import comigo.conta.backend.oficial.service.pagamento.dto.DetalhesPagamentoCriacaoDto;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/detalhes-pagamento")
public class GerenciaDetalhesPagamentoController {
    private final GerenciaDetalhesPagamentoService gerenciaDetalhesPagamentoService;
    private final CertificadoPagamentoService certificadoPagamentoService;

    public GerenciaDetalhesPagamentoController(GerenciaDetalhesPagamentoService gerenciaDetalhesPagamentoService, CertificadoPagamentoService certificadoPagamentoService) {
        this.gerenciaDetalhesPagamentoService = gerenciaDetalhesPagamentoService;
        this.certificadoPagamentoService = certificadoPagamentoService;
    }

    @PutMapping("/criar/{idRestaurante}")
    public ResponseEntity<DetalhesPagamento> postDetalhesPagamento(
            @PathVariable String idRestaurante,
            @RequestBody @Validated DetalhesPagamentoCriacaoDto detalhesPagamentoCriacaoDto
    ) {
        return ResponseEntity.ok(gerenciaDetalhesPagamentoService.criarNovo(detalhesPagamentoCriacaoDto, idRestaurante));
    }

    @PutMapping("/certificados/criar/{idRestaurante}")
    public ResponseEntity<DetalhesPagamento> postCertificado(
            @PathVariable String idRestaurante,
            @RequestParam("certificado") MultipartFile certificado
    ) {
        return ResponseEntity.ok(certificadoPagamentoService.cadastrarCertificado(idRestaurante, certificado));
    }

    @GetMapping("/certificados/{idRestaurante}")
    public ResponseEntity<Boolean> certificadoExiste(@PathVariable String idRestaurante) {
        return ResponseEntity.ok(certificadoPagamentoService.certificadoExiste(idRestaurante));
    }

    @GetMapping("/{idRestaurante}")
    public ResponseEntity<DetalhesPagamento> getDetalhesPagamento(@PathVariable String idRestaurante) {
        return ResponseEntity.of(gerenciaDetalhesPagamentoService.getDetalhesPagamento(idRestaurante));
    }

    @DeleteMapping("/{idRestaurante}")
    public ResponseEntity<Void> deletarDetalhesPagamento(@PathVariable String idRestaurante) {
        gerenciaDetalhesPagamentoService.deletar(idRestaurante);
        return ResponseEntity.ok().build();
    }
}
