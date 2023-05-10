package comigo.conta.backend.oficial.domain.shared.object_utils.fila;

public class Fila<T> {
    private final T[] fila;
    private int tamanho;

    public Fila(int size) {
        this.fila = (T[]) new Object[size];
        this.tamanho = 0;
    }

    public void insert(T info) {
        if (isFull()) {
            throw new IllegalStateException("Fila está cheia...");
        }
        fila[tamanho++] = info;
    }

    public T poll() {
        if (isEmpty()) {
            return null;
        }
        final T valorASerRetornado = fila[0];
        for (int i = 1; i < tamanho; i++) {
            fila[i - 1] = fila[i];
        }
        fila[--tamanho] = null;
        return valorASerRetornado;
    }

    public T peek() {
        return tamanho == 0 ? null : fila[0];
    }

    public boolean isFull() {
        return tamanho == fila.length;
    }

    public boolean isEmpty() {
        return tamanho == 0;
    }

    public void exibe() {
        if (isEmpty()) System.out.println("Nenhum valor na lista...");
        else for (int i = 0; i < tamanho; i++) System.out.println(fila[i]);
    }
}
