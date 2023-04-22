package comigo.conta.backend.oficial.service.produto.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;

public class ProdutoUpdateDto {
    @NotBlank
    private String nome;
    @NotBlank
    private String categoria;
    @DecimalMin("0.0")
    private double preco;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }
}
