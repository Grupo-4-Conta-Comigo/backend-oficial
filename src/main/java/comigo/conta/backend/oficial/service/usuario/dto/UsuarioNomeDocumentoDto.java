package comigo.conta.backend.oficial.service.usuario.dto;

public class UsuarioNomeDocumentoDto {
    private String nome;
    private String documento;

    public UsuarioNomeDocumentoDto(String nome, String documento) {
        this.nome = nome;
        this.documento = documento;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }
}
