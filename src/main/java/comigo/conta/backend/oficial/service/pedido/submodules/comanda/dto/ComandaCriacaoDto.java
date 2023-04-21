package comigo.conta.backend.oficial.service.pedido.submodules.comanda.dto;

import comigo.conta.backend.oficial.domain.pedido.Pedido;
import comigo.conta.backend.oficial.domain.pedido.submodules.comanda.Comanda;
import comigo.conta.backend.oficial.domain.shared.Status;

public class ComandaCriacaoDto {
    private String nomeDono;

    public Comanda toEntity(String idPedido) {
        final Pedido pedido = new Pedido();
        pedido.setId(idPedido);

        final Comanda comanda = new Comanda();
        comanda.setNomeDono(nomeDono);
        comanda.setStatus(Status.ativo);
        comanda.setPedido(pedido);

        return comanda;
    }

    public String getNomeDono() {
        return nomeDono;
    }

    public void setNomeDono(String nomeDono) {
        this.nomeDono = nomeDono;
    }
}
