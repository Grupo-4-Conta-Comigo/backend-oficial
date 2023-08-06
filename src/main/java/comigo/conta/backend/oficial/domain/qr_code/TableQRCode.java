package comigo.conta.backend.oficial.domain.qr_code;

import comigo.conta.backend.oficial.domain.usuario.Usuario;

import javax.persistence.*;

@Entity
public class TableQRCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private byte[] qrCodeImage;
    private int table;
    @OneToOne
    private Usuario user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getQrCodeImage() {
        return qrCodeImage;
    }

    public void setQrCodeImage(byte[] qrCodeImage) {
        this.qrCodeImage = qrCodeImage;
    }

    public int getTable() {
        return table;
    }

    public void setTable(int table) {
        this.table = table;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }
}
