package comigo.conta.backend.oficial.service.shared.usecases;

import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Service
public interface ArquivoExisteUseCase {
    boolean execute(Path path);
}
