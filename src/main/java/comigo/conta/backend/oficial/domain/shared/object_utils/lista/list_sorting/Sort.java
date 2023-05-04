package comigo.conta.backend.oficial.domain.shared.object_utils.lista.list_sorting;

import comigo.conta.backend.oficial.domain.shared.object_utils.lista.ListaObj;

public interface Sort<T> {
    ListaObj<T> sort(ListaObj<T> lista);
}
