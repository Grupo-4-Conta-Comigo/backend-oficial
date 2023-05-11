package comigo.conta.backend.oficial.service.shared.usecases.implementations;

import comigo.conta.backend.oficial.service.shared.usecases.ArquivoExisteUseCase;
import comigo.conta.backend.oficial.service.shared.usecases.CreateCertificatesFolderUseCase;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DefaultCreateCertificatesFolderUseCase implements CreateCertificatesFolderUseCase {
    private final ArquivoExisteUseCase arquivoExisteUseCase;

    public DefaultCreateCertificatesFolderUseCase(ArquivoExisteUseCase arquivoExisteUseCase) {
        this.arquivoExisteUseCase = arquivoExisteUseCase;
    }

    @Override
    public void execute(Path path) {
        try {
            if (arquivoExisteUseCase.execute(path)) {
                System.out.println("Folder de certificados já existe.");
                return;
            }
            Files.createDirectories(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
