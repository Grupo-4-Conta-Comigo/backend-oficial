package school.sptech.exemplojwt.service.pedido.dto;

import school.sptech.exemplojwt.domain.pedido.Pedido;
import school.sptech.exemplojwt.domain.shared.Status;

public class PedidoCriacaoDto {
    private int mesa;

    public int getMesa() {
        return mesa;
    }

    public void setMesa(int mesa) {
        this.mesa = mesa;
    }

    public Pedido toEntity() {
        final Pedido novoPedido = new Pedido();

        novoPedido.setMesa(mesa);
        novoPedido.setStatus(Status.ativo);

        return novoPedido;
    }
}
