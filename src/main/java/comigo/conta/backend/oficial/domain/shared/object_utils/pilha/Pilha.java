package comigo.conta.backend.oficial.domain.shared.object_utils.pilha;

public class Pilha<T> {
    private final T[] vetor;
    private int topo;

    public Pilha(int tamanho) {
        vetor = (T[]) new Object[tamanho];
        topo = -1;
    }

    public boolean isEmpty() {
        return topo == -1;
    }

    public boolean isFull() {
        return topo == vetor.length - 1;
    }

    public void push(T valor) {
        if (isFull()) {
            throw new IllegalStateException("Tamanho máximo do Stack já foi atingido");
        }
        vetor[++topo] = valor;
    }

    public T pop() {
        if (isEmpty()) {
           return null;
        }
        return vetor[topo--];
    }

    public T peek() {
        return isEmpty() ? null : vetor[topo];
    }

    public void print() {
        if (isEmpty()) {
            System.out.println("Pilha vazia...");
            return;
        }
        for (int i = topo; i >= 0; i--) {
            System.out.println(String.valueOf(vetor[i]).concat("\n"));
        }
    }
}
