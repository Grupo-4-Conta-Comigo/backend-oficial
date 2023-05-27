package comigo.conta.backend.oficial.service.calculo;

import comigo.conta.backend.oficial.service.calculo.dto.CalculoPersonalizadoRequestDto;
import comigo.conta.backend.oficial.service.calculo.dto.CalculoPersonalizadoResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CalculoService {

    public List<CalculoPersonalizadoResponseDto> calcularPersonalizado(
            CalculoPersonalizadoRequestDto calculoPersonalizadoRequestDto
    ) {
        Map<String, Double> pagantes = new HashMap<>();
        Map<String, String> idComandas = new HashMap<>();
        double somaPagantes = 0;
        for (final var item : calculoPersonalizadoRequestDto.getItens()) {
            for (final var pagante : item.getPagantes()) {
                somaPagantes += Math.round(pagante.getValorAPagar() * 100);
                pagantes.merge(pagante.getNome(), pagante.getValorAPagar(), Double::sum);
                idComandas.putIfAbsent(pagante.getNome(), pagante.getIdComanda());
            }
        }
        List<CalculoPersonalizadoResponseDto> response = new ArrayList<>();
        for (final var entry : pagantes.entrySet()) {
            response.add(new CalculoPersonalizadoResponseDto(idComandas.get(entry.getKey()), entry.getKey(), entry.getValue()));
        }
        if (somaPagantes != Math.round(calculoPersonalizadoRequestDto.getValorTotal() * 100)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    String.format(
                            "Soma dos values dos pagantes é diferente do valor total. Soma dos pagantes: %.2f. Valor Total: %.2f",
                            somaPagantes / 100,
                            calculoPersonalizadoRequestDto.getValorTotal()
                    )
            );
        }
        return response;
    }
}
