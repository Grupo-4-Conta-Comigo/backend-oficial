package school.sptech.exemplojwt.domain.pedido.submodules.item_comanda;

import com.fasterxml.jackson.annotation.JsonIgnore;
import school.sptech.exemplojwt.domain.pedido.submodules.comanda.Comanda;
import school.sptech.exemplojwt.domain.produto.Produto;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class ItemComanda {

    @Id
    private String id;
    @ManyToOne
    private Produto produto;
    private String observacao;
    @ManyToOne
    private Comanda comanda;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
    public void setComanda(Comanda comanda) {
        this.comanda = comanda;
    }
}
