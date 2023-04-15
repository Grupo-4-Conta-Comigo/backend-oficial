package school.sptech.exemplojwt.service.restaurante.dto;

import school.sptech.exemplojwt.domain.restaurante.Restaurante;
import school.sptech.exemplojwt.service.autenticacao.dto.RestauranteTokenDto;

public class RestauranteMapper {

    public static Restaurante of(RestauranteCriacaoDto restauranteCriacaoDto) {
        final var restaurante = new Restaurante();

        restaurante.setNome(restauranteCriacaoDto.getNome());
        restaurante.setCnpj(restauranteCriacaoDto.getCnpj());
        restaurante.setCep(restauranteCriacaoDto.getCep());
        restaurante.setEmail(restauranteCriacaoDto.getEmail());
        restaurante.setSenha(restauranteCriacaoDto.getSenha());

        return restaurante;
    }

    public static RestauranteTokenDto of(Restaurante restauranteAutenticado, String token) {
        final var restaurante = new RestauranteTokenDto();

        restaurante.setUserId(restauranteAutenticado.getId());
        restaurante.setNome(restauranteAutenticado.getNome());
        restaurante.setCnpj(restauranteAutenticado.getCnpj());
        restaurante.setCep(restauranteAutenticado.getCep());
        restaurante.setEmail(restauranteAutenticado.getEmail());
        restaurante.setToken(token);

        return restaurante;
    }
}
