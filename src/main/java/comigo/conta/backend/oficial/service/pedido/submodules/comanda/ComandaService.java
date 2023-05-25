package comigo.conta.backend.oficial.service.pedido.submodules.comanda;

import comigo.conta.backend.oficial.domain.pedido.submodules.comanda.Comanda;
import comigo.conta.backend.oficial.domain.pedido.submodules.comanda.repository.ComandaRepository;
import comigo.conta.backend.oficial.domain.shared.Status;
import comigo.conta.backend.oficial.domain.shared.usecases.GenerateRandomIdUsecase;
import comigo.conta.backend.oficial.domain.shared.usecases.GetPriceFromComandaUsecase;
import comigo.conta.backend.oficial.service.pedido.PedidoService;
import comigo.conta.backend.oficial.service.pedido.submodules.comanda.dto.ComandaCriacaoDto;
import comigo.conta.backend.oficial.service.pedido.submodules.comanda.dto.ComandaUpdateDto;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ComandaService {
    private final ComandaRepository comandaRepository;
    private final PedidoService pedidoService;
    private final GetPriceFromComandaUsecase getPriceFromComandaUsecase;
    private final GenerateRandomIdUsecase generateRandomIdUsecase;

    public ComandaService(ComandaRepository comandaRepository, PedidoService pedidoService, GetPriceFromComandaUsecase getPriceFromComandaUsecase, GenerateRandomIdUsecase generateRandomIdUsecase) {
        this.comandaRepository = comandaRepository;
        this.pedidoService = pedidoService;
        this.getPriceFromComandaUsecase = getPriceFromComandaUsecase;
        this.generateRandomIdUsecase = generateRandomIdUsecase;
    }

    public Comanda criar(ComandaCriacaoDto comandaCriacaoDto, String idPedido) {
        if (pedidoService.naoExiste(idPedido)) {
            throw new ResponseStatusException(404, "Pedido não encontrado!", null);
        }
        final Comanda novaComanda = comandaCriacaoDto.toEntity(idPedido);

        final String id = generateRandomIdUsecase.execute();
        novaComanda.setId(id);

        return this.comandaRepository.save(novaComanda);
    }
    public Optional<Comanda> getById(String idComanda) {
        return comandaRepository.findById(idComanda);
    }

    public List<Comanda> getAll(String idPedido, Optional<Boolean> ativos) {
        if (pedidoService.naoExiste(idPedido)) {
            throw new ResponseStatusException(404, "Pedido não encontrado!", null);
        }
        if (ativos.isPresent()) {
            return this.comandaRepository.findAllByPedidoIdAndStatus(
                    idPedido,
                    ativos.get() ?
                            Status.ativo :
                            Status.finalizado
            );
        }
        return this.comandaRepository.findAllByPedidoId(idPedido);
    }

    public Comanda editar(String idComanda, ComandaUpdateDto comandaUpdateDto) {
        final Comanda comandaAtual = getComandaOrThrow404(idComanda);

        comandaAtual.setStatus(comandaUpdateDto.getStatus());
        comandaAtual.setNomeDono(comandaUpdateDto.getNomeDono());

        return comandaRepository.save(comandaAtual);
    }

    public Comanda finalizar(String idComanda) {
        final Comanda comandaAtual = getComandaOrThrow404(idComanda);

        comandaAtual.setStatus(Status.finalizado);

        return comandaRepository.save(comandaAtual);
    }

    public void updateComandaQRCodeId(String idComanda, Integer idQRCodePix) {
        final Comanda comanda = getComandaOrThrow404(idComanda);
        comanda.setIdQRCodePix(idQRCodePix);
        comandaRepository.save(comanda);
    }

    public void deletar(String idComanda) {
        if (!comandaRepository.existsById(idComanda)) {
            throw new ResponseStatusException(404, "Comanda não encontrada!", null);
        }
        comandaRepository.deleteById(idComanda);
    }

    private Comanda getComandaOrThrow404(String idComanda) {
        return this.comandaRepository
                .findById(idComanda)
                .orElseThrow(
                        () -> new ResponseStatusException(404, "Comanda não encontrada!", null)
                );
    }

    public boolean notExistsById(String idComanda) {
        return !comandaRepository.existsById(idComanda);
    }

    public double getPreco(String idComanda) {
        return getPriceFromComandaUsecase.execute(getComandaOrThrow404(idComanda));
    }

}
