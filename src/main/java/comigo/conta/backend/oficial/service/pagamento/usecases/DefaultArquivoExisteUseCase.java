package comigo.conta.backend.oficial.service.pagamento.usecases;

import comigo.conta.backend.oficial.service.shared.usecases.ArquivoExisteUseCase;

public class DefaultArquivoExisteUseCase implements ArquivoExisteUseCase {
    @Override
    public boolean execute(String path) {
        return false;
    }
}
