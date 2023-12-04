package comigo.conta.backend.oficial.service.pagamento.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class CopiaColaDto {
    private String imagem;
    private String copiaCola;

    public static CopiaColaDto fromJson(Map<String, Object> json) {
        final String imageBase64 = ((String) json.get("imagemQrcode")).split(",")[1];
        final String copiaCola = (String) json.get("qrcode");
        return new CopiaColaDto(imageBase64, copiaCola);
    }
}
