package comigo.conta.backend.oficial.api.controller.qr_code;

import comigo.conta.backend.oficial.service.qr_code.TableQRCodeService;
import comigo.conta.backend.oficial.service.qr_code.dto.TableQRCodeCreationDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.awt.image.BufferedImage;
import java.util.Optional;

@RestController
@RequestMapping("/restaurantes/qr-code")
@Tag(name = "QR Code", description = "Grupo de requisições de QR Codes")
public class TableQRCodeController {
    private final TableQRCodeService service;

    public TableQRCodeController(TableQRCodeService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<BufferedImage> createQRCode(
            @RequestBody @Validated
            TableQRCodeCreationDTO dto,
            @RequestParam
            Optional<Integer> width,
            @RequestParam
            Optional<Integer> height
    ) {
        return ResponseEntity.ok(
                service.bindQRCodeWithRestaurantAndTable(
                        dto.getRestauranteId(),
                        dto.getMesa(),
                        width,
                        height
                )
        );
    }
}
