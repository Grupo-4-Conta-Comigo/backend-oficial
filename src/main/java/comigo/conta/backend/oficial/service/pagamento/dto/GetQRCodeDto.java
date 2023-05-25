package comigo.conta.backend.oficial.service.pagamento.dto;

import javax.validation.constraints.NotBlank;

public class GetQRCodeDto {
    @NotBlank
    String idComanda;
    @NotBlank
    String idRestaurante;

    public String getIdComanda() {
        return idComanda;
    }

    public void setIdComanda(String idComanda) {
        this.idComanda = idComanda;
    }

    public String getIdRestaurante() {
        return idRestaurante;
    }

    public void setIdRestaurante(String idRestaurante) {
        this.idRestaurante = idRestaurante;
    }
}
