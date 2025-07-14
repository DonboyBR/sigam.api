package com.medeiros.sigam_api.controller;

import com.medeiros.sigam_api.model.Venda;
import com.medeiros.sigam_api.service.VendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List; // Para retornar listas de vendas
import java.util.Optional; // Para lidar com vendas que podem não existir

@RestController
@RequestMapping("/api/vendas")
public class VendaController {

    private final VendaService vendaService;

    @Autowired
    public VendaController(VendaService vendaService) {
        this.vendaService = vendaService;
    }

    // --- ENDPOINTS DA API ---

    // 1. Criar uma nova Venda (Método POST)

    @PostMapping
    public ResponseEntity<Venda> criarVenda(@RequestBody Venda venda) {
        try {
            Venda novaVenda = vendaService.criarVenda(venda);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaVenda); // Retorna 201 Created
        } catch (RuntimeException e) {
            // Captura exceções como "Produto não encontrado" ou "Estoque insuficiente"
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // 2. Buscar todas as Vendas (Método GET)
    @GetMapping
    public ResponseEntity<List<Venda>> buscarTodasVendas() {
        List<Venda> vendas = vendaService.buscarTodasVendas();
        return ResponseEntity.ok(vendas);
    }

    // 3. Buscar uma Venda por ID (Método GET com variável de caminho)
    @GetMapping("/{id}")
    public ResponseEntity<Venda> buscarVendaPorId(@PathVariable Long id) {
        Optional<Venda> venda = vendaService.buscarVendaPorId(id);
        return venda.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 4. Deletar uma Venda (Método DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarVenda(@PathVariable Long id) {
        boolean deletado = vendaService.deletarVenda(id);
        if (deletado) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
