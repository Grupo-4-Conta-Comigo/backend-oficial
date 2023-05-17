package comigo.conta.backend.oficial.domain.pagamento;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Arrays;

@Entity
public class DetalhesPagamento {
    @Id
    private String id;
    private String chavePix;
    private String clientId;
    private String clientSecret;
    @JsonIgnore
    @Column(length = 1024 * 1024)
    private byte[] certificado;
    private String nomeCertificado;
    @Column(unique = true)
    private String usuarioId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChavePix() {
        return chavePix;
    }

    public void setChavePix(String chavePix) {
        this.chavePix = chavePix;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public byte[] getCertificado() {
        return certificado;
    }

    public void setCertificado(byte[] certificado) {
        this.certificado = certificado;
    }

    public String getNomeCertificado() {
        return nomeCertificado;
    }

    public void setNomeCertificado(String nomeCertificado) {
        this.nomeCertificado = nomeCertificado;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    @Override
    public String toString() {
        return "DetalhesPagamento{" +
                "id='" + id + '\'' +
                ", chavePix='" + chavePix + '\'' +
                ", clientId='" + clientId + '\'' +
                ", clientSecret='" + clientSecret + '\'' +
                ", certificado=" + Arrays.toString(certificado) +
                ", nomeCertificado='" + nomeCertificado + '\'' +
                ", usuarioId='" + usuarioId + '\'' +
                '}';
    }
}
