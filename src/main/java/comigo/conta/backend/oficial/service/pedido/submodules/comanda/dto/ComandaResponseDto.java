package comigo.conta.backend.oficial.service.pedido.submodules.comanda.dto;

import comigo.conta.backend.oficial.domain.pedido.submodules.comanda.Comanda;
import comigo.conta.backend.oficial.domain.pedido.submodules.item_comanda.ItemComanda;
import comigo.conta.backend.oficial.domain.shared.Status;
import comigo.conta.backend.oficial.domain.shared.usecases.GetPriceFromComandaUsecase;

import java.util.List;

public class ComandaResponseDto {
    private final String id;
    private final String nomeDono;
    private final Status status;
    private final double preco;
    private final Integer idQRCodePix;
    private final List<ItemComanda> itensComanda;

    public ComandaResponseDto(Comanda comanda) {
        this.id = comanda.getId();
        this.nomeDono = comanda.getNomeDono();
        this.status = comanda.getStatus();
        this.idQRCodePix = comanda.getIdQRCodePix();
        this.preco = new GetPriceFromComandaUsecase().execute(comanda);
        this.itensComanda = comanda.getItensComanda();
    }

    public String getId() {
        return id;
    }

    public String getNomeDono() {
        return nomeDono;
    }

    public Status getStatus() {
        return status;
    }

    public List<ItemComanda> getItensComanda() {
        return itensComanda;
    }

    public double getPreco() {
        return preco;
    }

    public Integer getIdQRCodePix() {
        return idQRCodePix;
    }
}
