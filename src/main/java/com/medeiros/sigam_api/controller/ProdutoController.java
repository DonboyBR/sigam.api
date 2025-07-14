package com.medeiros.sigam_api.controller;

import com.medeiros.sigam_api.model.Produto;
import com.medeiros.sigam_api.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/produtos") // Define o caminho base para todas as URLs deste controlador
public class ProdutoController {

    private final ProdutoService produtoService;

    @Autowired
    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    // --- ENDPOINTS DA API ---

    // 1. Criar um novo Produto (Método POST)
    @PostMapping // Mapeia requisições HTTP POST para este método
    public ResponseEntity<Produto> criarProduto(@RequestBody Produto produto) { // @RequestBody: Converte o JSON da requisição para um objeto Produto
        Produto novoProduto = produtoService.criarProduto(produto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoProduto); // Retorna 201 Created e o objeto criado
    }

    // 2. Buscar todos os Produtos (Método GET)
    @GetMapping // Anotação: Mapeia requisições HTTP GET para este método
    public ResponseEntity<List<Produto>> buscarTodosProdutos() {
        List<Produto> produtos = produtoService.buscarTodosProdutos();
        return ResponseEntity.ok(produtos); // Retorna 200 OK e a lista de produtos
    }

    // 3. Buscar um Produto por ID (Método GET com variável de caminho)
    @GetMapping("/{id}") // Anotação: O {id} na URL indica uma variável de caminho
    public ResponseEntity<Produto> buscarProdutoPorId(@PathVariable Long id) { // @PathVariable: Pega o {id} da URL e mapeia para o parâmetro 'id'
        Optional<Produto> produto = produtoService.buscarProdutoPorId(id);
        return produto.map(ResponseEntity::ok) // Se o produto for encontrado (isPresent), retorna 200 OK e o produto
                .orElseGet(() -> ResponseEntity.notFound().build()); // Se não, retorna 404 Not Found
    }

    // 4. Atualizar um Produto existente (Método PUT)
    @PutMapping("/{id}") // Mapeia requisições HTTP PUT para este método
    public ResponseEntity<Produto> atualizarProduto(@PathVariable Long id, @RequestBody Produto produtoDetalhes) {
        Produto produtoAtualizado = produtoService.atualizarProduto(id, produtoDetalhes);
        if (produtoAtualizado != null) {
            return ResponseEntity.ok(produtoAtualizado); // Retorna 200 OK e o produto atualizado
        }
        return ResponseEntity.notFound().build(); // Retorna 404 Not Found
    }

    // 5. Deletar um Produto (Método DELETE)
    @DeleteMapping("/{id}") // Anotação: Mapeia requisições HTTP DELETE para este método
    public ResponseEntity<Void> deletarProduto(@PathVariable Long id) {
        boolean deletado = produtoService.deletarProduto(id);
        if (deletado) {
            return ResponseEntity.noContent().build(); // Retorna 204 No Content (sucesso, mas sem corpo na resposta)
        }
        return ResponseEntity.notFound().build(); // Retorna 404 Not Found
    }
}
