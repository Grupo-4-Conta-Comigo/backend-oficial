package comigo.conta.backend.oficial.domain.pedido;

import comigo.conta.backend.oficial.domain.pedido.submodules.comanda.Comanda;
import comigo.conta.backend.oficial.domain.shared.Status;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Pedido {
    @Id
    private String id;
    private int mesa;
    private Status status;
    @OneToMany(mappedBy = "pedido")
    private List<Comanda> comandas;
    private String idRestaurante;
    private LocalDateTime dataCriacao;

    public Pedido() {
        dataCriacao = LocalDateTime.now();
    }

    public Pedido(int mesa, Status status, String idRestaurante) {
        this.mesa = mesa;
        this.status = status;
        this.idRestaurante = idRestaurante;
    }

    public Pedido(String id, int mesa, Status status, String idRestaurante) {
        this.id = id;
        this.mesa = mesa;
        this.status = status;
        this.idRestaurante = idRestaurante;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMesa() {
        return mesa;
    }

    public void setMesa(int mesa) {
        this.mesa = mesa;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getIdRestaurante() {
        return idRestaurante;
    }

    public void setIdRestaurante(String idRestaurante) {
        this.idRestaurante = idRestaurante;
    }

    public List<Comanda> getComandas() {
        return comandas;
    }

    public void setComandas(List<Comanda> comandas) {
        this.comandas = comandas;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }
}
