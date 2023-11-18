package comigo.conta.backend.oficial.domain.shared.constants;

public final class AppStrings {
    public final static String NOME_ARQUIVO_CSV = "Restaurantes.csv";

    public static String qrCodeData(String id, int table) {
        return String.format("%s/%d", id, table);
    }

    public static String defaultWebhookUrl() {
        return String.format("%s/webhook", defaultContaComigoUrl());
    }

    public static String defaultContaComigoUrl() {
        return "http://localhost:8080";
    }
}
