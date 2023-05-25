package comigo.conta.backend.oficial.service.pagamento;

import br.com.gerencianet.gnsdk.Gerencianet;
import br.com.gerencianet.gnsdk.exceptions.GerencianetException;
import com.mifmif.common.regex.Generex;
import comigo.conta.backend.oficial.domain.pagamento.DetalhesPagamento;
import comigo.conta.backend.oficial.domain.pedido.submodules.comanda.Comanda;
import comigo.conta.backend.oficial.service.pagamento.dto.CobrancaDetailsDto;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class RealizarPagamentosService {
    private final CertificadoPagamentoService certificadoPagamentoService;
    private final GerenciaDetalhesPagamentoService gerenciaDetalhesPagamentoService;
    private final UsuarioService usuarioService;
    private final ComandaService comandaService;

    public RealizarPagamentosService(CertificadoPagamentoService certificadoPagamentoService, GerenciaDetalhesPagamentoService gerenciaDetalhesPagamentoService, UsuarioService usuarioService, ComandaService comandaService) {
        this.certificadoPagamentoService = certificadoPagamentoService;
        this.gerenciaDetalhesPagamentoService = gerenciaDetalhesPagamentoService;
        this.usuarioService = usuarioService;
        this.comandaService = comandaService;
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

    public Comanda criarPagamento(String idRestaurante, String idComanda, double valor) {
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
                comanda.getIdQRCodePix(),
                idRestaurante,
                detalhesPagamento
        );
    }

    public CobrancaDetailsDto getCobrancaDetails(
            String idRestaurante,
            String idComanda
    ) {
        DetalhesPagamento detalhesPagamento = getDetalhesPagamentoOrThrow404(idRestaurante);
        criarArquivoSeNaoExiste(idRestaurante, detalhesPagamento);
        Comanda comanda = comandaService.getComandaOrThrow404(idComanda);
        String txid = getCobrancaTXID(comanda.getIdQRCodePix(), detalhesPagamento, idRestaurante);
        Map<String, Object> result = getDetalhesDeCobranca(detalhesPagamento, idRestaurante, txid);
        return new CobrancaDetailsDto(result);
    }

    private Integer realizarPagamento(
            DetalhesPagamento detalhesPagamento,
            String idRestaurante,
            String nome,
            String cpf,
            double valor
    ) {
        HashMap<String, Object> options = new HashMap<>();
        options.put("client_id", detalhesPagamento.getClientId());
        options.put("client_secret", detalhesPagamento.getClientSecret());
        options.put("certificate", certificadoPagamentoService.getRealCertificadoPath(idRestaurante, detalhesPagamento.getNomeCertificado()));
        options.put("sandbox", false);

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

    private byte[] criarQRCode(
            Integer idCobranca,
            String idRestaurante,
            DetalhesPagamento detalhesPagamento
    ) {
        HashMap<String, Object> options = new HashMap<>();
        options.put("client_id", detalhesPagamento.getClientId());
        options.put("client_secret", detalhesPagamento.getClientSecret());
        options.put("certificate", certificadoPagamentoService.getRealCertificadoPath(idRestaurante, detalhesPagamento.getNomeCertificado()));
        options.put("sandbox", false);

        HashMap<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(idCobranca));

        try {
            Gerencianet gn = new Gerencianet(options);
            Map<String, Object> response = gn.call("pixGenerateQRCode", params, new HashMap<>());
            return new ByteArrayInputStream(javax.xml.bind.DatatypeConverter.parseBase64Binary(((String) response.get("imagemQrcode")).split(",")[1])).readAllBytes();
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

    private String getCobrancaTXID(
            Integer idCobranca,
            DetalhesPagamento detalhesPagamento,
            String idRestaurante
    ) {
        HashMap<String, Object> options = new HashMap<>();
        options.put("client_id", detalhesPagamento.getClientId());
        options.put("client_secret", detalhesPagamento.getClientSecret());
        options.put("certificate", certificadoPagamentoService.getRealCertificadoPath(idRestaurante, detalhesPagamento.getNomeCertificado()));
        options.put("sandbox", false);

        HashMap<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(idCobranca));

        try {
            Gerencianet gn = new Gerencianet(options);
            Map<String, Object> response = gn.call("pixDetailLocation", params, new HashMap<>());
            System.out.println(response);
            return response.get("txid").toString();
        } catch (GerencianetException e) {
            System.out.println(e.getError());
            System.out.println(e.getErrorDescription());
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.valueOf(e.getCode()), e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Aconteceu um erro ao conseguir o txid da cobrança");
        }
    }

    private Map<String, Object> getDetalhesDeCobranca(
            DetalhesPagamento detalhesPagamento,
            String idRestaurante,
            String txid
    ) {
        HashMap<String, Object> options = new HashMap<>();
        options.put("client_id", detalhesPagamento.getClientId());
        options.put("client_secret", detalhesPagamento.getClientSecret());
        options.put("certificate", certificadoPagamentoService.getRealCertificadoPath(idRestaurante, detalhesPagamento.getNomeCertificado()));
        options.put("sandbox", false);

        HashMap<String, String> params = new HashMap<>();
        params.put("txid", txid);

        try {
            Gerencianet gn = new Gerencianet(options);
            Map<String, Object> response = gn.call("pixDetailDueCharge", params, new HashMap<>());
            System.out.println(response);
            return response;
        } catch (GerencianetException e) {
            System.out.println(e.getError());
            System.out.println(e.getErrorDescription());
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.valueOf(e.getCode()), e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Aconteceu um erro ao conseguir os detalhes da cobrança");
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
}
