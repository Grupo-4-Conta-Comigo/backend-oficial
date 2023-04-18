package comigo.conta.backend.oficial.service.pedido.dto;

import comigo.conta.backend.oficial.domain.pedido.Pedido;
import comigo.conta.backend.oficial.domain.shared.Status;

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
