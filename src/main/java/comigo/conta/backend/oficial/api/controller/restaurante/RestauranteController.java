package comigo.conta.backend.oficial.api.controller.restaurante;

import comigo.conta.backend.oficial.service.autenticacao.dto.RestauranteLoginDto;
import comigo.conta.backend.oficial.service.autenticacao.dto.RestauranteTokenDto;
import comigo.conta.backend.oficial.service.restaurante.RestauranteService;
import comigo.conta.backend.oficial.service.restaurante.dto.RestauranteCriacaoDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/restaurantes")
@Tag(name = "Restaurantes", description = "Grupo de requisições de Restaurantes")
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
