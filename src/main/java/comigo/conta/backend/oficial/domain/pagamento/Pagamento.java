package comigo.conta.backend.oficial.domain.pagamento;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Pagamento {
    @Id
    private String id;

    private String idRestaurante;
    private String chavePix;
    private double valorPagamento;
    private boolean pagamentoConcluido;
    private boolean foiUsadoPix;
    private LocalDateTime dataHoraPagamento;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdRestaurante() {
        return idRestaurante;
    }

    public void setIdRestaurante(String idRestaurante) {
        this.idRestaurante = idRestaurante;
    }

    public String getChavePix() {
        return chavePix;
    }

    public void setChavePix(String chavePix) {
        this.chavePix = chavePix;
    }

    public double getValorPagamento() {
        return valorPagamento;
    }

    public void setValorPagamento(double valorPagamento) {
        this.valorPagamento = valorPagamento;
    }

    public boolean isPagamentoConcluido() {
        return pagamentoConcluido;
    }

    public void setPagamentoConcluido(boolean pagamentoConcluido) {
        this.pagamentoConcluido = pagamentoConcluido;
    }

    public LocalDateTime getDataHoraPagamento() {
        return dataHoraPagamento;
    }

    public void setDataHoraPagamento(LocalDateTime dataHoraPagamento) {
        this.dataHoraPagamento = dataHoraPagamento;
    }

    public boolean isFoiUsadoPix() {
        return foiUsadoPix;
    }

    public void setFoiUsadoPix(boolean foiUsadoPix) {
        this.foiUsadoPix = foiUsadoPix;
    }
}
