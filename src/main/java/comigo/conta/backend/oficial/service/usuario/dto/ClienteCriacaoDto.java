package comigo.conta.backend.oficial.service.usuario.dto;

import comigo.conta.backend.oficial.domain.usuario.Cliente;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class ClienteCriacaoDto {
    @NotBlank
    private String nome;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    @Size(min = 8)
    private String senha;

    public Cliente toCliente() {
        final var cliente = new Cliente();
        cliente.setNome(nome);
        cliente.setEmail(email);
        cliente.setSenha(senha);
        return cliente;
    }
}
