package comigo.conta.backend.oficial.service.pedido.submodules.item_comanda;

import comigo.conta.backend.oficial.domain.pedido.submodules.item_comanda.ItemComanda;
import comigo.conta.backend.oficial.domain.pedido.submodules.item_comanda.repository.ItemComandaRepository;
import comigo.conta.backend.oficial.domain.shared.usecases.GenerateRandomIdUsecase;
import comigo.conta.backend.oficial.service.pedido.submodules.comanda.ComandaService;
import comigo.conta.backend.oficial.service.pedido.submodules.item_comanda.dto.ItemComandaCriacaoDto;
import comigo.conta.backend.oficial.service.produto.ProdutoService;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ItemComandaService {
    final private ItemComandaRepository itemComandaRepository;
    final private ProdutoService produtoService;
    final private ComandaService comandaService;
    final private GenerateRandomIdUsecase generateRandomIdUsecase;

    public ItemComandaService(ItemComandaRepository itemComandaRepository, ProdutoService produtoService, ComandaService comandaService, GenerateRandomIdUsecase generateRandomIdUsecase) {
        this.itemComandaRepository = itemComandaRepository;
        this.produtoService = produtoService;
        this.comandaService = comandaService;
        this.generateRandomIdUsecase = generateRandomIdUsecase;
    }

    public ItemComanda criar(ItemComandaCriacaoDto itemComandaCriacaoDto) {
        if (!produtoService.existsById(itemComandaCriacaoDto.getIdProduto())) {
            throw new ResponseStatusException(404, "Produto não encontrado!", null);
        }
        if (!comandaService.existsById(itemComandaCriacaoDto.getIdComanda())) {
            throw new ResponseStatusException(404, "Comanda não encontrada!", null);
        }

        final ItemComanda novaComanda = itemComandaCriacaoDto.toEntity();

        final String id = generateRandomIdUsecase.execute();
        novaComanda.setId(id);

        return itemComandaRepository.save(novaComanda);
    }

    public List<ItemComanda> getAll(String idComanda) {
        if (!comandaService.existsById(idComanda)) {
            throw new ResponseStatusException(404, "Comanda não encontrada!", null);
        }
        return itemComandaRepository.findAllByComandaId(idComanda);
    }
}
