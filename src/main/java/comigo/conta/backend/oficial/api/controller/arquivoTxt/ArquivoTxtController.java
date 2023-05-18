package comigo.conta.backend.oficial.api.controller.arquivoTxt;


import comigo.conta.backend.oficial.domain.pagamento.DetalhesPagamento;
import comigo.conta.backend.oficial.domain.usuario.Usuario;
import comigo.conta.backend.oficial.service.arquivoTxt.ArquivoTxtService;
import comigo.conta.backend.oficial.service.pagamento.GerenciaDetalhesPagamentoService;
import comigo.conta.backend.oficial.service.usuario.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;

@RestController
@RequestMapping("/registros")
public class ArquivoTxtController {

    private final UsuarioService usuarioService;

    private final GerenciaDetalhesPagamentoService gerenciaDetalhesPagamentoService;
    private ArquivoTxtService service;

    public ArquivoTxtController(UsuarioService usuarioService, GerenciaDetalhesPagamentoService gerenciaDetalhesPagamentoService){

        this.usuarioService = usuarioService;
        this.gerenciaDetalhesPagamentoService = gerenciaDetalhesPagamentoService;


    }

    @GetMapping("/exportar-registro")
    public ResponseEntity<Void> exportarTxt(@RequestBody List<Usuario> usuarios , @RequestBody List<DetalhesPagamento> detalhesPagamento){

        service.gravarArquivoTxt(usuarios, detalhesPagamento, "registros.txt");

        return ResponseEntity.ok().build();

    }

    @PostMapping("/importar-registro")
    public ResponseEntity<Void> importarTxt(@RequestParam String arquivo){

        service.lerArquivoTxt(arquivo);

        return ResponseEntity.ok().build();

    }






}
