package comigo.conta.backend.oficial.service.pagamento.dto;

public class CriarPagamentoDto {
    private String idRestaurante;
    private int numeroMesa;
    private String nomePagante;
    private String chavePix;
    private double valorPagamento;
    private boolean pagamentoConcluido;
    private boolean pix;

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

    public boolean isPix() {
        return pix;
    }

    public void setPix(boolean pix) {
        this.pix = pix;
    }

    public String getNomePagante() {
        return nomePagante;
    }

    public void setNomePagante(String nomePagante) {
        this.nomePagante = nomePagante;
    }

    public int getNumeroMesa() {
        return numeroMesa;
    }

    public void setNumeroMesa(int numeroMesa) {
        this.numeroMesa = numeroMesa;
    }
}
