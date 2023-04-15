package school.sptech.exemplojwt.service.autenticacao.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import school.sptech.exemplojwt.domain.restaurante.Restaurante;

import java.util.Collection;

public class RestauranteDetalhesDto implements UserDetails {
    private final String nome;
    private final String cnpj;
    private final String cep;
    private final String email;
    private final String senha;

    public RestauranteDetalhesDto(Restaurante restaurante) {
        this.nome = restaurante.getNome();
        this.cnpj = restaurante.getCnpj();
        this.cep = restaurante.getCep();
        this.email = restaurante.getEmail();
        this.senha = restaurante.getSenha();
    }

    public String getNome() {
        return nome;
    }


    public String getCnpj() {
        return cnpj;
    }

    public String getCep() {
        return cep;
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
