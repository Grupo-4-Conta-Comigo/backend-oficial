package school.sptech.exemplojwt.api.controller.restaurante;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import school.sptech.exemplojwt.service.autenticacao.dto.RestauranteLoginDto;
import school.sptech.exemplojwt.service.autenticacao.dto.RestauranteTokenDto;
import school.sptech.exemplojwt.service.restaurante.RestauranteService;
import school.sptech.exemplojwt.service.restaurante.dto.RestauranteCriacaoDto;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {
    private final RestauranteService restauranteService;

    public RestauranteController(RestauranteService restauranteService) {
        this.restauranteService = restauranteService;
    }

    @PostMapping("/criar")
    public ResponseEntity<Void> criar(@RequestBody @Validated RestauranteCriacaoDto restauranteCriacaoDto) {
        this.restauranteService.criar(restauranteCriacaoDto);
        return ResponseEntity.status(201).build();
    }

    @PostMapping("/login")
    public ResponseEntity<RestauranteTokenDto> login(@RequestBody @Validated RestauranteLoginDto restauranteLoginDto) {
        RestauranteTokenDto restauranteTokenDto = this.restauranteService.autenticar(restauranteLoginDto);

        return ResponseEntity.status(200).body(restauranteTokenDto);
    }
}
