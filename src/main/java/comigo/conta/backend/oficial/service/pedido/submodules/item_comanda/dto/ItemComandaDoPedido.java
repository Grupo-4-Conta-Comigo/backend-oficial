package comigo.conta.backend.oficial.service.pedido.submodules.item_comanda.dto;

import comigo.conta.backend.oficial.domain.produto.Produto;

public record ItemComandaDoPedido(String id, String idComanda, String observacao, String nomeDono, Produto produto) {
}
