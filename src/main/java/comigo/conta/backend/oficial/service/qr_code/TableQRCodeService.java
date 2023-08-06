package comigo.conta.backend.oficial.service.qr_code;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import comigo.conta.backend.oficial.domain.qr_code.TableQRCode;
import comigo.conta.backend.oficial.domain.qr_code.repository.TableQRCodeRepository;
import comigo.conta.backend.oficial.domain.shared.constants.AppStrings;
import comigo.conta.backend.oficial.domain.shared.utils.ImageUtils;
import comigo.conta.backend.oficial.domain.usuario.Usuario;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Optional;

@Service
public class TableQRCodeService {
    private final TableQRCodeRepository repository;

    public TableQRCodeService(TableQRCodeRepository repository) {
        this.repository = repository;
    }

    public BufferedImage bindQRCodeWithRestaurantAndTable(
            String restauranteId,
            int mesa,
            Optional<Integer> width,
            Optional<Integer> height
    ) {
        if (repository.existsByUserIdAndTable(restauranteId, mesa)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    String.format("Restaurante já possui um QR Code para a mesa %d", mesa)
            );
        }
        final var user = new Usuario();
        user.setId(restauranteId);

        final var imageQrCode = generateQRCode(restauranteId, mesa, width, height);

        final var tableQrCode = new TableQRCode();

        tableQrCode.setTable(mesa);
        tableQrCode.setUser(user);
        tableQrCode.setQrCodeImage(convertImage(imageQrCode));

        repository.save(tableQrCode);

        return imageQrCode;
    }

    private BufferedImage generateQRCode(
            String restauranteId,
            int table,
            Optional<Integer> width,
            Optional<Integer> height
    ) {
        try {
            final var url = AppStrings.appUrl(restauranteId, table);

            final var qrCodeWriter = new QRCodeWriter();
            final var bitMatrix = qrCodeWriter.encode(
                    url,
                    BarcodeFormat.QR_CODE,
                    width.orElse(400),
                    height.orElse(400)
            );

            return MatrixToImageWriter.toBufferedImage(bitMatrix);
        } catch (WriterException e) {
            e.printStackTrace();
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Erro ao criar o qr code."
            );
        }
    }

    private byte[] convertImage(BufferedImage image) {
        try {
            return ImageUtils.bufferedImageToByteArray(image);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Erro ao criar o qr code."
            );
        }
    }
}
