package comigo.conta.backend.oficial.api.controller.restaurante;

import comigo.conta.backend.oficial.service.autenticacao.dto.RestauranteLoginDto;
import comigo.conta.backend.oficial.service.autenticacao.dto.RestauranteMudarSenhaDto;
import comigo.conta.backend.oficial.service.autenticacao.dto.RestauranteTokenDto;
import comigo.conta.backend.oficial.service.restaurante.RestauranteService;
import comigo.conta.backend.oficial.service.restaurante.dto.RestauranteCriacaoDto;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Formatter;
import java.util.FormatterClosedException;

@RestController
@RequestMapping("/restaurantes")
@Tag(name = "Restaurantes", description = "Grupo de requisições de Restaurantes")
public class RestauranteController {

    private final RestauranteService restauranteService;

    public RestauranteController(RestauranteService restauranteService) {
        this.restauranteService = restauranteService;
    }

    public static void gravaArquivoCsv(RestauranteCriacaoDto restaurante, String nomeArq) {
        FileWriter arq = null;
        Formatter saida = null;
        boolean deuRuim = false;

        nomeArq += ".csv";

        // Bloco try catch para abrir o arquivo
        try {
            arq = new FileWriter(nomeArq, true);
            saida = new Formatter(arq);
        } catch (IOException erro) {
            System.out.println("Erro ao abrir o arquivo");
            System.exit(1);
        }

        // Bloco try catch para gravar no arquivo

        try {
            saida.format("%s;%s;%s\n", restaurante.getNome(),
                    restaurante.getEmail(),
                    restaurante.getCnpj());
        } catch (FormatterClosedException erro) {
            System.out.println("Erro ao gravar o arquivo");
            deuRuim = true;
        } finally {
            saida.close();
            try {
                arq.close();
            } catch (IOException erro) {
                System.out.println("Erro ao fechar o arquivo");
                deuRuim = true;
            }

            if (deuRuim) {
                System.exit(1);
            }
        }
    }

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200"
            ),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(schema = @Schema(hidden = true))
            )
    })

    @CrossOrigin
    @PostMapping("/criar")
    public ResponseEntity<Void> criar(
            @RequestBody @Validated RestauranteCriacaoDto restauranteCriacaoDto
    ) {
        restauranteService.criar(restauranteCriacaoDto);
        gravaArquivoCsv(restauranteCriacaoDto, "Restaurantes");
        return ResponseEntity.status(201).build();
    }

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200"
            ),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(schema = @Schema(hidden = true))
            )
    })
    @PostMapping("/login")
    public ResponseEntity<RestauranteTokenDto> login(
            @RequestBody @Validated RestauranteLoginDto restauranteLoginDto
    ) {
        RestauranteTokenDto restauranteTokenDto = restauranteService.autenticar(restauranteLoginDto);

        return ResponseEntity.status(200).body(restauranteTokenDto);
    }

    @PatchMapping("/mudar-senha")
    public ResponseEntity<RestauranteTokenDto> mudarSenha(
            @RequestBody @Validated RestauranteMudarSenhaDto restauranteMudarSenhaDto
    ) {
        final var restauranteLoginAntigoDto = new RestauranteLoginDto();
        restauranteLoginAntigoDto.setEmail(restauranteMudarSenhaDto.getEmail());
        restauranteLoginAntigoDto.setSenha(restauranteMudarSenhaDto.getSenhaAntiga());
        restauranteService.autenticar(restauranteLoginAntigoDto);

        restauranteService.mudarSenha(restauranteMudarSenhaDto);

        final var restauranteLoginNovoDto = new RestauranteLoginDto();
        restauranteLoginNovoDto.setEmail(restauranteMudarSenhaDto.getEmail());
        restauranteLoginNovoDto.setSenha(restauranteMudarSenhaDto.getSenhaNova());
        return login(restauranteLoginNovoDto);
    }
}
