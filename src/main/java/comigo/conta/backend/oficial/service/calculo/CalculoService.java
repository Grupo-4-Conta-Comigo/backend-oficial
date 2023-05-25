package comigo.conta.backend.oficial.service.calculo;

import comigo.conta.backend.oficial.service.calculo.dto.CalculoPersonaliazdoPaganteDto;
import comigo.conta.backend.oficial.service.calculo.dto.CalculoPersonalizadoRequestDto;
import comigo.conta.backend.oficial.service.calculo.dto.CalculoPersonalizadoResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingDouble;

@Service
public class CalculoService {

    public List<CalculoPersonalizadoResponseDto> calcularPersonalizado(
            CalculoPersonalizadoRequestDto calculoPersonalizadoRequestDto
    ) {
        Map<String, Double> pagantes = new HashMap<>();
        double somaPagantes = 0;
        for (final var item : calculoPersonalizadoRequestDto.getItens()) {
            for (final var pagante : item.getPagantes()) {
                somaPagantes += Math.round(pagante.getValorAPagar() * 100);
                pagantes.merge(pagante.getNome(), pagante.getValorAPagar(), Double::sum);
            }
        }
        List<CalculoPersonalizadoResponseDto> response = new ArrayList<>();
        for (final var entry : pagantes.entrySet()) {
            response.add(new CalculoPersonalizadoResponseDto(entry.getKey(), entry.getValue()));
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

    public List<CalculoPersonalizadoResponseDto> calcularPersonalizadoIlegivel(
            CalculoPersonalizadoRequestDto calculoPersonalizadoRequestDto
    ) {
        return calculoPersonalizadoRequestDto.getItens()
                .stream()
                .flatMap(item -> item.getPagantes().stream())
                .collect(groupingBy(CalculoPersonaliazdoPaganteDto::getNome, summingDouble(CalculoPersonaliazdoPaganteDto::getValorAPagar)))
                .entrySet()
                .stream()
                .map(entry -> new CalculoPersonalizadoResponseDto(entry.getKey(), entry.getValue()))
                .toList();
    }
}
