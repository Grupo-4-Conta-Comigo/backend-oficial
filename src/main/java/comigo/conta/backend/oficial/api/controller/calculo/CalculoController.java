package comigo.conta.backend.oficial.api.controller.calculo;

import comigo.conta.backend.oficial.service.calculo.CalculoService;
import comigo.conta.backend.oficial.service.calculo.dto.CalculoPersonalizadoRequestDto;
import comigo.conta.backend.oficial.service.calculo.dto.CalculoPersonalizadoResponseDto;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/calculos")
@Tag(name = "Cálculos", description = "Grupo de requisições de Cálculos"    )
public class CalculoController {
    private final CalculoService calculoService;

    public CalculoController(CalculoService calculoService) {
        this.calculoService = calculoService;
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
    })
    @PostMapping("/calculo-personalizado")
    public ResponseEntity<List<CalculoPersonalizadoResponseDto>> calculoPersonalizado(
            @RequestBody @Validated
            CalculoPersonalizadoRequestDto calculoPersonalizadoRequestDto
    ) {
        return ResponseEntity.ok(calculoService.calcularPersonalizado(calculoPersonalizadoRequestDto));
    }
}
