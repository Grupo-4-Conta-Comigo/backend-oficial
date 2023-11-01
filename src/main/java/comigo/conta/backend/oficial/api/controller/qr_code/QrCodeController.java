package comigo.conta.backend.oficial.api.controller.qr_code;

import comigo.conta.backend.oficial.service.usuario.IntegracaoMobileService;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.*;

import java.awt.image.BufferedImage;
import java.util.Optional;

@RestController
@RequestMapping("/qr-code/restaurante/{restauranteId}")
public class QrCodeController {
    private final IntegracaoMobileService integracaoMobileService;

    public QrCodeController(IntegracaoMobileService integracaoMobileService) {
        this.integracaoMobileService = integracaoMobileService;
    }

    @PutMapping(value = "/mesa/{mesa}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<BufferedImage> generateQRCodeForTable(
            @PathVariable
            String restauranteId,
            @PathVariable
            int mesa,
            @RequestParam Optional<Integer> largura,
            @RequestParam Optional<Integer> altura
    ) {
        return ResponseEntity.ok(
                integracaoMobileService.generateQRCodeToTable(
                        restauranteId,
                        mesa,
                        largura,
                        altura
                )
        );
    }

    @Bean
    public HttpMessageConverter<BufferedImage> createImageHttpMessageConverter() {
        return new BufferedImageHttpMessageConverter();
    }
}
