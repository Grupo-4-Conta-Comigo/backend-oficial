package comigo.conta.backend.oficial.api.controller.pedido;

import comigo.conta.backend.oficial.domain.pedido.submodules.item_comanda.ItemComanda;
import comigo.conta.backend.oficial.service.pedido.submodules.item_comanda.ItemComandaService;
import comigo.conta.backend.oficial.service.pedido.submodules.item_comanda.dto.ItemComandaCriacaoDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/itens-comanda")
public class ItemComandaController {
    private final ItemComandaService itemComandaService;

    public ItemComandaController(ItemComandaService itemComandaService) {
        this.itemComandaService = itemComandaService;
    }

    @PostMapping("/criar/")
    public ResponseEntity<ItemComanda> postItemComanda(
            @RequestBody ItemComandaCriacaoDto itemComandaCriacaoDto
    ) {
        return ResponseEntity.status(201).body(itemComandaService.criar(itemComandaCriacaoDto));
    }

    @GetMapping("/{idComanda}")
    public ResponseEntity<List<ItemComanda>> getAll(@PathVariable String idComanda) {
        final List<ItemComanda> itens = itemComandaService.getAll(idComanda);
        return itens.isEmpty() ? ResponseEntity.status(204).build() : ResponseEntity.ok(itens);
    }
}
