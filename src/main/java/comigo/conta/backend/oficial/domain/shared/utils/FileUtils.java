package comigo.conta.backend.oficial.domain.shared.utils;

import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {
    public static final Path ABSOLUTE_PROJECT_PATH = Paths.get("").toAbsolutePath();
    public static final Path PAYMENT_DETAILS_CERTIFICATES_PATH = Paths.get(ABSOLUTE_PROJECT_PATH + "/certs");
}
