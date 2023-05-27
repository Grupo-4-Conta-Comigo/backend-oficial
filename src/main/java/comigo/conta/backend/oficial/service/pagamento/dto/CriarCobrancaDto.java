package comigo.conta.backend.oficial.service.pagamento.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;

public class CriarCobrancaDto {
    @NotBlank
    private String idRestaurante;
    @NotBlank
    private String idComanda;
    @DecimalMin("0")
    private double valor;

    public String getIdRestaurante() {
        return idRestaurante;
    }

    public void setIdRestaurante(String idRestaurante) {
        this.idRestaurante = idRestaurante;
    }

    public String getIdComanda() {
        return idComanda;
    }

    public void setIdComanda(String idComanda) {
        this.idComanda = idComanda;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}
