package comigo.conta.backend.oficial.service.pagamento.dto;

import javax.validation.constraints.NotBlank;

public class DetalhesPagamentoCriacaoDto {
    @NotBlank
    private String chavePix;
    @NotBlank
    private String clientId;
    @NotBlank
    private String clientSecret;

    public String getChavePix() {
        return chavePix;
    }

    public void setChavePix(String chavePix) {
        this.chavePix = chavePix;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }
}
