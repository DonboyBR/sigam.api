package com.medeiros.sigam_api.controller;

import com.medeiros.sigam_api.model.Ativo;
import com.medeiros.sigam_api.service.AtivoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController // Indica que esta classe é um controlador REST
@RequestMapping("/api/ativos") // Define o caminho base para todas as URLs deste controlador
// Configuração CORS: Permite requisições de múltiplas origens localhost para desenvolvimento
// Esta configuração global no CorsConfig.java já deveria ser suficiente, mas mantemos aqui para clareza
// em caso de problemas específicos ou se o CorsConfig.java for removido/desabilitado.
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4201", "http://localhost:4202",
        "http://localhost:4203", "http://localhost:4204", "http://localhost:4205",
        "http://localhost:56235", "http://localhost:8080"})
public class AtivoController {

    private final AtivoService ativoService; // O serviço que vamos usar

    // Injeção de Dependência: O Spring entrega uma instância de AtivoService aqui
    @Autowired
    public AtivoController(AtivoService ativoService) {
        this.ativoService = ativoService;
    }

    // --- ENDPOINTS DA API ---

    // 1. Criar um novo Ativo (Método POST)
    // URL: POST http://localhost:8080/api/ativos
    @PostMapping
    public ResponseEntity<Ativo> criarAtivo(@RequestBody Ativo ativo) {
        try {
            Ativo novoAtivo = ativoService.criarAtivo(ativo);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoAtivo); // Retorna 201 Created
        } catch (RuntimeException e) {
            // Em caso de erro, retorna 400 Bad Request. Em produção, seria mais detalhado.
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // 2. Buscar todos os Ativos (Método GET)
    // URL: GET http://localhost:8080/api/ativos
    @GetMapping
    public ResponseEntity<List<Ativo>> buscarTodosAtivos() {
        List<Ativo> ativos = ativoService.buscarTodosAtivos();
        return ResponseEntity.ok(ativos); // Retorna 200 OK
    }

    // 3. Buscar um Ativo por ID (Método GET com variável de caminho)
    // URL: GET http://localhost:8080/api/ativos/1
    @GetMapping("/{id}")
    public ResponseEntity<Ativo> buscarAtivoPorId(@PathVariable Long id) {
        Optional<Ativo> ativo = ativoService.buscarAtivoPorId(id);
        return ativo.map(ResponseEntity::ok) // Se encontrar, retorna 200 OK
                .orElseGet(() -> ResponseEntity.notFound().build()); // Se não, retorna 404 Not Found
    }

    // 4. Atualizar um Ativo existente (Método PUT)
    // URL: PUT http://localhost:8080/api/ativos/1
    @PutMapping("/{id}")
    public ResponseEntity<Ativo> atualizarAtivo(@PathVariable Long id, @RequestBody Ativo ativoDetalhes) {
        Ativo ativoAtualizado = ativoService.atualizarAtivo(id, ativoDetalhes);
        if (ativoAtualizado != null) {
            return ResponseEntity.ok(ativoAtualizado); // Retorna 200 OK
        }
        return ResponseEntity.notFound().build(); // Retorna 404 Not Found
    }

    // 5. Deletar um Ativo (Método DELETE)
    // URL: DELETE http://localhost:8080/api/ativos/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAtivo(@PathVariable Long id) {
        boolean deletado = ativoService.deletarAtivo(id);
        if (deletado) {
            return ResponseEntity.noContent().build(); // Retorna 204 No Content (sucesso sem conteúdo)
        }
        return ResponseEntity.notFound().build(); // Retorna 404 Not Found
    }
}