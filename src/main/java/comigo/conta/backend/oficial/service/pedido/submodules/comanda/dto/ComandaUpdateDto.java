package comigo.conta.backend.oficial.service.pedido.submodules.comanda.dto;

import comigo.conta.backend.oficial.domain.shared.Status;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ComandaUpdateDto {
    @NotNull
    private Status status;
    @NotBlank
    private String nomeDono;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getNomeDono() {
        return nomeDono;
    }

    public void setNomeDono(String nomeDono) {
        this.nomeDono = nomeDono;
    }
}
