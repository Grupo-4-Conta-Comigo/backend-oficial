package comigo.conta.backend.oficial.service.produto;

import comigo.conta.backend.oficial.domain.shared.usecases.GenerateRandomIdUsecase;
import comigo.conta.backend.oficial.service.produto.dto.ProdutoCriacaoDto;
import comigo.conta.backend.oficial.service.produto.dto.ProdutoMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import comigo.conta.backend.oficial.domain.produto.Produto;
import comigo.conta.backend.oficial.domain.produto.repository.ProdutoRepository;
import comigo.conta.backend.oficial.service.produto.dto.ProdutoUpdateDto;

import java.util.List;

@Service
public class ProdutoService {
    private final ProdutoRepository repository;
    private final GenerateRandomIdUsecase generateRandomIdUsecase;

    public ProdutoService(ProdutoRepository repository, GenerateRandomIdUsecase generateRandomIdUsecase) {
        this.repository = repository;
        this.generateRandomIdUsecase = generateRandomIdUsecase;
    }

    public Produto criar(ProdutoCriacaoDto produtoCriacaoDto, String idRestaurante) {
        final var novoProduto = ProdutoMapper.of(produtoCriacaoDto, idRestaurante);

        final String id = generateRandomIdUsecase.execute();
        novoProduto.setId(id);

        return this.repository.save(novoProduto);
    }

    public List<Produto> getAll(String idRestaurante) {
        return this.repository.findAllByIdRestaurante(idRestaurante);
    }

    public Produto update(ProdutoUpdateDto produtoUpdateDto, String idRestaurante) {
        final var produtoAntigo = this.repository.findById(produtoUpdateDto.getId()).orElseThrow(() -> new ResponseStatusException(404, String.format("Produto com id %s não encontrado", produtoUpdateDto.getId()), null));
        final var updatedProduto = ProdutoMapper.of(produtoUpdateDto, idRestaurante);
        if (!produtoAntigo.getIdRestaurante().equals(updatedProduto.getIdRestaurante())) {
            throw new ResponseStatusException(400, String.format("Produto com id %s não pertence ao restaurante com id %s", produtoAntigo.getId(), updatedProduto.getIdRestaurante()), null);
        }
        System.out.println(updatedProduto.getId());
        return this.repository.save(updatedProduto);
    }

    public void delete(String idProduto) {
        if (!repository.existsById(idProduto)) {
            throw new ResponseStatusException(404, String.format("Produto com id %s não encontrado", idProduto), null);
        }
        this.repository.deleteById(idProduto);
    }
}
