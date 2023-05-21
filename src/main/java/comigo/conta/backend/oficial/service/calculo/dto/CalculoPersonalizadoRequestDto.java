package comigo.conta.backend.oficial.service.calculo.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class CalculoPersonalizadoRequestDto {
    @DecimalMin("0")
    @NotNull
    private double valorTotal;
    @NotEmpty
    private List<CalculoPersonalizadoItemDto> itens;

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public List<CalculoPersonalizadoItemDto> getItens() {
        return itens;
    }

    public void setItens(List<CalculoPersonalizadoItemDto> itens) {
        this.itens = itens;
    }
}
