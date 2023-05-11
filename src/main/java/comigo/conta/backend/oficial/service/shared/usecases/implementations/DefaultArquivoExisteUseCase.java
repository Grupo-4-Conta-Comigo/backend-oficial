package comigo.conta.backend.oficial.service.shared.usecases.implementations;

import comigo.conta.backend.oficial.service.shared.usecases.ArquivoExisteUseCase;

import java.nio.file.Files;
import java.nio.file.Path;

public class DefaultArquivoExisteUseCase implements ArquivoExisteUseCase {
    @Override
    public boolean execute(Path path) {
        return Files.exists(path);
    }
}
