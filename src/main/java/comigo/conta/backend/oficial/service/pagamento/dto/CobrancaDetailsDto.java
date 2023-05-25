package comigo.conta.backend.oficial.service.pagamento.dto;

import java.util.Map;

public class CobrancaDetailsDto {
    private final String status;
    private final String creationDateTime;
    private final String txid;
    private final Double valor;

    @SuppressWarnings("unchecked cast")
    public CobrancaDetailsDto(Map<String, Object> json) {
        this.status = json.get("status").toString();
        this.creationDateTime = ((Map<String, Object>) json.get("calendario")).get("criacao").toString();
        this.txid = json.get("txid").toString();
        this.valor = Double.valueOf(((Map<String, Object>) json.get("valor")).get("original").toString());
    }

    public String getStatus() {
        return status;
    }

    public String getCreationDateTime() {
        return creationDateTime;
    }

    public String getTxid() {
        return txid;
    }

    public Double getValor() {
        return valor;
    }
}
