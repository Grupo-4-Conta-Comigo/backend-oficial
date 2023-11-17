package comigo.conta.backend.oficial.api.controller.mobile;

import comigo.conta.backend.oficial.service.usuario.ClienteService;
import comigo.conta.backend.oficial.service.usuario.dto.ClienteCriacaoDto;
import comigo.conta.backend.oficial.service.usuario.dto.ClienteLoginDto;
import comigo.conta.backend.oficial.service.usuario.dto.ClienteTokenDto;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mobile/clientes")
@Tag(name = "Clientes")
@AllArgsConstructor
public class ClienteController {
    private final ClienteService clienteService;

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200"
            ),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(schema = @Schema(hidden = true))
            )
    })
    @CrossOrigin
    @PostMapping("/criar")
    public ResponseEntity<Void> criar(@RequestBody @Validated ClienteCriacaoDto clienteCriacaoDto) {
        clienteService.criarCliente(clienteCriacaoDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @CrossOrigin
    @PostMapping("/login")
    public ResponseEntity<ClienteTokenDto> login(@RequestBody @Validated ClienteLoginDto clienteLoginDto) {
        final var clienteAutenticado = clienteService.autenticar(clienteLoginDto);
        return ResponseEntity.ok(clienteAutenticado);
    }
}
