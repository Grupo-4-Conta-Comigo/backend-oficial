package comigo.conta.backend.oficial.service.produto.dto;

import comigo.conta.backend.oficial.domain.produto.Categoria;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;

public class ProdutoUpdateDto {
    @NotBlank
    private String nome;
    @NotBlank
    private Categoria categoria;
    @DecimalMin("0.0")
    private double preco;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }
}
