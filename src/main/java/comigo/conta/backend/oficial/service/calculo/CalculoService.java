package comigo.conta.backend.oficial.service.calculo;

import comigo.conta.backend.oficial.service.calculo.dto.CalculoPersonaliazdoPaganteDto;
import comigo.conta.backend.oficial.service.calculo.dto.CalculoPersonalizadoRequestDto;
import comigo.conta.backend.oficial.service.calculo.dto.CalculoPersonalizadoResponseDto;
import org.springframework.stereotype.Service;

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
        for (final var item : calculoPersonalizadoRequestDto.getItens()) {
            for (final var pagante : item.getPagantes()) {
                pagantes.merge(pagante.getNome(), pagante.getValorAPagar(), Double::sum);
            }
        }
        List<CalculoPersonalizadoResponseDto> response = new ArrayList<>();
        for (final var entry : pagantes.entrySet()) {
            response.add(new CalculoPersonalizadoResponseDto(entry.getKey(), entry.getValue()));
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
