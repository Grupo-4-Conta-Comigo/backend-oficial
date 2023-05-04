package comigo.conta.backend.oficial.service.usuario.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class GarcomCriacaoDto {
    @NotBlank
    private String nome;
    @CPF
    @NotBlank
    @Schema(example = "500.757.680-89")
    private String cpf;
    @Email
    private String email;
    @NotBlank
    @Size(min = 8)
    private String senha;
    @NotBlank
    private String restauranteId;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getRestauranteId() {
        return restauranteId;
    }

    public void setRestauranteId(String restauranteId) {
        this.restauranteId = restauranteId;
    }
}
