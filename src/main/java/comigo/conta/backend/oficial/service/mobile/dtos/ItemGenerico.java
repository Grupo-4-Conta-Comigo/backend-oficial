package comigo.conta.backend.oficial.service.mobile.dtos;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@ToString
public record ItemGenerico(String nome, double preco, int quantidade, String observacao) {
}
