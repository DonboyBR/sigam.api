package com.medeiros.sigam_api.controller;

import com.medeiros.sigam_api.model.FechamentoCaixa;
import com.medeiros.sigam_api.service.FechamentoCaixaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal; // Para lidar com valores monetários
import java.util.List; // Para retornar listas
import java.util.Optional; // Para lidar com objetos que podem não existir

@RestController
@RequestMapping("/api/caixa")
public class FechamentoCaixaController {

    private final FechamentoCaixaService fechamentoCaixaService;

    @Autowired
    public FechamentoCaixaController(FechamentoCaixaService fechamentoCaixaService) {
        this.fechamentoCaixaService = fechamentoCaixaService;
    }

    // --- ENDPOINTS DA API ---

    // 1. Abrir um novo Caixa (Método POST)
    // Corpo da requisição: { "valorInicialTroco": 100.00, "responsavel": "Nome do Atendente" }
    @PostMapping("/abrir")
    public ResponseEntity<FechamentoCaixa> abrirCaixa(@RequestBody FechamentoCaixa request) {
        try {
            FechamentoCaixa novoCaixa = fechamentoCaixaService.abrirCaixa(
                    request.getValorInicialTroco(),
                    request.getResponsavelFechamento()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(novoCaixa); // Retorna 201 Created
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Retorna 400 Bad Request
        }
    }

    // 2. Fechar um Caixa existente (Método PUT)
    // Corpo da requisição: { "totalVendasEVO": 1500.00, "responsavel": "Nome do Atendente" }
    @PutMapping("/fechar/{idCaixa}")
    public ResponseEntity<FechamentoCaixa> fecharCaixa(@PathVariable Long idCaixa, @RequestBody FechamentoCaixa request) {
        try {
            FechamentoCaixa caixaFechado = fechamentoCaixaService.fecharCaixa(
                    idCaixa,
                    request.getTotalVendasEVO(), // Total do EVO, input manual para a demo
                    request.getResponsavelFechamento()
            );
            return ResponseEntity.ok(caixaFechado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // 3. Buscar um Fechamento de Caixa por ID (Método GET)
    @GetMapping("/{id}")
    public ResponseEntity<FechamentoCaixa> buscarFechamentoCaixaPorId(@PathVariable Long id) {
        Optional<FechamentoCaixa> fechamentoCaixa = fechamentoCaixaService.buscarFechamentoCaixaPorId(id);
        return fechamentoCaixa.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 4. Buscar todos os Fechamentos de Caixa (Método GET)
    @GetMapping
    public ResponseEntity<List<FechamentoCaixa>> buscarTodosFechamentosCaixa() {
        List<FechamentoCaixa> fechamentos = fechamentoCaixaService.buscarTodosFechamentosCaixa();
        return ResponseEntity.ok(fechamentos);
    }

    // 5. Buscar o Caixa Aberto (Método GET) - Simplificado para a demo
    @GetMapping("/aberto")
    public ResponseEntity<FechamentoCaixa> buscarCaixaAberto() {
        Optional<FechamentoCaixa> caixaAberto = fechamentoCaixaService.buscarCaixaAberto();
        return caixaAberto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
