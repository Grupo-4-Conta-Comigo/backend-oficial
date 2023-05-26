package comigo.conta.backend.oficial.service.pagamento;

import comigo.conta.backend.oficial.domain.pagamento.DetalhesPagamento;
import comigo.conta.backend.oficial.domain.pagamento.repository.DetalhesPagamentoRepository;
import comigo.conta.backend.oficial.domain.shared.usecases.GenerateRandomIdUsecase;
import comigo.conta.backend.oficial.domain.shared.utils.FileUtils;
import comigo.conta.backend.oficial.service.usuario.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Optional;

@Service
public class CertificadoPagamentoService {
    private final DetalhesPagamentoRepository repository;
    private final UsuarioService usuarioService;
    private final GenerateRandomIdUsecase generateRandomIdUsecase;

    public CertificadoPagamentoService(
            final DetalhesPagamentoRepository repository,
            final UsuarioService usuarioService,
            final GenerateRandomIdUsecase generateRandomIdUsecase
    ) {
        this.repository = repository;
        this.usuarioService = usuarioService;
        this.generateRandomIdUsecase = generateRandomIdUsecase;
    }

    public DetalhesPagamento cadastrarCertificado(
            String idRestaurante,
            MultipartFile certificado
    ) {
        if (usuarioService.naoExiste(idRestaurante)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado...");
        }
        validateFileName(certificado.getOriginalFilename());
        return cadastrarCertificadoNoBancoDeDados(idRestaurante, certificado);
    }

    public boolean certificadoExisteNoBanco(String idRestaurante) {
        if (usuarioService.naoExiste(idRestaurante)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado...");
        }
        return repository.findByUsuarioId(idRestaurante)
                .map(
                        detalhesPagamento ->
                                detalhesPagamento.getCertificado().length != 0 ||
                                        detalhesPagamento.getCertificado() != null
                ).orElse(false);
    }

    private File getDiretorioPaiDoCertificado(String idRestaurante) {
        return new File(FileUtils.PAYMENT_DETAILS_CERTIFICATES_PATH.toFile(), idRestaurante);
    }

    public boolean certificadoExisteLocalmente(String idRestaurante, String nomeArquivo) {
        File diretorioPai = getDiretorioPaiDoCertificado(idRestaurante);
        return new File(diretorioPai, nomeArquivo).exists();
    }

    private Optional<File> getCertificadoLocal(File diretorioPaiCertificado) {
        if (!diretorioPaiCertificado.exists()) {
            return Optional.empty();
        }
        File[] arquivosDiretorioCertificado = diretorioPaiCertificado.listFiles();
        if (arquivosDiretorioCertificado == null || arquivosDiretorioCertificado.length == 0) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Chama o dono desse back que ele tem que consertar isso"
            );
        }
        File certificado = Arrays.stream(arquivosDiretorioCertificado)
                .findFirst()
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                "Chama o dono desse back que ele tem que consertar isso"
                        ));
        return Optional.of(certificado);
    }

    private void cadastrarCertificadoNoBancoDeDados(
            String idRestaurante,
            File certificado
    ) {
        final DetalhesPagamento detalhesPagamento = getDetalhesPagamentoOrCreateDetalhesPagamento(idRestaurante);
        if (detalhesPagamento.getCertificado() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Arquivo já cadastrado");
        }
        try {
            detalhesPagamento.setCertificado(Files.readAllBytes(certificado.toPath()));
            detalhesPagamento.setNomeCertificado(certificado.getName());
            repository.save(detalhesPagamento);
        } catch (IOException e) {
            throw new ResponseStatusException(500, "Erro ao salvar o arquivo ao banco de dados.", e);
        }
    }

    private void validateFileName(String fileName) {
        if (fileName == null || fileName.contains("..")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome de arquivo inválido: " + fileName);
        }
    }

    public Path getRealCertificadoPath(String idRestaurante, String nomeCertificado) {
        return Path.of(
                FileUtils.PAYMENT_DETAILS_CERTIFICATES_PATH.toString(),
                idRestaurante,
                nomeCertificado
        );
    }
    public static Path getRealCertificadoPathStatic(String idRestaurante, String nomeCertificado) {
        return Path.of(
                FileUtils.PAYMENT_DETAILS_CERTIFICATES_PATH.toString(),
                idRestaurante,
                nomeCertificado
        );
    }

    public void criarCertificadoLocalmente(
            byte[] certificado,
            String idRestaurante,
            String nomeCertificado
    ) {
        try {
            Files.createDirectory(
                    FileUtils.PAYMENT_DETAILS_CERTIFICATES_PATH.resolve(idRestaurante)
            );
            Files.createFile(
                    getRealCertificadoPath(idRestaurante, nomeCertificado)
            );
            Files.copy(
                    new ByteArrayInputStream(certificado),
                    getRealCertificadoPath(idRestaurante, nomeCertificado),
                    StandardCopyOption.REPLACE_EXISTING
            );
        } catch (FileAlreadyExistsException e) {
            e.printStackTrace();
        } catch (IOException | UncheckedIOException e) {
            e.printStackTrace();
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    String.format(
                            "Erro ao salvar o arquivo %s localmente",
                            nomeCertificado
                    )
            );
        }
    }

    private DetalhesPagamento cadastrarCertificadoNoBancoDeDados(
            String idRestaurante,
            MultipartFile certificado
    ) {
        DetalhesPagamento detalhesPagamento = getDetalhesPagamentoOrCreateDetalhesPagamento(idRestaurante);
        if (detalhesPagamento.getCertificado() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Arquivo já cadastrado");
        }
        try {
            detalhesPagamento.setCertificado(certificado.getBytes());
            detalhesPagamento.setNomeCertificado(certificado.getOriginalFilename());
            return repository.save(detalhesPagamento);
        } catch (IOException e) {
            throw new ResponseStatusException(500, "Erro ao salvar o arquivo ao banco de dados.", e);
        }
    }

    private DetalhesPagamento getDetalhesPagamentoOrCreateDetalhesPagamento(String idRestaurante) {
        return repository.findByUsuarioId(idRestaurante).orElseGet(() -> criarNovosDetalhesPagamento(idRestaurante));
    }

    private DetalhesPagamento criarNovosDetalhesPagamento(String idRestaurante) {
        final DetalhesPagamento detalhesPagamento = new DetalhesPagamento();
        detalhesPagamento.setUsuarioId(idRestaurante);
        detalhesPagamento.setId(generateRandomIdUsecase.execute());
        return repository.save(detalhesPagamento);
    }
}
