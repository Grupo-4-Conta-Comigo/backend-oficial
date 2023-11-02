package comigo.conta.backend.oficial.service.usuario.dto;

import comigo.conta.backend.oficial.domain.usuario.Cliente;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ClienteTokenDto {
    private String token;
    private Long id;
    private String nome;
    private String email;
    private String senha;

    private ClienteTokenDto() {

    }
    public ClienteTokenDto(String token, Cliente cliente) {
        this.token = token;
        this.id = cliente.getId();
        this.nome = cliente.getNome();
        this.email = cliente.getEmail();
        this.senha = cliente.getSenha();
    }
}
