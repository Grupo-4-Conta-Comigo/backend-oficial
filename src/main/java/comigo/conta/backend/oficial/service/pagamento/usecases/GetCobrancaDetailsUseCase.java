package comigo.conta.backend.oficial.service.pagamento.usecases;

import br.com.gerencianet.gnsdk.Gerencianet;
import br.com.gerencianet.gnsdk.exceptions.GerencianetException;
import comigo.conta.backend.oficial.domain.pagamento.DetalhesPagamento;
import comigo.conta.backend.oficial.service.pagamento.CertificadoPagamentoService;
import comigo.conta.backend.oficial.service.pagamento.dto.CobrancaDetailsDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class GetCobrancaDetailsUseCase implements Callable<CobrancaDetailsDto> {
    private final Integer idCobranca;
    private final DetalhesPagamento detalhesPagamento;
    private final String idRestaurante;

    public GetCobrancaDetailsUseCase(Integer idCobranca, DetalhesPagamento detalhesPagamento, String idRestaurante) {
        this.idCobranca = idCobranca;
        this.detalhesPagamento = detalhesPagamento;
        this.idRestaurante = idRestaurante;
    }

    @Override
    public CobrancaDetailsDto call() throws Exception {
        final String txid = getTXID();
        final Map<String, Object> result = getDetalhesDeCobranca(detalhesPagamento, idRestaurante, txid);
        return new CobrancaDetailsDto(result);
    }

    private String getTXID() {
        HashMap<String, Object> options = new HashMap<>();
        options.put("client_id", detalhesPagamento.getClientId());
        options.put("client_secret", detalhesPagamento.getClientSecret());
        options.put("certificate", CertificadoPagamentoService.getRealCertificadoPathStatic(idRestaurante, detalhesPagamento.getNomeCertificado()));
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
        options.put("certificate", CertificadoPagamentoService.getRealCertificadoPathStatic(idRestaurante, detalhesPagamento.getNomeCertificado()));
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
}
