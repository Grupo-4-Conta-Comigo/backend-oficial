package comigo.conta.backend.oficial.service.mobile.dtos;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
public class PedidoGenerico {
    private int mesa;
    private List<ItemGenerico> itens;
}
