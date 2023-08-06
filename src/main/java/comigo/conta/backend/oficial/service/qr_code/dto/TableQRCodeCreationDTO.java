package comigo.conta.backend.oficial.service.qr_code.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class TableQRCodeCreationDTO {
    @NotBlank
    private String restauranteId;
    @NotNull
    private int mesa;

    public String getRestauranteId() {
        return restauranteId;
    }

    public void setRestauranteId(String restauranteId) {
        this.restauranteId = restauranteId;
    }

    public int getMesa() {
        return mesa;
    }

    public void setMesa(int mesa) {
        this.mesa = mesa;
    }
}
