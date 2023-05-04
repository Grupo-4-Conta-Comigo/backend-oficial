package comigo.conta.backend.oficial.service.usuario.dto;

import comigo.conta.backend.oficial.domain.usuario.Cargo;
import comigo.conta.backend.oficial.domain.usuario.Usuario;
import comigo.conta.backend.oficial.service.autenticacao.dto.UsuarioTokenDto;

public class UsuarioMapper {

    public static Usuario restauranteCriacaoDtoToUsuario(RestauranteCriacaoDto restauranteCriacaoDto) {
        final var restaurante = new Usuario();

        restaurante.setNome(restauranteCriacaoDto.getNome());
        restaurante.setRegistro(restauranteCriacaoDto.getCnpj());
        restaurante.setCargo(Cargo.admin);
        restaurante.setEmail(restauranteCriacaoDto.getEmail());
        restaurante.setSenha(restauranteCriacaoDto.getSenha());

        return restaurante;
    }

    public static UsuarioTokenDto UsuarioAutenticadoToUsuarioTokenDto(Usuario usuarioAutenticado, String token) {
        final var usuario = new UsuarioTokenDto();

        usuario.setUserId(usuarioAutenticado.getId());
        usuario.setNome(usuarioAutenticado.getNome());
        usuario.setRegistro(usuarioAutenticado.getRegistro());
        usuario.setCargo(usuarioAutenticado.getCargo());
        usuario.setEmail(usuarioAutenticado.getEmail());
        usuario.setToken(token);

        return usuario;
    }

    public static Usuario garcomCriacaoDtoToUsuario(GarcomCriacaoDto garcomCriacaoDto) {
        final Usuario usuario = new Usuario();

        usuario.setNome(garcomCriacaoDto.getNome());
        usuario.setRegistro(garcomCriacaoDto.getCpf());
        usuario.setCargo(Cargo.garcom);
        usuario.setEmail(garcomCriacaoDto.getEmail());
        usuario.setSenha(garcomCriacaoDto.getSenha());
        usuario.setRestauranteId(garcomCriacaoDto.getRestauranteId());

        return usuario;
    }
}
