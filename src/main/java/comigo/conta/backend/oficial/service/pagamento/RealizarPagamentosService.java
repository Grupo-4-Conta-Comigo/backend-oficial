package comigo.conta.backend.oficial.service.pagamento;

import br.com.gerencianet.gnsdk.Gerencianet;
import br.com.gerencianet.gnsdk.exceptions.GerencianetException;
import com.mifmif.common.regex.Generex;
import comigo.conta.backend.oficial.domain.pagamento.DetalhesPagamento;
import comigo.conta.backend.oficial.service.usuario.UsuarioService;
import comigo.conta.backend.oficial.service.usuario.dto.UsuarioNomeDocumentoDto;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    public RealizarPagamentosService(CertificadoPagamentoService certificadoPagamentoService, GerenciaDetalhesPagamentoService gerenciaDetalhesPagamentoService, UsuarioService usuarioService) {
        this.certificadoPagamentoService = certificadoPagamentoService;
        this.gerenciaDetalhesPagamentoService = gerenciaDetalhesPagamentoService;
        this.usuarioService = usuarioService;
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
