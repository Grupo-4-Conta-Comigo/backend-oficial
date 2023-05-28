package comigo.conta.backend.oficial.service.pagamento;

import comigo.conta.backend.oficial.domain.pagamento.Pagamento;
import comigo.conta.backend.oficial.domain.pagamento.repository.PagamentoRepository;
import comigo.conta.backend.oficial.domain.shared.usecases.GenerateRandomIdUsecase;
import comigo.conta.backend.oficial.domain.usuario.Cargo;
import comigo.conta.backend.oficial.domain.usuario.Usuario;
import comigo.conta.backend.oficial.domain.usuario.repository.UsuarioRepository;
import comigo.conta.backend.oficial.service.usuario.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ArquivoPagamentoService {
    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;
    private final PagamentoRepository pagamentoRepository;
    private final GenerateRandomIdUsecase generateRandomIdUsecase;
    private final PasswordEncoder passwordEncoder;

    public ArquivoPagamentoService(UsuarioService usuarioService, UsuarioRepository usuarioRepository, PagamentoRepository pagamentoRepository, GenerateRandomIdUsecase generateRandomIdUsecase, PasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.usuarioRepository = usuarioRepository;
        this.pagamentoRepository = pagamentoRepository;
        this.generateRandomIdUsecase = generateRandomIdUsecase;
        this.passwordEncoder = passwordEncoder;
    }

    public void gravarRegistro(String registro, String nomeArquivo) {
        try (BufferedWriter saida = new BufferedWriter(new FileWriter(nomeArquivo, true))) {
            saida.append(registro);
        } catch (IOException erro) {
            erro.printStackTrace();
            System.out.println("Erro ao abrir o arquivo!");
        }
    }

    public File gravarArquivoTxt(String idRestaurante) {
        final String hoje = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        final Usuario restaurante = usuarioService.getUsuarioOrThrow404(idRestaurante);
        final String nomeArquivo = restaurante.getNome().replace(" ", "-").concat("-relatiorio-").concat(hoje).concat(".txt");
        int contadorReg = 0;
        final String header =
                "00RECEITA ESTABELECIMENTO"
                        .concat(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                        .concat("V2");
        gravarRegistro(header, nomeArquivo);
        String corpo01 = "01"
                .concat(String.format("%-36.36s", restaurante.getId()))
                .concat(String.format("%-45.45s", restaurante.getNome()))
                .concat(String.format("%-18.18s", restaurante.getRegistro()))
                .concat(String.format("%-10.10s", restaurante.getCargo()))
                .concat(String.format("%-45.45s", restaurante.getEmail()))
                .concat(String.format("%-30.30s", restaurante.getSenha()));
        gravarRegistro(corpo01, nomeArquivo);
        contadorReg++;

        List<Pagamento> listaPagamento = pagamentoRepository.findByIdRestauranteAndDataHoraPagamentoBetween(
                idRestaurante,
                LocalDateTime.of(
                        LocalDateTime.now().getYear(),
                        LocalDateTime.now().getMonth(),
                        LocalDateTime.now().getDayOfMonth(),
                        0,
                        0
                ),
                LocalDateTime.of(
                        LocalDateTime.now().getYear(),
                        LocalDateTime.now().getMonth(),
                        LocalDateTime.now().getDayOfMonth(),
                        23,
                        59
                )
        );
        for (Pagamento pagamento : listaPagamento) {
            String corpo02 = "02"
                    .concat(String.format("%36.36s", pagamento.getId()))
                    .concat(String.format("%-40.40s", pagamento.getChavePix()))
                    .concat(String.format("%010.2f", pagamento.getValorPagamento()))
                    .concat(String.format("%36.36s", pagamento.getIdRestaurante()))
                    .concat(String.format("%-5.5s", pagamento.isPagamentoConcluido()))
                    .concat(String.format("%-19.19s", pagamento.getDataHoraPagamento()));
            gravarRegistro(corpo02, nomeArquivo);
            contadorReg++;
        }

        String trailer = "03".concat(String.format("%05d", contadorReg));
        gravarRegistro(trailer, nomeArquivo);

        return new File(nomeArquivo);
    }

    public void lerArquivoTxt(MultipartFile arquivoTxt, String idRestaurante) {
        AtomicInteger contagemRegistrosLidos = new AtomicInteger();
        try (BufferedReader entrada = new BufferedReader(new InputStreamReader(arquivoTxt.getInputStream()))) {
            entrada.lines().forEach(
                    registro -> {
                        String tipoRegistro = registro.substring(0, 2);
                        switch (tipoRegistro) {
                            case "00" -> {
                                System.out.println("É um registro header");
                                System.out.println("Tipo de arquivo: " + registro.substring(2, 25));
                                System.out.println("Data e hora: " + registro.substring(26, 44));
                                System.out.println("Versão layout: " + registro.substring(44, 46));
                            }
                            case "01" -> {
                                System.out.println("É um registro de dados ou corpo");
                                salvarNovoUsuario(registro, idRestaurante);
                                contagemRegistrosLidos.getAndIncrement();
                            }
                            case "02" -> {
                                System.out.println("É um registro de dados ou corpo");
                                salvarNovoPagamento(registro, idRestaurante);
                                contagemRegistrosLidos.getAndIncrement();
                            }
                            case "03" -> {
                                System.out.println("É um registro de trailer");
                                lerTrailer(contagemRegistrosLidos.get(), registro);
                            }
                            default -> throw new ResponseStatusException(
                                    HttpStatus.BAD_REQUEST,
                                    "Tipo de registro inválido"
                            );
                        }
                    }
            );
        } catch (IOException erro) {
            erro.printStackTrace();
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Erro ao ler o arquivo..."
            );
        } catch (NumberFormatException erro) {
            erro.printStackTrace();
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Registro com valores inválidos"
            );
        }
    }

    private static void lerTrailer(int contRegLidos, String registro) {
        int regDadosGravados = Integer.parseInt(registro.substring(2, 7));
        if (regDadosGravados == contRegLidos) {
            System.out.println(
                    "Quantidade de resgistros de dados gravados compatível com quantidade de registros de dados lidos"
            );
        } else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Quantidade de registros de dados gravados incompatível com quantidade de registros de dados lidos"
            );
        }
    }

    private void salvarNovoPagamento(String registro, String idRestaurante) {
        System.out.println(registro);
        String idPagamento = generateRandomIdUsecase.execute();
        String chavePix = registro.substring(2, 42).trim();
        double valorTotal = Double.parseDouble(registro.substring(42, 52).replace(',', '.'));
        boolean isPagamentoConcluido = Boolean.parseBoolean(registro.substring(52, 57));
        LocalDateTime dataHoraPagamento = LocalDateTime.parse(registro.substring(57, 76));
        Pagamento pagamento = new Pagamento();
        pagamento.setId(idPagamento);
        pagamento.setIdRestaurante(idRestaurante);
        pagamento.setChavePix(chavePix);
        pagamento.setValorPagamento(valorTotal);
        pagamento.setPagamentoConcluido(isPagamentoConcluido);
        pagamento.setDataHoraPagamento(dataHoraPagamento);
        pagamentoRepository.save(pagamento);
    }

    private void salvarNovoUsuario(String registro, String idRestaurante) {
        String id = generateRandomIdUsecase.execute();
        String nome = registro.substring(2, 46).trim();
        String registroUsuario = registro.substring(46, 64).trim();
        String cargo = registro.substring(64, 74).trim();
        String email = registro.substring(74, 119).trim();
        String senha = registro.substring(119, 149).trim();
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNome(nome);
        usuario.setRegistro(registroUsuario);
        usuario.setCargo(Cargo.valueOf(cargo));
        usuario.setEmail(email);
        usuario.setRestauranteId(idRestaurante);
        usuario.setSenha(passwordEncoder.encode(senha));
        usuarioRepository.save(usuario);
    }
}
