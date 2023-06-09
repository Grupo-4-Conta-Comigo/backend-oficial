package comigo.conta.backend.oficial.domain.pedido.submodules.item_comanda;

import comigo.conta.backend.oficial.domain.pedido.submodules.comanda.Comanda;
import comigo.conta.backend.oficial.domain.produto.Produto;

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


    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public void setComanda(Comanda comanda) {
        this.comanda = comanda;
    }
}
