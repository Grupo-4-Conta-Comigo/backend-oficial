package school.sptech.exemplojwt.api.controller.produto;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import school.sptech.exemplojwt.domain.produto.Produto;
import school.sptech.exemplojwt.service.produto.ProdutoService;
import school.sptech.exemplojwt.service.produto.dto.ProdutoCriacaoDto;
import school.sptech.exemplojwt.service.produto.dto.ProdutoGetDto;
import school.sptech.exemplojwt.service.produto.dto.ProdutoUpdateDto;

import java.util.List;

@RestController
@RequestMapping("/produtos")
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
