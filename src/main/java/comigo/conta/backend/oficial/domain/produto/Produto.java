package comigo.conta.backend.oficial.domain.produto;

import comigo.conta.backend.oficial.domain.pedido.submodules.item_comanda.ItemComanda;

import javax.persistence.*;
import java.util.List;

@Entity
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private Categoria categoria;
    private double preco;
    private String idRestaurante;
    @OneToMany(mappedBy = "produto")
    private List<ItemComanda> itensComanda;

    public Produto() {
    }

    public Produto(String nome, Categoria categoria, double preco, String idRestaurante) {
        this.nome = nome;
        this.categoria = categoria;
        this.preco = preco;
        this.idRestaurante = idRestaurante;
    }

    public Produto(Long id, String nome, Categoria categoria, double preco, String idRestaurante) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
        this.preco = preco;
        this.idRestaurante = idRestaurante;
    }

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

    public String getIdRestaurante() {
        return idRestaurante;
    }

    public void setIdRestaurante(String idRestaurante) {
        this.idRestaurante = idRestaurante;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
