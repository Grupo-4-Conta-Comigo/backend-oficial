package comigo.conta.backend.oficial.domain.shared.usecases;

import comigo.conta.backend.oficial.domain.pedido.Pedido;
import org.springframework.stereotype.Service;

@Service
public class GetPriceFromPedidoUsecase {
    private final GetPriceFromComandaUsecase getPriceFromComandaUsecase;

    public GetPriceFromPedidoUsecase(GetPriceFromComandaUsecase getPriceFromComandaUsecase) {
        this.getPriceFromComandaUsecase = getPriceFromComandaUsecase;
    }

    public double execute(final Pedido pedido) {
        return pedido.getComandas().stream().mapToDouble(this.getPriceFromComandaUsecase::execute).sum();
    }
}
