package comigo.conta.backend.oficial.domain.pagamento;

import comigo.conta.backend.oficial.domain.usuario.Usuario;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.io.File;

@Entity
public class DetalhesPagamento {
    @Id
    private String id;
    private String chavePix;
    private String clientId;
    private String clientSecret;
    private File certificado;
    @OneToOne
    private Usuario usuarioId;

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

    public File getCertificado() {
        return certificado;
    }

    public void setCertificado(File certificado) {
        this.certificado = certificado;
    }
}
