package comigo.conta.backend.oficial.api.controller.pedido;

import comigo.conta.backend.oficial.domain.pedido.submodules.item_comanda.ItemComanda;
import comigo.conta.backend.oficial.service.pedido.submodules.item_comanda.ItemComandaService;
import comigo.conta.backend.oficial.service.pedido.submodules.item_comanda.dto.ItemComandaCriacaoDto;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/itens-comanda")
@Tag(name = "Itens das Comandas", description = "Grupo de requisições dos Itens das Comandas")
public class ItemComandaController {
    private final ItemComandaService itemComandaService;

    public ItemComandaController(ItemComandaService itemComandaService) {
        this.itemComandaService = itemComandaService;
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
    @PostMapping("/criar/")
    public ResponseEntity<ItemComanda> postItemComanda(
            @RequestBody @Validated ItemComandaCriacaoDto itemComandaCriacaoDto
    ) {
        return ResponseEntity.status(201).body(itemComandaService.criar(itemComandaCriacaoDto));
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
    @GetMapping("/todos/{idComanda}")
    public ResponseEntity<List<ItemComanda>> getAll(@PathVariable String idComanda) {
        final List<ItemComanda> itens = itemComandaService.getAll(idComanda);
        return itens.isEmpty() ? ResponseEntity.status(204).build() : ResponseEntity.ok(itens);
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
    @GetMapping("/{idItemComanda}")
    public ResponseEntity<ItemComanda> getItemComandaById(@PathVariable String idItemComanda) {
        return ResponseEntity.of(itemComandaService.getById(idItemComanda));
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
    @PutMapping("/editar/{idItemComanda}")
    public ResponseEntity<ItemComanda> editarItemComanda(
            @PathVariable String idItemComanda,
            @RequestBody @Validated ItemComandaCriacaoDto itemComandaCriacaoDto
    ) {
        return ResponseEntity.ok(itemComandaService.editar(idItemComanda, itemComandaCriacaoDto));
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
    @DeleteMapping("/deletar/{idItemComanda}")
    public ResponseEntity<Void> deletarItemComanda(@PathVariable String idItemComanda) {
        itemComandaService.deletar(idItemComanda);
        return ResponseEntity.status(200).build();
    }


}
