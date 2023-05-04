package comigo.conta.backend.oficial.domain.shared.object_utils.lista.list_sorting.implementations;

import comigo.conta.backend.oficial.domain.pedido.Pedido;
import comigo.conta.backend.oficial.domain.shared.object_utils.lista.ListaObj;
import comigo.conta.backend.oficial.domain.shared.object_utils.lista.list_sorting.SortPedidosOldestToNewests;
import org.springframework.stereotype.Service;

@Service
public class SortPedidosOldestsToNewestsSelectionSortImpl implements SortPedidosOldestToNewests {
    @Override
    public ListaObj<Pedido> sort(ListaObj<Pedido> lista) {
        for (int i = 0; i < lista.size() - 1; i++) {
            int iMenor = i;
            for (int j = i; j < lista.size(); j++) {
                if (lista.get(j).getDataCriacao().isBefore(lista.get(iMenor).getDataCriacao())) {
                    iMenor = j;
                }
            }
            Pedido aux = lista.get(i);
            lista.update(i, lista.get(iMenor));
            lista.update(iMenor, aux);
        }
        return lista;
    }
}
