package comigo.conta.backend.oficial.service.produto;

import comigo.conta.backend.oficial.domain.produto.Categoria;
import comigo.conta.backend.oficial.domain.produto.Produto;
import comigo.conta.backend.oficial.domain.produto.repository.ProdutoRepository;
import comigo.conta.backend.oficial.service.produto.dto.ProdutoCriacaoDto;
import comigo.conta.backend.oficial.service.produto.dto.ProdutoMapper;
import comigo.conta.backend.oficial.service.produto.dto.ProdutoUpdateDto;
import comigo.conta.backend.oficial.service.usuario.UsuarioService;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {
    private final ProdutoRepository repository;
    private final UsuarioService usuarioService;

    public ProdutoService(ProdutoRepository repository, UsuarioService usuarioService) {
        this.repository = repository;
        this.usuarioService = usuarioService;
    }

    public Produto criar(ProdutoCriacaoDto produtoCriacaoDto, String idRestaurante) {
        if (usuarioService.naoExiste(idRestaurante)) {
            throw new ResponseStatusException(404, "Restaurante não encontrado!", null);
        }
        final var novoProduto = ProdutoMapper.of(produtoCriacaoDto, idRestaurante);
        return this.repository.save(novoProduto);
    }

    public List<Produto> getAll(String idRestaurante, Optional<Categoria> categoria) {
        return categoria.isPresent()
                ? repository.findByIdRestauranteAndCategoria(idRestaurante, categoria.get())
                : repository.findAllByIdRestaurante(idRestaurante);
    }

    public Produto update(ProdutoUpdateDto produtoUpdateDto, Long idPedido) {
        final var produtoAtual = this.repository
                .findById(idPedido)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                404,
                                "Produto não encontrado!",
                                null
                        )
                );
        produtoAtual.setCategoria(produtoUpdateDto.getCategoria());
        produtoAtual.setNome(produtoUpdateDto.getNome());
        produtoAtual.setPreco(produtoUpdateDto.getPreco());

        return this.repository.save(produtoAtual);
    }

    public void delete(Long idProduto) {
        if (!repository.existsById(idProduto)) {
            throw new ResponseStatusException(404, String.format("Produto com id %s não encontrado", idProduto), null);
        }
        this.repository.deleteById(idProduto);
    }

    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    public Optional<Produto> getById(Long idProduto) {
        return repository.findById(idProduto);
    }
}
