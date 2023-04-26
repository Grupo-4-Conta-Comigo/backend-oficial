package comigo.conta.backend.oficial.service.autenticacao.dto;

import javax.validation.constraints.NotBlank;

public class RestauranteMudarSenhaDto {
    @NotBlank
    private String email;
    @NotBlank
    private String senhaAntiga;
    @NotBlank
    private String senhaNova;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenhaAntiga() {
        return senhaAntiga;
    }

    public void setSenhaAntiga(String senhaAntiga) {
        this.senhaAntiga = senhaAntiga;
    }

    public String getSenhaNova() {
        return senhaNova;
    }

    public void setSenhaNova(String senhaNova) {
        this.senhaNova = senhaNova;
    }
}
