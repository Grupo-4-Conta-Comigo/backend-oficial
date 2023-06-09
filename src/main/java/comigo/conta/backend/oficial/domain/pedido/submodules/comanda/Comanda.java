package comigo.conta.backend.oficial.domain.pedido.submodules.comanda;

import com.fasterxml.jackson.annotation.JsonIgnore;
import comigo.conta.backend.oficial.domain.pedido.submodules.item_comanda.ItemComanda;
import comigo.conta.backend.oficial.domain.shared.Status;
import comigo.conta.backend.oficial.domain.pedido.Pedido;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Comanda {
    @Id
    private String id;
    private String nomeDono;
    private Status status;
    @ElementCollection
    private List<Integer> idQRCodePix;
    @ManyToOne
    private Pedido pedido;
    @OneToMany(mappedBy = "comanda")
    private List<ItemComanda> itensComanda;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
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

    @JsonIgnore
    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public List<ItemComanda> getItensComanda() {
        return itensComanda != null ? itensComanda : new ArrayList<>();
    }

    public void setItensComanda(List<ItemComanda> itensComanda) {
        this.itensComanda = itensComanda;
    }

    public List<Integer> getIdQRCodePix() {
        return idQRCodePix;
    }

    public void setIdQRCodePix(List<Integer> idQRCodePix) {
        this.idQRCodePix = idQRCodePix;
    }
}
