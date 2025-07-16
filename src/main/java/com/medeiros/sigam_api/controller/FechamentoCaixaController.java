package com.medeiros.sigam_api.controller;

import com.medeiros.sigam_api.model.FechamentoCaixa;
import com.medeiros.sigam_api.service.FechamentoCaixaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/caixa")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4201", "http://localhost:4202",
        "http://localhost:4203", "http://localhost:4204", "http://localhost:4205",
        "http://localhost:56235", "http://localhost:8080"}) // Mantenha as portas que você configurou
public class FechamentoCaixaController {

    private final FechamentoCaixaService fechamentoCaixaService;

    @Autowired
    public FechamentoCaixaController(FechamentoCaixaService fechamentoCaixaService) {
        this.fechamentoCaixaService = fechamentoCaixaService;
    }

    @PostMapping("/abrir")
    public ResponseEntity<FechamentoCaixa> abrirCaixa(@RequestBody FechamentoCaixa request) {
        try {
            FechamentoCaixa novoCaixa = fechamentoCaixaService.abrirCaixa(
                    request.getValorInicialTroco(),
                    request.getResponsavelFechamento()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(novoCaixa);
        } catch (RuntimeException e) {
            System.err.println("Erro ao abrir caixa no backend: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/fechar/{idCaixa}")
    public ResponseEntity<FechamentoCaixa> fecharCaixa(@PathVariable Long idCaixa, @RequestBody FechamentoCaixa request) {
        try {
            FechamentoCaixa caixaFechado = fechamentoCaixaService.fecharCaixa(
                    idCaixa,
                    request.getTotalVendasEVO(),
                    request.getResponsavelFechamento(),
                    request.getObservacoes() // 👈 ATENÇÃO AQUI! Passando as observações
            );
            return ResponseEntity.ok(caixaFechado);
        } catch (RuntimeException e) {
            System.err.println("Erro ao fechar caixa no backend: " + e.getMessage());
            // Retorna 404 se o caixa não for encontrado, ou 400 para outros erros de lógica
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<FechamentoCaixa> buscarFechamentoCaixaPorId(@PathVariable Long id) {
        Optional<FechamentoCaixa> fechamentoCaixa = fechamentoCaixaService.buscarFechamentoCaixaPorId(id);
        return fechamentoCaixa.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<FechamentoCaixa>> buscarTodosFechamentosCaixa() {
        List<FechamentoCaixa> fechamentos = fechamentoCaixaService.buscarTodosFechamentosCaixa();
        return ResponseEntity.ok(fechamentos);
    }

    @GetMapping("/aberto")
    public ResponseEntity<FechamentoCaixa> buscarCaixaAberto() {
        Optional<FechamentoCaixa> caixaAberto = fechamentoCaixaService.buscarCaixaAberto();
        return caixaAberto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}