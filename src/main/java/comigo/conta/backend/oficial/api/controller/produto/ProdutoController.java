package comigo.conta.backend.oficial.api.controller.produto;

import comigo.conta.backend.oficial.domain.produto.Produto;
import comigo.conta.backend.oficial.service.produto.ProdutoService;
import comigo.conta.backend.oficial.service.produto.dto.ProdutoCriacaoDto;
import comigo.conta.backend.oficial.service.produto.dto.ProdutoGetDto;
import comigo.conta.backend.oficial.service.produto.dto.ProdutoUpdateDto;
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

    @PostMapping("/criar/{idRestaurante}")
    public ResponseEntity<Produto> criar(
            @RequestBody @Validated ProdutoCriacaoDto produtoCriacaoDto,
            @PathVariable String idRestaurante
    ) {
        return ResponseEntity.status(201).body(this.service.criar(produtoCriacaoDto, idRestaurante));
    }

    @GetMapping("/{idRestaurante}")
    public ResponseEntity<List<ProdutoGetDto>> get(@PathVariable String idRestaurante) {
        final var allProdutos = service.getAll(idRestaurante).stream().map(ProdutoGetDto::new).toList();
        return allProdutos.isEmpty() ? ResponseEntity.status(204).build() : ResponseEntity.ok(allProdutos);
    }

    @PutMapping("/editar/{idRestaurante}")
    public ResponseEntity<ProdutoGetDto> update(
            @RequestBody @Validated ProdutoUpdateDto produtoUpdateDto,
            @PathVariable String idRestaurante
    ) {
        final var savedProduto = this.service.update(produtoUpdateDto, idRestaurante);
        return ResponseEntity.ok(new ProdutoGetDto(savedProduto));
    }

    @DeleteMapping("/{idProduto}")
    public ResponseEntity<Void> delete(@PathVariable String idProduto) {
        this.service.delete(idProduto);

        return ResponseEntity.status(200).build();
    }
}
