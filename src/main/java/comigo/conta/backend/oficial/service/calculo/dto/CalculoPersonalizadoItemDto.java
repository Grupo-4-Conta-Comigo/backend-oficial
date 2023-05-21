package comigo.conta.backend.oficial.service.calculo.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

public class CalculoPersonalizadoItemDto {
    @NotBlank
    private String nome;
    @DecimalMin("0")
    @NotNull
    private double preco;
    @NotBlank
    private String donoOriginal;
    private List<CalculoPersonaliazdoPaganteDto> pagantes;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public String getDonoOriginal() {
        return donoOriginal;
    }

    public void setDonoOriginal(String donoOriginal) {
        this.donoOriginal = donoOriginal;
    }

    public List<CalculoPersonaliazdoPaganteDto> getPagantes() {
        return pagantes;
    }

    public void setPagantes(List<CalculoPersonaliazdoPaganteDto> pagantes) {
        this.pagantes = pagantes;
    }
}
