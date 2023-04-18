package comigo.conta.backend.oficial.service.pedido.dto;

import comigo.conta.backend.oficial.domain.shared.Status;

import java.util.List;

public class ComandaCriacaoDto {
    private String id;
    private String nomeDono;
    private Status status;
    private List<ItemComandaCriacaoDto> itensComanda;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNomeDono() {
        return nomeDono;
    }

    public void setNomeDono(String nomeDono) {
        this.nomeDono = nomeDono;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<ItemComandaCriacaoDto> getItensComanda() {
        return itensComanda;
    }

    public void setItensComanda(List<ItemComandaCriacaoDto> itensComanda) {
        this.itensComanda = itensComanda;
    }
}
