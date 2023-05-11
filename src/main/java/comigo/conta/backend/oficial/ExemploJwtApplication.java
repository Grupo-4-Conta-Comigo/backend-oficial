package comigo.conta.backend.oficial;

import comigo.conta.backend.oficial.service.shared.usecases.CreateCertificatesFolderUseCase;
import comigo.conta.backend.oficial.service.shared.usecases.implementations.DefaultArquivoExisteUseCase;
import comigo.conta.backend.oficial.service.shared.usecases.implementations.DefaultCreateCertificatesFolderUseCase;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static comigo.conta.backend.oficial.domain.shared.utils.FileUtils.*;

@SpringBootApplication
public class ExemploJwtApplication {
    public static void main(String[] args) {
        CreateCertificatesFolderUseCase useCase = new DefaultCreateCertificatesFolderUseCase(new DefaultArquivoExisteUseCase());
        System.out.println(PAYMENT_DETAILS_CERTIFICATES_PATH);
        useCase.execute(PAYMENT_DETAILS_CERTIFICATES_PATH);
        SpringApplication.run(ExemploJwtApplication.class, args);
    }

}
