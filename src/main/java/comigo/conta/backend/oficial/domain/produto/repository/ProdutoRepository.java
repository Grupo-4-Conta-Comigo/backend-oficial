package comigo.conta.backend.oficial.domain.produto.repository;

import comigo.conta.backend.oficial.domain.produto.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import comigo.conta.backend.oficial.domain.produto.Produto;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, String> {
    List<Produto> findByIdRestauranteAndCategoria(String idRestaurante, Categoria categoria);
    List<Produto> findAllByIdRestaurante(String id);
}
