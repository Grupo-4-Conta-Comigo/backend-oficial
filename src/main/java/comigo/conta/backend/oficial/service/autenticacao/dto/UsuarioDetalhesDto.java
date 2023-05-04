package comigo.conta.backend.oficial.service.autenticacao.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import comigo.conta.backend.oficial.domain.usuario.Usuario;

import java.util.Collection;

public class UsuarioDetalhesDto implements UserDetails {
    private final String nome;
    private final String registro;
    private final String email;
    private final String senha;

    public UsuarioDetalhesDto(Usuario usuario) {
        this.nome = usuario.getNome();
        this.registro = usuario.getRegistro();
        this.email = usuario.getEmail();
        this.senha = usuario.getSenha();
    }

    public String getNome() {
        return nome;
    }


    public String getRegistro() {
        return registro;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
