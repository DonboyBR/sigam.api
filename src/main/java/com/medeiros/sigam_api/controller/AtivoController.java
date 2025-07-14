package com.medeiros.sigam_api.controller;

import com.medeiros.sigam_api.model.Ativo;
import com.medeiros.sigam_api.service.AtivoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ativos") // Define o caminho base para todas as URLs deste controlador
public class AtivoController {

    private final AtivoService ativoService;

    @Autowired
    public AtivoController(AtivoService ativoService) {
        this.ativoService = ativoService;
    }

    //  ENDPOINTS
    // Ativo (Método POST)

    @PostMapping
    public ResponseEntity<Ativo> criarAtivo(@RequestBody Ativo ativo) {
        Ativo novoAtivo = ativoService.criarAtivo(ativo);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoAtivo); // Retorna 201 Created
    }

    // Ativo (Método GET)

    @GetMapping
    public ResponseEntity<List<Ativo>> buscarTodosAtivos() {
        List<Ativo> ativos = ativoService.buscarTodosAtivos();
        return ResponseEntity.ok(ativos); // Retorna 200 OK
    }

    // Ativo por ID (Método GET com variável de caminho)

    @GetMapping("/{id}") // O {id} na URL indica uma variável de caminho
    public ResponseEntity<Ativo> buscarAtivoPorId(@PathVariable Long id) { // @PathVariable pega o {id} da URL
        Optional<Ativo> ativo = ativoService.buscarAtivoPorId(id);
        return ativo.map(ResponseEntity::ok) // Se encontrar, retorna 200 OK
                .orElseGet(() -> ResponseEntity.notFound().build()); // Se não, retorna 404 Not Found
    }

    // Atualizar um Ativo existente (Método PUT)

    @PutMapping("/{id}")
    public ResponseEntity<Ativo> atualizarAtivo(@PathVariable Long id, @RequestBody Ativo ativoDetalhes) {
        Ativo ativoAtualizado = ativoService.atualizarAtivo(id, ativoDetalhes);
        if (ativoAtualizado != null) {
            return ResponseEntity.ok(ativoAtualizado); // Retorna 200 OK
        }
        return ResponseEntity.notFound().build(); // Retorna 404 Not Found
    }

    //  Deletar um Ativo (Método DELETE)

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAtivo(@PathVariable Long id) {
        boolean deletado = ativoService.deletarAtivo(id);
        if (deletado) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}