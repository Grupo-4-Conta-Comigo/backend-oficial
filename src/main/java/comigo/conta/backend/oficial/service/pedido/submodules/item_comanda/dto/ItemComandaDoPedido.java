package comigo.conta.backend.oficial.service.pedido.submodules.item_comanda.dto;

import comigo.conta.backend.oficial.domain.produto.Produto;

public class ItemComandaDoPedido {
    final private String id;
    final private String observacao;
    final private String nomeDono;
    final private Produto produto;

    public ItemComandaDoPedido(String id, String observacao, String nomeDono, Produto produto) {
        this.id = id;
        this.observacao = observacao;
        this.nomeDono = nomeDono;
        this.produto = produto;
    }

    public String getId() {
        return id;
    }

    public String getObservacao() {
        return observacao;
    }

    public String getNomeDono() {
        return nomeDono;
    }

    public Produto getProduto() {
        return produto;
    }
}
