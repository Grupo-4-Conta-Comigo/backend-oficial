package comigo.conta.backend.oficial.service.usuario.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ClienteLoginDto {
    @NotBlank
    private String email;
    @NotBlank
    private String senha;
}
