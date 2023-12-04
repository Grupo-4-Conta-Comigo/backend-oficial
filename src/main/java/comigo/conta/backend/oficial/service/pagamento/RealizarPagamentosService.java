package comigo.conta.backend.oficial.service.pagamento;

import br.com.gerencianet.gnsdk.Gerencianet;
import br.com.gerencianet.gnsdk.exceptions.GerencianetException;
import com.mifmif.common.regex.Generex;
import comigo.conta.backend.oficial.domain.pagamento.DetalhesPagamento;
import comigo.conta.backend.oficial.domain.pagamento.Pagamento;
import comigo.conta.backend.oficial.domain.pagamento.repository.PagamentoRepository;
import comigo.conta.backend.oficial.domain.pedido.submodules.comanda.Comanda;
import comigo.conta.backend.oficial.domain.shared.usecases.GenerateRandomIdUsecase;
import comigo.conta.backend.oficial.service.pagamento.dto.CobrancaDetailsDto;
import comigo.conta.backend.oficial.service.pagamento.dto.CopiaColaDto;
import comigo.conta.backend.oficial.service.pagamento.dto.CriarPagamentoDto;
import comigo.conta.backend.oficial.service.pagamento.usecases.GetCobrancaDetailsUseCase;
import comigo.conta.backend.oficial.service.pedido.submodules.comanda.ComandaService;
import comigo.conta.backend.oficial.service.usuario.UsuarioService;
import comigo.conta.backend.oficial.service.usuario.dto.UsuarioNomeDocumentoDto;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayInputStream;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;

import static javax.xml.bind.DatatypeConverter.*;

@Service
public class RealizarPagamentosService {
    private final PagamentoRepository repository;
    private final CertificadoPagamentoService certificadoPagamentoService;
    private final GerenciaDetalhesPagamentoService gerenciaDetalhesPagamentoService;
    private final UsuarioService usuarioService;
    private final ComandaService comandaService;
    private final GenerateRandomIdUsecase generateRandomIdUsecase;

    public RealizarPagamentosService(PagamentoRepository repository, CertificadoPagamentoService certificadoPagamentoService, GerenciaDetalhesPagamentoService gerenciaDetalhesPagamentoService, UsuarioService usuarioService, ComandaService comandaService, GenerateRandomIdUsecase generateRandomIdUsecase) {
        this.repository = repository;
        this.certificadoPagamentoService = certificadoPagamentoService;
        this.gerenciaDetalhesPagamentoService = gerenciaDetalhesPagamentoService;
        this.usuarioService = usuarioService;
        this.comandaService = comandaService;
        this.generateRandomIdUsecase = generateRandomIdUsecase;
    }

    public boolean realizarPagamentoParaTeste(String idRestaurante) {
        DetalhesPagamento detalhesPagamento = getDetalhesPagamentoOrThrow404(idRestaurante);
        criarArquivoSeNaoExiste(idRestaurante, detalhesPagamento);
        UsuarioNomeDocumentoDto usuario = getUsuarioNomeDocumentoOrThrow404(idRestaurante);
        return Objects.nonNull(
                realizarPagamento(
                        detalhesPagamento,
                        idRestaurante,
                        usuario.getNome(),
                        usuario.getDocumento(),
                        1
                )
        );
    }

    public Comanda criarCobranca(String idRestaurante, String idComanda, double valor) {
        DetalhesPagamento detalhesPagamento = getDetalhesPagamentoOrThrow404(idRestaurante);
        criarArquivoSeNaoExiste(idRestaurante, detalhesPagamento);
        UsuarioNomeDocumentoDto usuario = getUsuarioNomeDocumentoOrThrow404(idRestaurante);
        Integer idPagamento = realizarPagamento(
                detalhesPagamento,
                idRestaurante,
                usuario.getNome(),
                usuario.getDocumento(),
                valor
        );
        return comandaService.updateComandaQRCodeId(idComanda, idPagamento);
    }

    public byte[] getQRCode(String idComanda, String idRestaurante) {
        DetalhesPagamento detalhesPagamento = getDetalhesPagamentoOrThrow404(idRestaurante);
        criarArquivoSeNaoExiste(idRestaurante, detalhesPagamento);
        Comanda comanda = comandaService.getComandaOrThrow404(idComanda);
        return criarQRCode(
                comanda.getIdQRCodePix().get(comanda.getIdQRCodePix().size() - 1),
                idRestaurante,
                detalhesPagamento
        );
    }

    public List<CobrancaDetailsDto> getCobrancaDetails(
            String idRestaurante,
            String idComanda
    ) {
        DetalhesPagamento detalhesPagamento = getDetalhesPagamentoOrThrow404(idRestaurante);
        criarArquivoSeNaoExiste(idRestaurante, detalhesPagamento);
        Comanda comanda = comandaService.getComandaOrThrow404(idComanda);
        ExecutorService executor = Executors.newFixedThreadPool(comanda.getIdQRCodePix().size());
        List<GetCobrancaDetailsUseCase> usecases =
                comanda
                        .getIdQRCodePix()
                        .stream()
                        .map(
                                idCobranca -> new GetCobrancaDetailsUseCase(idCobranca, detalhesPagamento, idRestaurante)
                        ).toList();
        List<Future<CobrancaDetailsDto>> results;
        try {
            results = executor.invokeAll(usecases);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao conseguir os detalhes de cobrança");
        }
        executor.shutdown();
        return results.stream().map(cobrancaDetailsDtoFuture -> {
            try {
                return cobrancaDetailsDtoFuture.get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao conseguir os detalhes de cobrança");
            }
        }).toList();
    }

    private Integer realizarPagamento(
            DetalhesPagamento detalhesPagamento,
            String idRestaurante,
            String nome,
            String cpf,
            double valor
    ) {
        HashMap<String, Object> options = configureOptions(detalhesPagamento, idRestaurante);

        HashMap<String, String> params = new HashMap<>();
        params.put("txid", new Generex("[a-zA-Z0-9]{26,35}").random());
        String valorFormatado = new DecimalFormat("#.00").format(valor).replace(",", ".");
        System.out.println(valorFormatado);

        Map<String, Object> body = new HashMap<>();
        body.put("calendario", new JSONObject().put("dataDeVencimento", LocalDate.now().plusDays(3)).put("validadeAposVencimento", 30));
        body.put("devedor", new JSONObject().put("cpf", "38237290801").put("nome", nome));
        body.put("valor", new JSONObject().put("original", valorFormatado));
        body.put("chave", detalhesPagamento.getChavePix());
        try {
            Gerencianet gn = new Gerencianet(options);
            Map<String, Object> response = gn.call("pixCreateDueCharge", params, body);
            System.out.println(response);
            @SuppressWarnings("unchecked")
            Map<String, Object> loc = (Map<String, Object>) response.get("loc");
            return (int) loc.get("id");
        } catch (GerencianetException e) {
            System.out.println(e.getError());
            System.out.println(e.getErrorDescription());
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.valueOf(e.getCode()), e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Aconteceu um erro ao realizar o pagamento");
        }
    }

    private HashMap<String, Object> configureOptions(DetalhesPagamento detalhesPagamento, String idRestaurante) {
        HashMap<String, Object> options = new HashMap<>();
        options.put("client_id", detalhesPagamento.getClientId());
        options.put("client_secret", detalhesPagamento.getClientSecret());
        options.put("certificate", certificadoPagamentoService.getRealCertificadoPath(idRestaurante, detalhesPagamento.getNomeCertificado()));
        options.put("sandbox", true);
        return options;
    }

    private byte[] criarQRCode(
            Integer idCobranca,
            String idRestaurante,
            DetalhesPagamento detalhesPagamento
    ) {
        HashMap<String, Object> options = configureOptions(detalhesPagamento, idRestaurante);

        HashMap<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(idCobranca));

        try {
            Gerencianet gn = new Gerencianet(options);
            Map<String, Object> response = gn.call("pixGenerateQRCode", params, new HashMap<>());
            return new ByteArrayInputStream(parseBase64Binary(((String) response.get("imagemQrcode")).split(",")[1])).readAllBytes();
        } catch (GerencianetException e) {
            System.out.println(e.getError());
            System.out.println(e.getErrorDescription());
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.valueOf(e.getCode()), e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Aconteceu um erro ao realizar o pagamento");
        }

    }

    private UsuarioNomeDocumentoDto getUsuarioNomeDocumentoOrThrow404(String idRestaurante) {
        return usuarioService
                .findNomeDocumentoById(idRestaurante)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado...")
                );
    }

    private void criarArquivoSeNaoExiste(String idRestaurante, DetalhesPagamento detalhesPagamento) {
        if (!certificadoPagamentoService.certificadoExisteLocalmente(idRestaurante, detalhesPagamento.getNomeCertificado())) {
            certificadoPagamentoService.criarCertificadoLocalmente(
                    detalhesPagamento.getCertificado(),
                    idRestaurante,
                    detalhesPagamento.getNomeCertificado()
            );
        }
    }

    private DetalhesPagamento getDetalhesPagamentoOrThrow404(String idRestaurante) {
        return gerenciaDetalhesPagamentoService
                .getDetalhesPagamento(idRestaurante)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Detalhes de Pagamento não encontrados")
                );
    }

    public Pagamento cadastrarPagamento(CriarPagamentoDto criarPagamentoDto) {
        if (usuarioService.naoExiste(criarPagamentoDto.getIdRestaurante())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurante não encontrado...");
        }
        Pagamento pagamento = new Pagamento();
        pagamento.setIdRestaurante(criarPagamentoDto.getIdRestaurante());
        pagamento.setNumeroMesa(criarPagamentoDto.getNumeroMesa());
        pagamento.setNomePagante(criarPagamentoDto.getNomePagante());
        pagamento.setChavePix(criarPagamentoDto.getChavePix());
        pagamento.setValorPagamento(criarPagamentoDto.getValorPagamento());
        pagamento.setPagamentoConcluido(criarPagamentoDto.isPagamentoConcluido());
        pagamento.setFoiUsadoPix(criarPagamentoDto.isPix());
        pagamento.setDataHoraPagamento(LocalDateTime.now());
        pagamento.setId(generateRandomIdUsecase.execute());

        return repository.save(pagamento);
    }

    public List<Pagamento> getTodosPagamentos(
            String idRestaurante
    ) {
        if (usuarioService.naoExiste(idRestaurante)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurante não encontrado");
        }
        return repository.findTop2ByIdRestauranteOrderByDataHoraPagamentoDesc(idRestaurante);
    }

    public CopiaColaDto getCopiaCola(String idRestaurante, double valor) {
        final var config = configureOptions(getDetalhesPagamentoOrThrow404(idRestaurante), idRestaurante);
        final var cobrancaId = realizarPagamento(
                getDetalhesPagamentoOrThrow404(idRestaurante),
                idRestaurante,
                "Rafael Reis",
                "",
                valor
        );
        return requisicaoCopiaCola(cobrancaId, config);
    }

    private CopiaColaDto requisicaoCopiaCola(Integer cobrancaId, HashMap<String, Object> options) {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(cobrancaId));

        try {
            Gerencianet gn = new Gerencianet(options);
            Map<String, Object> response = gn.call("pixGenerateQRCode", params, new HashMap<>());
            System.out.println(response);
            return CopiaColaDto.fromJson(response);
        } catch (GerencianetException e) {
            System.out.println(e.getError());
            System.out.println(e.getErrorDescription());
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.valueOf(e.getCode()), e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Aconteceu um erro ao realizar o pagamento");
        }
    }
}
