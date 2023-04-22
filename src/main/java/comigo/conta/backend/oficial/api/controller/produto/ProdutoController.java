package comigo.conta.backend.oficial.api.controller.produto;

import comigo.conta.backend.oficial.domain.produto.Produto;
import comigo.conta.backend.oficial.service.produto.ProdutoService;
import comigo.conta.backend.oficial.service.produto.dto.ProdutoCriacaoDto;
import comigo.conta.backend.oficial.service.produto.dto.ProdutoGetDto;
import comigo.conta.backend.oficial.service.produto.dto.ProdutoUpdateDto;
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
@RequestMapping("/produtos")
@Tag(name = "Produtos", description = "Grupo de requisições de Produtos.")
public class ProdutoController {
    private final ProdutoService service;

    public ProdutoController(ProdutoService service) {
        this.service = service;
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
    @PostMapping("/criar/{idRestaurante}")
    public ResponseEntity<Produto> criar(
            @RequestBody @Validated ProdutoCriacaoDto produtoCriacaoDto,
            @PathVariable String idRestaurante
    ) {
        return ResponseEntity.status(201).body(this.service.criar(produtoCriacaoDto, idRestaurante));
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
    @GetMapping("/todos/{idRestaurante}")
    public ResponseEntity<List<ProdutoGetDto>> get(@PathVariable String idRestaurante) {
        final var allProdutos = service.getAll(idRestaurante).stream().map(ProdutoGetDto::new).toList();
        return allProdutos.isEmpty() ? ResponseEntity.status(204).build() : ResponseEntity.ok(allProdutos);
    }

    @GetMapping("/{idProduto}")
    public ResponseEntity<Produto> getProduto(@PathVariable String idProduto) {
        return ResponseEntity.of(service.getById(idProduto));
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
    @PutMapping("/editar/{idProduto}")
    public ResponseEntity<Produto> update(
            @RequestBody @Validated ProdutoUpdateDto produtoUpdateDto,
            @PathVariable String idProduto
    ) {
        return ResponseEntity.ok(this.service.update(produtoUpdateDto, idProduto));
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
    @DeleteMapping("/deletar/{idProduto}")
    public ResponseEntity<Void> delete(@PathVariable String idProduto) {
        this.service.delete(idProduto);

        return ResponseEntity.status(200).build();
    }
}
