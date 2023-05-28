package comigo.conta.backend.oficial.api.controller.usuario;

import comigo.conta.backend.oficial.domain.shared.constants.AppStrings;
import comigo.conta.backend.oficial.domain.usuario.Usuario;
import comigo.conta.backend.oficial.service.autenticacao.dto.UsuarioLoginDto;
import comigo.conta.backend.oficial.service.autenticacao.dto.RestauranteMudarSenhaDto;
import comigo.conta.backend.oficial.service.autenticacao.dto.UsuarioTokenDto;
import comigo.conta.backend.oficial.service.usuario.UsuarioService;
import comigo.conta.backend.oficial.service.usuario.dto.GarcomCriacaoDto;
import comigo.conta.backend.oficial.service.usuario.dto.GarcomEdicaoDto;
import comigo.conta.backend.oficial.service.usuario.dto.RestauranteCriacaoDto;
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
import java.util.List;

@RestController
@RequestMapping("/restaurantes")
@Tag(name = "Restaurantes", description = "Grupo de requisições de Restaurantes")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
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
        usuarioService.criarRestaurante(restauranteCriacaoDto);
        gravaArquivoCsv(restauranteCriacaoDto);
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
    public ResponseEntity<UsuarioTokenDto> login(
            @RequestBody @Validated UsuarioLoginDto usuarioLoginDto
    ) {
        UsuarioTokenDto usuarioTokenDto = usuarioService.autenticar(usuarioLoginDto);

        return ResponseEntity.status(200).body(usuarioTokenDto);
    }

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200"
            ),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "401",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "403",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = @Content(schema = @Schema(hidden = true))
            )
    })
    @PostMapping("/garcons/criar")
    public ResponseEntity<Void> criarGarcom(
            @RequestBody @Validated GarcomCriacaoDto garcomCriacaoDto
    ) {
        usuarioService.criarGarcom(garcomCriacaoDto);
        return ResponseEntity.status(201).build();
    }

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200"
            ),
            @ApiResponse(
                    responseCode = "204",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "401",
                    content = @Content(schema = @Schema(hidden = true))
            ),@ApiResponse(
                    responseCode = "403",
                    content = @Content(schema = @Schema(hidden = true))
            ),@ApiResponse(
                    responseCode = "404",
                    content = @Content(schema = @Schema(hidden = true))
            ),
    })
    @GetMapping("/garcons/todos/{idRestaurante}")
    public ResponseEntity<List<Usuario>> getAll(@PathVariable String idRestaurante) {
        return listToResponseEntity(usuarioService.findGarconsByRestauranteId(idRestaurante));
    }
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200"
            ),
            @ApiResponse(
                    responseCode = "401",
                    content = @Content(schema = @Schema(hidden = true))
            ),@ApiResponse(
            responseCode = "403",
            content = @Content(schema = @Schema(hidden = true))
    ),@ApiResponse(
            responseCode = "404",
            content = @Content(schema = @Schema(hidden = true))
    ),
    })
    @GetMapping("/{idRestaurante}")
    public ResponseEntity<Usuario> getbyId(@PathVariable String idRestaurante) {
        return ResponseEntity.ok(usuarioService.getUsuarioOrThrow404(idRestaurante));
    }

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200"
            ),
            @ApiResponse(
                    responseCode = "401",
                    content = @Content(schema = @Schema(hidden = true))
            ),@ApiResponse(
                    responseCode = "403",
                    content = @Content(schema = @Schema(hidden = true))
            ),@ApiResponse(
                    responseCode = "404",
                    content = @Content(schema = @Schema(hidden = true))
            ),
    })
    @GetMapping("/garcons/{idGarcom}")
    public ResponseEntity<Usuario> getGarcom(@PathVariable String idGarcom) {
        return ResponseEntity.of(usuarioService.findGarcomById(idGarcom));
    }

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200"
            ),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "401",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "403",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = @Content(schema = @Schema(hidden = true))
            ),
    })
    @PutMapping("/garcons/editar/{idGarcom}")
    public ResponseEntity<Usuario> editarGarcom(
            @PathVariable String idGarcom,
            @RequestBody @Validated GarcomEdicaoDto garcomEdicaoDto
    ) {
        return ResponseEntity.ok(usuarioService.editarGarcom(garcomEdicaoDto, idGarcom));
    }

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200"
            ),
            @ApiResponse(
                    responseCode = "401",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "403",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = @Content(schema = @Schema(hidden = true))
            ),
    })
    @DeleteMapping("/garcons/{idGarcom}")
    public ResponseEntity<Void> deletarGarcom(@PathVariable String idGarcom) {
        usuarioService.deletarGarcom(idGarcom);
        return ResponseEntity.status(200).build();
    }

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200"
            ),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "401",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "403",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = @Content(schema = @Schema(hidden = true))
            ),
    })
    @PatchMapping("/mudar-senha")
    public ResponseEntity<UsuarioTokenDto> mudarSenha(
            @RequestBody @Validated RestauranteMudarSenhaDto restauranteMudarSenhaDto
    ) {
        final var restauranteLoginAntigoDto = new UsuarioLoginDto();
        restauranteLoginAntigoDto.setEmail(restauranteMudarSenhaDto.getEmail());
        restauranteLoginAntigoDto.setSenha(restauranteMudarSenhaDto.getSenhaAntiga());
        usuarioService.autenticar(restauranteLoginAntigoDto);

        usuarioService.mudarSenha(restauranteMudarSenhaDto);

        final var restauranteLoginNovoDto = new UsuarioLoginDto();
        restauranteLoginNovoDto.setEmail(restauranteMudarSenhaDto.getEmail());
        restauranteLoginNovoDto.setSenha(restauranteMudarSenhaDto.getSenhaNova());
        return login(restauranteLoginNovoDto);
    }

    private void gravaArquivoCsv(RestauranteCriacaoDto restaurante) {
        try (
                final FileWriter arq = new FileWriter(AppStrings.NOME_ARQUIVO_CSV, true);
                final Formatter saida = new Formatter(arq)
        ) {
            saida.format("%s;%s;%s\n", restaurante.getNome(), restaurante.getEmail(), restaurante.getCnpj());
        } catch (IOException erro) {
            System.out.println("Erro ao abrir o arquivo");
            erro.printStackTrace();
        }
    }

    private ResponseEntity<List<Usuario>> listToResponseEntity(List<Usuario> usuarios) {
        return usuarios.isEmpty() ? ResponseEntity.status(204).build() : ResponseEntity.ok(usuarios);
    }
}
