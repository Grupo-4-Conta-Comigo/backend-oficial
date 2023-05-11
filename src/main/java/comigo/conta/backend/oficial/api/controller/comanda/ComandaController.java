package comigo.conta.backend.oficial.api.controller.comanda;

import comigo.conta.backend.oficial.domain.pedido.submodules.comanda.Comanda;
import comigo.conta.backend.oficial.service.pedido.submodules.comanda.ComandaService;
import comigo.conta.backend.oficial.service.pedido.submodules.comanda.dto.ComandaCriacaoDto;
import comigo.conta.backend.oficial.service.pedido.submodules.comanda.dto.ComandaResponseDto;
import comigo.conta.backend.oficial.service.pedido.submodules.comanda.dto.ComandaUpdateDto;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/comandas")
@Tag(name = "Comandas", description = "Grupo de requisições de Comandas")
public class ComandaController {
    private final ComandaService comandaService;

    public ComandaController(ComandaService comandaService) {
        this.comandaService = comandaService;
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
    @PostMapping("/criar/{idPedido}")
    public ResponseEntity<ComandaResponseDto> postComanda(
            @PathVariable String idPedido,
            @RequestBody @Validated ComandaCriacaoDto comandaCriacaoDto
    ) {
        return ResponseEntity.status(201).body(
                new ComandaResponseDto(
                        comandaService.criar(comandaCriacaoDto, idPedido)
                )
        );
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
    @GetMapping("/{idPedido}")
    public ResponseEntity<ComandaResponseDto> getComandaById(@PathVariable String idPedido) {
        return ResponseEntity.of(
                comandaService.getById(idPedido)
                .map(ComandaResponseDto::new)
        );
    }

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200"
            ),
            @ApiResponse(
                    responseCode = "204",
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
    @GetMapping("/todas/{idPedido}")
    public ResponseEntity<List<ComandaResponseDto>> getAll(
            @PathVariable String idPedido,
            @RequestParam("ativos") Optional<Boolean> ativos
    ) {
        final List<Comanda> comandas = this.comandaService.getAll(idPedido, ativos);
        return comandas.isEmpty() ?
                ResponseEntity.status(204).build() :
                ResponseEntity.ok(comandaToComandaDto(comandas));
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
    @GetMapping("/preco/{idComanda}")
    public ResponseEntity<Double> getPreco(@PathVariable String idComanda) {
        return ResponseEntity.ok(comandaService.getPreco(idComanda));
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
    @PutMapping("/editar/{idComanda}")
    public ResponseEntity<ComandaResponseDto> putComanda(
            @PathVariable String idComanda,
            @RequestBody @Validated ComandaUpdateDto comandaUpdateDto
    ) {
        return ResponseEntity.ok(
                new ComandaResponseDto(
                        comandaService.editar(idComanda, comandaUpdateDto)
                )
        );
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
    @PatchMapping("/finalizar/{idComanda}")
    public ResponseEntity<ComandaResponseDto> finalizarComanda(@PathVariable String idComanda) {
        return ResponseEntity.ok(
                new ComandaResponseDto(
                        comandaService.finalizar(idComanda)
                )
        );
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
    @DeleteMapping("/deletar/{idComanda}")
    public ResponseEntity<Void> deletarComanda(@PathVariable String idComanda) {
        this.comandaService.deletar(idComanda);
        return ResponseEntity.status(200).build();
    }

    List<ComandaResponseDto> comandaToComandaDto(List<Comanda> comandas) {
        return comandas.stream().map(ComandaResponseDto::new).toList();
    }

}
