package comigo.conta.backend.oficial.domain.usuario;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Usuario {
    @Id
    private String id;
    private String nome;
    @Schema(example = "05.356.159/0001-67")
    private String registro;
    private Cargo cargo;
    private String email;
    private String senha;
    private String restauranteId;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRegistro() {
        return registro;
    }

    public void setRegistro(String registro) {
        this.registro = registro;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonIgnore
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public String getRestauranteId() {
        return restauranteId;
    }

    public void setRestauranteId(String restauranteId) {
        this.restauranteId = restauranteId;
    }
}
