package comigo.conta.backend.oficial.service.pedido.submodules.item_comanda.dto;

import comigo.conta.backend.oficial.domain.pedido.submodules.comanda.Comanda;
import comigo.conta.backend.oficial.domain.pedido.submodules.item_comanda.ItemComanda;
import comigo.conta.backend.oficial.domain.produto.Produto;

import javax.validation.constraints.NotBlank;

public class ItemComandaCriacaoDto {
    @NotBlank
    private String idComanda;
    @NotBlank
    private String idProduto;
    private String observacao;

    public String getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(String idProduto) {
        this.idProduto = idProduto;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public ItemComanda toEntity() {
        final Produto produto = new Produto();
        produto.setId(idProduto);

        final Comanda comanda = new Comanda();
        comanda.setId(idComanda);

        final ItemComanda itemComanda = new ItemComanda();
        itemComanda.setObservacao(observacao);
        itemComanda.setProduto(produto);
        itemComanda.setComanda(comanda);

        return itemComanda;
    }

    public String getIdComanda() {
        return idComanda;
    }

    public void setIdComanda(String idComanda) {
        this.idComanda = idComanda;
    }
}
