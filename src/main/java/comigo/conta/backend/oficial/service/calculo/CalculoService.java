package comigo.conta.backend.oficial.service.calculo;

import comigo.conta.backend.oficial.service.calculo.dto.CalculoPersonalizadoRequestDto;
import comigo.conta.backend.oficial.service.calculo.dto.CalculoPersonalizadoResponseDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class CalculoService {

    public List<CalculoPersonalizadoResponseDto> calcularPersonalizado(
            CalculoPersonalizadoRequestDto calculoPersonalizadoRequestDto
    ) {
        HashMap<String, Double> pagantes = new HashMap<>();
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
}
