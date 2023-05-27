package comigo.conta.backend.oficial.service.calculo.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CalculoPersonaliazdoPaganteDto {
    @NotBlank
    private String idComanda;
    @NotBlank
    private String nome;
    @DecimalMin("0")
    @NotNull
    private double valorAPagar;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getValorAPagar() {
        return valorAPagar;
    }

    public void setValorAPagar(double valorAPagar) {
        this.valorAPagar = valorAPagar;
    }

    public String getIdComanda() {
        return idComanda;
    }

    public void setIdComanda(String idComanda) {
        this.idComanda = idComanda;
    }
}
