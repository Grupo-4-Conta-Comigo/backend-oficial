package comigo.conta.backend.oficial.service.pedido.dto;

import comigo.conta.backend.oficial.domain.pedido.Pedido;
import comigo.conta.backend.oficial.domain.pedido.submodules.comanda.Comanda;
import comigo.conta.backend.oficial.domain.shared.Status;
import comigo.conta.backend.oficial.domain.shared.usecases.GetPriceFromComandaUsecase;
import comigo.conta.backend.oficial.domain.shared.usecases.GetPriceFromPedidoUsecase;

import java.time.LocalDateTime;
import java.util.List;

public class PedidoResponseDto {
    private final static GetPriceFromPedidoUsecase usecase = new GetPriceFromPedidoUsecase(new GetPriceFromComandaUsecase());
    private final String id;
    private final int mesa;
    private final Status status;
    private final double preco;
    private final List<Comanda> comandas;
    private final String idRestaurante;
    private final LocalDateTime dataCriacao;

    public PedidoResponseDto(Pedido pedido) {
        this.id = pedido.getId();
        this.mesa = pedido.getMesa();
        this.status = pedido.getStatus();
        this.comandas = pedido.getComandas();
        this.idRestaurante = pedido.getIdRestaurante();
        this.dataCriacao = pedido.getDataCriacao();
        this.preco = usecase.execute(pedido);
    }

    public String getId() {
        return id;
    }

    public int getMesa() {
        return mesa;
    }

    public Status getStatus() {
        return status;
    }

    public double getPreco() {
        return preco;
    }

    public List<Comanda> getComandas() {
        return comandas;
    }

    public String getIdRestaurante() {
        return idRestaurante;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }
}
