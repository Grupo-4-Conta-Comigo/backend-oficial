package comigo.conta.backend.oficial.domain.shared.usecases;

import comigo.conta.backend.oficial.domain.pedido.submodules.comanda.Comanda;
import org.springframework.stereotype.Service;

@Service
public class GetPriceFromComandaUsecase {
    public double execute(final Comanda comanda) {
        return comanda.getItensComanda().stream().mapToDouble(itemComanda -> itemComanda.getProduto().getPreco()).sum();
    }
}
