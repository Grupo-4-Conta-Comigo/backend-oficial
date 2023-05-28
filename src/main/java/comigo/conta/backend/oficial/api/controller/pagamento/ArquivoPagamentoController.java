package comigo.conta.backend.oficial.api.controller.pagamento;

import comigo.conta.backend.oficial.service.pagamento.ArquivoPagamentoService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@RestController
@RequestMapping("/arquivos-pagamento")
public class ArquivoPagamentoController {
    private final ArquivoPagamentoService arquivoPagamentoService;

    public ArquivoPagamentoController(ArquivoPagamentoService arquivoPagamentoService) {
        this.arquivoPagamentoService = arquivoPagamentoService;
    }

    @PostMapping("/ler-arquivo/{idRestaurante}")
    public ResponseEntity<Void> lerArquivoTxt(
            @RequestParam MultipartFile arquivoTxt,
            @PathVariable String idRestaurante
    ) {
        arquivoPagamentoService.lerArquivoTxt(arquivoTxt, idRestaurante);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/exportar-relatorio/{idRestaurante}")
    public ResponseEntity<Resource> receberArquivo(@PathVariable String idRestaurante) throws IOException {
        File arquivoTxt = arquivoPagamentoService.gravarArquivoTxt(idRestaurante);
        ContentDisposition contentDisposition = ContentDisposition.attachment().filename(arquivoTxt.getName()).build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(contentDisposition);
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(arquivoTxt.length())
                .contentType(MediaType.TEXT_PLAIN)
                .body(new ByteArrayResource(Files.readAllBytes(arquivoTxt.toPath())));
    }
}
