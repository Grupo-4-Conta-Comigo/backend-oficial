package comigo.conta.backend.oficial.service.calculo.dto;

public class CalculoPersonalizadoResponseDto {
    private final String nome;
    private final double valorAPagar;

    public CalculoPersonalizadoResponseDto(String nome, double valorAPagar) {
        this.nome = nome;
        this.valorAPagar = valorAPagar;
    }

    public String getNome() {
        return nome;
    }

    public double getValorAPagar() {
        return valorAPagar;
    }
}
