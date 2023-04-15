package school.sptech.exemplojwt.domain.shared.object_utils.list_sorting;

import school.sptech.exemplojwt.domain.shared.object_utils.ListaObj;

public interface Sort<T> {
    ListaObj<T> sort(ListaObj<T> lista);
}
