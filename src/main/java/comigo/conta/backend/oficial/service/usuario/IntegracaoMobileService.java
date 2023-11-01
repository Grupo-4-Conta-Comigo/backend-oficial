package comigo.conta.backend.oficial.service.usuario;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import comigo.conta.backend.oficial.domain.shared.constants.AppStrings;
import comigo.conta.backend.oficial.domain.usuario.repository.UsuarioRepository;
import comigo.conta.backend.oficial.service.usuario.dto.WebhookDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.Optional;

import static java.net.http.HttpRequest.BodyPublishers;
import static java.net.http.HttpRequest.newBuilder;

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
        if (response.statusCode() != 200) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Seu servidor responseu com um erro."
            );
        }

        user.setWebhookUrl(webhook);

        repository.save(user);

        return new WebhookDto(webhook);

    }


    public BufferedImage generateQRCodeToTable(
            String restauranteId,
            int mesa,
            Optional<Integer> largura,
            Optional<Integer> altura
    ) {
        try {
            final var user = usuarioService.getUsuarioOrThrow404(restauranteId);

            if (Objects.isNull(user.getWebhookUrl())) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Usuario precisa de um webhook cadastrado para poder gerar qr code"
                );
            }

            return generateQRCode(AppStrings.appUrl(restauranteId, mesa), largura, altura);
        } catch (WriterException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Erro ao gerar o QR Code.",
                    e
            );
        }

    }

    private HttpResponse<String> createHttpRequest(String webhook) {
        final var httpClient = HttpClient.newHttpClient();
        final var request = newBuilder(URI.create(webhook))
                .header("accept", "application/json")
                .POST(BodyPublishers.noBody())
                .build();

        try {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
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
        final var matrix = qrCodeWriter.encode(
                data,
                BarcodeFormat.QR_CODE,
                largura.orElse(400),
                altura.orElse(400)
        );

        return MatrixToImageWriter.toBufferedImage(matrix);
    }
}
