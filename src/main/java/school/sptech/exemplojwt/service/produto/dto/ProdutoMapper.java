package school.sptech.exemplojwt.service.produto.dto;

import school.sptech.exemplojwt.domain.produto.Produto;

public class ProdutoMapper {
    public static Produto of(ProdutoCriacaoDto produtoCriacaoDto, String idRestaurante) {
        return new Produto(
                produtoCriacaoDto.getNome(),
                produtoCriacaoDto.getCategoria(),
                produtoCriacaoDto.getPreco(),
                idRestaurante
        );
    }

    public static Produto of(ProdutoUpdateDto produtoUpdateDto, String idRestaurante) {
        return new Produto(
                produtoUpdateDto.getId(),
                produtoUpdateDto.getNome(),
                produtoUpdateDto.getCategoria(),
                produtoUpdateDto.getPreco(),
                idRestaurante
        );
    }
}
