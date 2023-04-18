package comigo.conta.backend.oficial.domain.shared.object_utils.list_sorting;

import comigo.conta.backend.oficial.domain.shared.object_utils.ListaObj;

public interface Sort<T> {
    ListaObj<T> sort(ListaObj<T> lista);
}
