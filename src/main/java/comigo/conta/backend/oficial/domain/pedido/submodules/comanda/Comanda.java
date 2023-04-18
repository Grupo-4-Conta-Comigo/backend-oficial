package comigo.conta.backend.oficial.domain.pedido.submodules.comanda;

import com.fasterxml.jackson.annotation.JsonIgnore;
import comigo.conta.backend.oficial.domain.pedido.submodules.item_comanda.ItemComanda;
import comigo.conta.backend.oficial.domain.shared.Status;
import comigo.conta.backend.oficial.domain.pedido.Pedido;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Comanda {
    @Id
    private String id;
    private String nomeDono;
    private Status status;
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
        return itensComanda;
    }

    public void setItensComanda(List<ItemComanda> itensComanda) {
        this.itensComanda = itensComanda;
    }
}
