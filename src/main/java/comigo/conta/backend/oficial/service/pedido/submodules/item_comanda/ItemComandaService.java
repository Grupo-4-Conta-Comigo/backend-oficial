package comigo.conta.backend.oficial.service.pedido.submodules.item_comanda;

import comigo.conta.backend.oficial.domain.pedido.Pedido;
import comigo.conta.backend.oficial.domain.pedido.submodules.comanda.Comanda;
import comigo.conta.backend.oficial.domain.pedido.submodules.item_comanda.ItemComanda;
import comigo.conta.backend.oficial.domain.pedido.submodules.item_comanda.repository.ItemComandaRepository;
import comigo.conta.backend.oficial.domain.produto.Categoria;
import comigo.conta.backend.oficial.domain.produto.Produto;
import comigo.conta.backend.oficial.domain.shared.usecases.GenerateRandomIdUsecase;
import comigo.conta.backend.oficial.service.pedido.PedidoService;
import comigo.conta.backend.oficial.service.pedido.submodules.comanda.ComandaService;
import comigo.conta.backend.oficial.service.pedido.submodules.item_comanda.dto.ItemComandaCriacaoDto;
import comigo.conta.backend.oficial.service.pedido.submodules.item_comanda.dto.ItemComandaDoPedido;
import comigo.conta.backend.oficial.service.produto.ProdutoService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ItemComandaService {
    final private ItemComandaRepository itemComandaRepository;
    final private ProdutoService produtoService;
    final private ComandaService comandaService;
    final private PedidoService pedidoService;
    final private GenerateRandomIdUsecase generateRandomIdUsecase;

    public ItemComandaService(ItemComandaRepository itemComandaRepository, ProdutoService produtoService, ComandaService comandaService, PedidoService pedidoService, GenerateRandomIdUsecase generateRandomIdUsecase) {
        this.itemComandaRepository = itemComandaRepository;
        this.produtoService = produtoService;
        this.comandaService = comandaService;
        this.pedidoService = pedidoService;
        this.generateRandomIdUsecase = generateRandomIdUsecase;
    }

    public ItemComanda criar(ItemComandaCriacaoDto itemComandaCriacaoDto) {
        if (!produtoService.existsById(itemComandaCriacaoDto.getIdProduto())) {
            throw new ResponseStatusException(404, "Produto não encontrado!", null);
        }
        if (comandaService.notExistsById(itemComandaCriacaoDto.getIdComanda())) {
            throw new ResponseStatusException(404, "Comanda não encontrada!", null);
        }

        final ItemComanda novoItemComanda = itemComandaCriacaoDto.toEntity();

        final String id = generateRandomIdUsecase.execute();
        novoItemComanda.setId(id);

        return itemComandaRepository.save(novoItemComanda);
    }

    public List<ItemComanda> getAll(
            String idComanda,
            Optional<Categoria> categoria
    ) {
        if (comandaService.notExistsById(idComanda)) {
            throw new ResponseStatusException(404, "Comanda não encontrada!", null);
        }
        return categoria.isPresent()
                ? itemComandaRepository.findByComanda_IdAndProduto_Categoria(idComanda, categoria.get())
                : itemComandaRepository.findAllByComandaId(idComanda);
    }

    public Optional<ItemComanda> getById(String idItemComanda) {
        return itemComandaRepository.findById(idItemComanda);
    }

    public List<ItemComandaDoPedido> getAllFromPedido(String idPedido) {
        Pedido pedido = pedidoService.getById(idPedido).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado..."));
        List<ItemComandaDoPedido> itensComandaDoPedido = new ArrayList<>();
        for (final var comanda : pedido.getComandas()) {
            for (final var itemComanda : comanda.getItensComanda()) {
                itensComandaDoPedido.add(new ItemComandaDoPedido(itemComanda.getId(), comanda.getId(), itemComanda.getObservacao(), comanda.getNomeDono(), itemComanda.getProduto()));
            }
        }
        return itensComandaDoPedido;
    }

    public ItemComanda editar(String idItemComanda, ItemComandaCriacaoDto itemComandaCriacaoDto) {
        final ItemComanda itemComandaAtual = getOrThrow404(idItemComanda);

        final var comanda = new Comanda();
        comanda.setId(itemComandaCriacaoDto.getIdComanda());

        final var produto = new Produto();
        produto.setId(itemComandaCriacaoDto.getIdProduto());

        itemComandaAtual.setComanda(comanda);
        itemComandaAtual.setProduto(produto);
        itemComandaAtual.setObservacao(itemComandaCriacaoDto.getObservacao());

        return itemComandaRepository.save(itemComandaAtual);
    }

    public void deletar(String idItemComanda) {
        if (!itemComandaRepository.existsById(idItemComanda)) {
            throw new ResponseStatusException(404, "Item da Comanda não encontrado!", null);
        }
        itemComandaRepository.deleteById(idItemComanda);
    }

    private ItemComanda getOrThrow404(String idItemComanda) {
        return itemComandaRepository
                .findById(idItemComanda)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                404,
                                "Item da Comanda não encontrado!",
                                null
                        )
                );
    }
}
