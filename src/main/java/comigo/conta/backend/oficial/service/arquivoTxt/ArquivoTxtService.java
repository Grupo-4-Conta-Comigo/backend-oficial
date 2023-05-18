package comigo.conta.backend.oficial.service.arquivoTxt;

import comigo.conta.backend.oficial.domain.pagamento.DetalhesPagamento;
import comigo.conta.backend.oficial.domain.usuario.Usuario;
import comigo.conta.backend.oficial.domain.usuario.repository.UsuarioRepository;
import comigo.conta.backend.oficial.service.usuario.UsuarioService;
import comigo.conta.backend.oficial.service.usuario.dto.GarcomCriacaoDto;

import javax.swing.*;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ArquivoTxtService {

    private final UsuarioService usuarioService;
    private final GarcomCriacaoDto garcomCriacaoDto;

    public ArquivoTxtService(UsuarioService usuarioService, GarcomCriacaoDto garcomCriacaoDto) {
        this.usuarioService = usuarioService;
        this.garcomCriacaoDto = garcomCriacaoDto;
    }

    public void gravarRegistro(String registro, String nomeArquivo){
        BufferedWriter saida = null;

        try{
            saida = new BufferedWriter(new FileWriter(nomeArquivo, true));
        } catch(IOException erro){
            System.out.println("Erro ao abrir o arquivo!");
            System.exit(1);
        }

        try{
            saida.append(registro + "\n");
            saida.close();
        } catch(IOException erro){
            System.out.println("Erro ao gravar no arquivo ");
        }
    }

    public void gravarArquivoTxt(List<Usuario> lista, List<DetalhesPagamento> listaPagamento, String nomeArquivo){
        int contadorReg = 0;

        String header ="00RECEITAESTABELECIMENTO";
        header += LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        header += "V1";

        gravarRegistro(header, nomeArquivo);

        String corpo01;
        for (int i = 0; i < lista.size(); i++){
            Usuario e = lista.get(i);
            corpo01 ="01";
            corpo01 += String.format("%2.2s", (e.getId()));
            corpo01 +=String.format("%-45.45s", e.getNome());
            corpo01 +=String.format("%-18.18s", e.getRegistro());
            corpo01 +=String.format("%-10.10s", e.getCargo());
            corpo01 +=String.format("%-45.45s", e.getEmail());
            corpo01 += String.format("%-30.30s", e.getSenha());
            corpo01 +=String.format("%4.4s", e.getRestauranteId());

            gravarRegistro(corpo01, nomeArquivo);
            contadorReg ++;
        }


        String corpo02;
        for (int i = 0; i < listaPagamento.size(); i++){
            DetalhesPagamento p = listaPagamento.get(i);
            corpo02 = "02";
            corpo02 += String.format("%2.2s", p.getId());
            corpo02 +=String.format("%2.2s", p.getClientId());
            corpo02 +=String.format("%-40.40s", p.getChavePix());
            corpo02 += String.format("%40s", p.getNomeCertificado());

            gravarRegistro(corpo02, nomeArquivo);
            contadorReg ++;
        }

        String trailer = "03";
        trailer +=String.format("%05d", contadorReg);

        gravarRegistro(trailer, nomeArquivo);
    }

    public void lerArquivoTxt(String nomeArquivo){

        BufferedReader entrada = null;
        // Arquivo
        String registro, tipoRegistro;
        //Usuário
        String nome, regUsuario, cargo, email, senha;
        Long id, restauranteId;
        //Pagamento
        Long idPagamento, restauranteIdPag;
        String chavePix;
        Double valorBruto, valorDesconto, valorLiquido;
        boolean isPagamentoConcluido;
        LocalDate dataHoraPagamento;

        int contRegLidos = 0;
        int regDadosGravados;

        List<Usuario> listaUsuarioLida = new ArrayList<>();
        List<DetalhesPagamento> listaPagamentoLida = new ArrayList<>();

        try{
            entrada = new BufferedReader(new FileReader(nomeArquivo));
        } catch(IOException erro){
            System.out.println("Erro ao abrir arquivo");
            System.exit(1);
        }


        try{
            registro = entrada.readLine();

            while (registro != null){
                tipoRegistro = registro.substring(0,2);
                if (tipoRegistro.equals("00")){
                    System.out.println("É um registro header");
                    System.out.println("Tipo de arquivo: " + registro.substring(2,25));
                    System.out.println("Data e hora: " + registro.substring(25,44));
                    System.out.println("Versão layout: " + registro.substring(44,46));
                }
                else if (tipoRegistro.equals("03")){
                    System.out.println("É um registro de trailer");
                    regDadosGravados = Integer.parseInt(registro.substring(2,7));
                    if (regDadosGravados == contRegLidos){
                        System.out.println("Quantidade de resgistros de dados gravados" +
                                "compatível com quantidade de registros de dados lidos");
                    }
                    else {
                        System.out.println("Quantidade de registros de dados gravados incompatível com" +
                                " quantidade de registros de dados lidos");
                    }
                }
                else if (tipoRegistro.equals("01")){
                    System.out.println("É um registro de dados ou corpo");
                    id = Long.parseLong(registro.substring(2,4));
                    nome = registro.substring(4,49).trim();
                    regUsuario = registro.substring(49,67).trim();
                    cargo = registro.substring(67,77).trim();
                    email = registro.substring(77,122).trim();
                    senha = registro.substring(122,152).trim();
                    restauranteId = Long.parseLong(registro.substring(152,156));

//                    GarcomCriacaoDto = new GarcomCriacaoDto();
//
//                    usuarioService.criarGarcom();

                    //repository.save(u)

                }
                else if (tipoRegistro.equals("02")){

                    System.out.println("É um registro de dados ou corpo");
                    idPagamento = Long.parseLong(registro.substring(2,4));
                    restauranteIdPag = Long.parseLong(registro.substring(4,8));
                    chavePix = registro.substring(8,48).trim();
                    valorBruto = Double.valueOf(registro.substring(48,58).replace(',', '.'));
                    valorDesconto = Double.valueOf(registro.substring(58,68).replace(',', '.'));
                    valorLiquido = Double.valueOf(registro.substring(68,78).replace(',', '.'));
                    isPagamentoConcluido = Boolean.parseBoolean(registro.substring(78,83));
                    dataHoraPagamento = LocalDate.parse(registro.substring(83,102));
                    DetalhesPagamento p = new DetalhesPagamento();

                    //repository.save(p)

                }
                else{
                    System.out.println("Tipo de registro inválido!");
                }

                registro = entrada.readLine();
            }
            entrada.close();
        }catch (IOException erro){

            System.out.println("Erro ao ler o arquivo");

        }


    }

}
