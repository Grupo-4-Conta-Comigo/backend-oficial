package comigo.conta.backend.oficial.domain.shared.object_utils;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class ListaObj<T> {
    private final T[] vetor;
    private int size;

    public ListaObj(int size) {
        this.size = 0;
        this.vetor = (T[]) new Object[size];
    }

    public boolean add(T value) {
        if (isVetorCheio()) return false;
        vetor[size++] = value;
        return true;
    }

    public boolean add(T[] list) {
        if (size + list.length > vetor.length) {
            return false;
        }
        for (T t : list) {
            add(t);
        }
        return true;
    }

    public boolean add(ListaObj<T> list) {
        return add(list.vetor);
    }

    public boolean add(List<T> list) {
        return add((T[]) list.toArray());
    }

    public Optional<T> get(int index) {
        if (isIndiceInvalido(index)) {
            return Optional.empty();
        }
        return Optional.of(vetor[index]);
    }

    public Optional<Integer> getIndex(T value) {
        for (int i = 0; i < size; i++) {
            if (vetor[i].equals(value)) {
                return Optional.of(i);
            }
        }
        return Optional.empty();
    }

    public boolean removeByIndex(int index) {
        if (isIndiceInvalido(index)) return false;
        for (int i = index; i < size - 1; i++) {
            vetor[i] = vetor[i + 1];
        }
        vetor[--size] = null;
        return true;
    }

    public boolean remove(T value) {
        return this.getIndex(value).map(this::removeByIndex).orElse(false);
    }

    public int clear() {
        int amountOfItems = size;
        for (int i = 0; i < size; i++) {
            vetor[i] = null;
        }
        size = 0;
        return amountOfItems;
    }

    public boolean update(int index, T value) {
        if (isIndiceInvalido(index)) {
            return false;
        }
        vetor[index] = value;
        return true;
    }

    public boolean update(T oldValue, T newValue) {
        for (int i = 0; i < size; i++) {
            if (vetor[i].equals(oldValue)) {
                vetor[i] = newValue;
                return true;
            }
        }
        return false;
    }

    public long amount(T value) {
        return IntStream.range(0, size).filter(index -> vetor[index].equals(value)).count();
    }

    public boolean addAtStart(T value) {
        if (isVetorCheio()) {
            return false;
        }
        for (int i = size - 1; i >= 0; i--) {
            vetor[i + 1] = vetor[i];
        }
        size++;
        vetor[0] = value;
        return true;
    }

    public int size() {
        return this.size;
    }

    private boolean isIndiceInvalido(int index) {
        return index < 0 || index >= size;
    }

    private boolean isVetorCheio() {
        return size >= vetor.length;
    }
}
