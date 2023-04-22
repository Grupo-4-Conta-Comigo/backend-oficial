package comigo.conta.backend.oficial.service.produto.dto;

import comigo.conta.backend.oficial.domain.produto.Produto;

public class ProdutoMapper {
    public static Produto of(ProdutoCriacaoDto produtoCriacaoDto, String idRestaurante) {
        return new Produto(
                produtoCriacaoDto.getNome(),
                produtoCriacaoDto.getCategoria(),
                produtoCriacaoDto.getPreco(),
                idRestaurante
        );
    }
}
