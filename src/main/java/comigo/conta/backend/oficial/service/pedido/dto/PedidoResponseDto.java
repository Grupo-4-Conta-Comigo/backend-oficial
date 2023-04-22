package comigo.conta.backend.oficial.service.pedido.dto;

import comigo.conta.backend.oficial.domain.pedido.Pedido;
import comigo.conta.backend.oficial.domain.shared.Status;
import comigo.conta.backend.oficial.domain.shared.usecases.GetPriceFromComandaUsecase;
import comigo.conta.backend.oficial.domain.shared.usecases.GetPriceFromPedidoUsecase;
import comigo.conta.backend.oficial.service.pedido.submodules.comanda.dto.ComandaResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public class PedidoResponseDto {
    private final String id;
    private final int mesa;
    private final Status status;
    private final double preco;
    private final String idRestaurante;
    private final LocalDateTime dataCriacao;
    private final List<ComandaResponseDto> comandas;

    public PedidoResponseDto(Pedido pedido) {
        this.id = pedido.getId();
        this.mesa = pedido.getMesa();
        this.status = pedido.getStatus();
        this.comandas = pedido.getComandas().stream().map(ComandaResponseDto::new).toList();
        this.idRestaurante = pedido.getIdRestaurante();
        this.dataCriacao = pedido.getDataCriacao();
        this.preco = new GetPriceFromPedidoUsecase(new GetPriceFromComandaUsecase()).execute(pedido);
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

    public List<ComandaResponseDto> getComandas() {
        return comandas;
    }

    public String getIdRestaurante() {
        return idRestaurante;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }
}
