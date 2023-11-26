package comigo.conta.backend.oficial.service.usuario;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import comigo.conta.backend.oficial.domain.shared.constants.AppStrings;
import comigo.conta.backend.oficial.domain.usuario.repository.UsuarioRepository;
import comigo.conta.backend.oficial.service.mobile.dtos.ItemGenerico;
import comigo.conta.backend.oficial.service.mobile.dtos.PedidoGenerico;
import comigo.conta.backend.oficial.service.usuario.dto.WebhookDto;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

import static java.net.http.HttpRequest.BodyPublishers;
import static java.net.http.HttpRequest.newBuilder;
import static java.net.http.HttpResponse.*;

@Service
public class IntegracaoMobileService {
    private final UsuarioRepository repository;
    private final UsuarioService usuarioService;

    public IntegracaoMobileService(UsuarioRepository repository, UsuarioService usuarioService) {
        this.repository = repository;
        this.usuarioService = usuarioService;
    }

    public WebhookDto bindWebhook(String restauranteId, Optional<String> optWebhook) {
        final var user = usuarioService.getUsuarioOrThrow404(restauranteId);
        final var webhook = optWebhook.orElseGet(AppStrings::defaultWebhookUrl);

        final var response = createHttpRequest(webhook);
        final var statusCode = response.statusCode();
        if (statusCode < 200 || statusCode >= 300) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Seu servidor responseu com um erro."
            );
        }

        user.setWebhookUrl(webhook);

        repository.save(user);

        return new WebhookDto(webhook);

    }

    public WebhookDto getWebhook(String restauranteId) {
        final var user = usuarioService.getUsuarioOrThrow404(restauranteId);
        final var webhookDto = new WebhookDto();
        webhookDto.setWebhookUrl(user.getWebhookUrl());
        return webhookDto;
    }


    public BufferedImage generateQRCodeToTable(
            String restauranteId,
            int mesa,
            Optional<Integer> largura,
            Optional<Integer> altura
    ) {
        try {
            usuarioService.getUsuarioOrThrow404(restauranteId);

            return generateQRCode(AppStrings.qrCodeData(restauranteId, mesa), largura, altura);
        } catch (WriterException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Erro ao gerar o QR Code.",
                    e
            );
        }

    }

    public PedidoGenerico getPedido(String restauranteId, int mesa) {
        final var restaurante = usuarioService.getUsuarioOrThrow404(restauranteId);
        final var webhook = restaurante.getWebhookUrl();

        if (Objects.isNull(webhook)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Usuario precisa de um webhook cadastrado para poder fazer as coisas funfarem"
            );
        }

        final var requestEndpoint = String.format("%s/mesas/%d", webhook, mesa);
        final var response = sendPedidosRequest(requestEndpoint);
        if (response.statusCode() != 200) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Não foi encontrado um pedido nessa mesa."
            );
        }

        try {
            final var jsonObject = new JSONObject(response.body());
            final var itensJson = jsonObject.getJSONArray("itens");
            final var itens = itensJson.toList()
                    .stream()
                    .map(o -> (HashMap<String, Object>) o)
                    .map(hashMap -> new ItemGenerico(
                                    (String) hashMap.get("nome"),
                                    ((Number) hashMap.get("preco")).doubleValue(),
                                    (Integer) hashMap.get("quantidade"),
                                    (String) hashMap.get("observacao")
                            )
                    )
                    .toList();
            return new PedidoGenerico(mesa, itens);

        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Erro ao parsear resultado"
            );
        }
    }

    private HttpResponse<String> createHttpRequest(String webhook) {
        final var httpClient = HttpClient.newHttpClient();
        final var request = newBuilder(URI.create(String.format("%s/pedidos", webhook)))
                .header("accept", "application/json")
                .POST(BodyPublishers.noBody())
                .build();

        try {
            return httpClient.send(request, BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Seu servidor responseu com um erro."
            );
        }
    }

    private HttpResponse<String> sendPedidosRequest(String webhook) {
        final var httpClient = HttpClient.newHttpClient();
        final var request = newBuilder(URI.create(webhook))
                .header("accept", "application/json")
                .GET()
                .build();

        try {
            return httpClient.send(request, BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Seu servidor responseu com um erro."
            );
        }
    }

    private BufferedImage generateQRCode(
            String data,
            Optional<Integer> largura,
            Optional<Integer> altura
    ) throws WriterException {
        final var qrCodeWriter = new QRCodeWriter();
        final var temp = new HashMap<EncodeHintType, Object>();
        temp.put(EncodeHintType.MARGIN, 2);
        temp.put(EncodeHintType.ERROR_CORRECTION, "Q");

        final var matrix = qrCodeWriter.encode(
                data,
                BarcodeFormat.QR_CODE,
                largura.orElse(600),
                altura.orElse(600),
                temp
        );

        return MatrixToImageWriter.toBufferedImage(matrix);
    }
}
