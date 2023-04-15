package school.sptech.exemplojwt.service.pedido.dto;

import school.sptech.exemplojwt.domain.pedido.Pedido;
import school.sptech.exemplojwt.domain.shared.Status;

public class PedidoUpdateDto {
    private Status status;
    private Integer mesa;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getMesa() {
        return mesa;
    }

    public void setMesa(Integer mesa) {
        this.mesa = mesa;
    }

    public Pedido toEntity() {
        final Pedido pedido = new Pedido();

        pedido.setMesa(mesa);
        pedido.setStatus(status);

        return pedido;
    }
}
