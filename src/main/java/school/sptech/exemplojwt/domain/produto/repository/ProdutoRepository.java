package school.sptech.exemplojwt.domain.produto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import school.sptech.exemplojwt.domain.produto.Produto;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, String> {
    List<Produto> findAllByIdRestaurante(String id);
}
