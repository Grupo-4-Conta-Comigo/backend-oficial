package comigo.conta.backend.oficial.domain.shared.constants;

public class AppStrings {
    public final static String NOME_ARQUIVO_CSV = "Restaurantes.csv";

    public static String appUrl(String id, int table) {
        return String.format("https://contacomigo.app/restaurante/%s/mesa/%d", id, table);
    }
}
