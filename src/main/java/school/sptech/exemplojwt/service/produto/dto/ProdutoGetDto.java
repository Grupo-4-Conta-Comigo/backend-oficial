package school.sptech.exemplojwt.service.produto.dto;

import school.sptech.exemplojwt.domain.produto.Produto;

public class ProdutoGetDto {
    private final String id;
    private final String nome;
    private final String categoria;
    private final double preco;
    private final String idRestaurante;

    public ProdutoGetDto(Produto produto) {
        this.id = produto.getId();
        this.nome = produto.getNome();
        this.categoria = produto.getCategoria();
        this.preco = produto.getPreco();
        this.idRestaurante = produto.getIdRestaurante();
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public double getPreco() {
        return preco;
    }

    public String getIdRestaurante() {
        return idRestaurante;
    }
}
