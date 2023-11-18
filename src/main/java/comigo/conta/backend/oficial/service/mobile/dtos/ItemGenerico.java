package comigo.conta.backend.oficial.service.mobile.dtos;

import lombok.*;

@Data
@AllArgsConstructor
public class ItemGenerico {
    private String nome;
    private double preco;
    private int quantidade;
    private String observacao;
}
