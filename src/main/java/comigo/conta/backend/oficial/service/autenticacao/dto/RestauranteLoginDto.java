package comigo.conta.backend.oficial.service.autenticacao.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;

public class RestauranteLoginDto {
    @NotBlank
    @Schema(example = "conta.comigo@email.com")
    private String email;
    @NotBlank
    @Schema(example = "123456")
    private String senha;

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
}
