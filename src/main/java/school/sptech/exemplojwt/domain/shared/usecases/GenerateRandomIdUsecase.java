package school.sptech.exemplojwt.domain.shared.usecases;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GenerateRandomIdUsecase {
    public String execute() {
        return UUID.randomUUID().toString();
    }
}
