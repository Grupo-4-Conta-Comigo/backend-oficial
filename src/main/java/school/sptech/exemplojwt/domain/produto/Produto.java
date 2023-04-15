package school.sptech.exemplojwt.domain.produto;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Produto {

    @Id
    private String id;
    private String nome;
    private String categoria;
    private double preco;
    private String idRestaurante;

    public Produto() {
    }

    public Produto(String nome, String categoria, double preco, String idRestaurante) {
        this.nome = nome;
        this.categoria = categoria;
        this.preco = preco;
        this.idRestaurante = idRestaurante;
    }

    public Produto(String id, String nome, String categoria, double preco, String idRestaurante) {
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

    public String getIdRestaurante() {
        return idRestaurante;
    }

    public void setIdRestaurante(String idRestaurante) {
        this.idRestaurante = idRestaurante;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
