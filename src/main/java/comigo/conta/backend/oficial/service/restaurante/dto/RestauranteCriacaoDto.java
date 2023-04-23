package comigo.conta.backend.oficial.service.restaurante.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.validator.constraints.br.CNPJ;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class RestauranteCriacaoDto {
    @NotBlank
    private String nome;
    @CNPJ
    @Schema(example = "05.356.159/0001-67")
    private String cnpj;

    @Email
    @NotBlank
    private String email;
    @NotBlank
    @Size(min = 8)
    private String senha;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
}
