package com.medeiros.sigam_api.service;

import com.medeiros.sigam_api.model.Produto; // Importa a entidade Produto
import com.medeiros.sigam_api.repository.ProdutoRepository; // Importa o repositório do Produto
import org.springframework.beans.factory.annotation.Autowired; // Para injeção de dependência
import org.springframework.stereotype.Service; // Marca a classe como um serviço
import java.util.List; // Para retornar listas de produtos
import java.util.Optional; // Para lidar com produtos que podem não existir

@Service // Anotação: Marca esta classe como um componente de serviço do Spring
public class ProdutoService {

    private final ProdutoRepository produtoRepository; // O repositório que vamos usar

    // Chama o repositório Produto, instancia dentro da variável produto
    @Autowired
    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    // Método para criar/salvar um novo Produto
    public Produto criarProduto(Produto produto) {

        return produtoRepository.save(produto); // método save() do JpaRepository
    }

    // Método para buscar todos os Produtos
    public List<Produto> buscarTodosProdutos() {
        return produtoRepository.findAll(); // método findAll() do JpaRepository
    }

    // Método para buscar um Produto por ID
    public Optional<Produto> buscarProdutoPorId(Long id) {
        return produtoRepository.findById(id); // método findById() do JpaRepository
    }

    // Método para atualizar um Produto existente
    public Produto atualizarProduto(Long id, Produto produtoDetalhes) {
        Optional<Produto> produtoExistente = produtoRepository.findById(id);

        if (produtoExistente.isPresent()) {
            Produto produto = produtoExistente.get();
            // Atualiza os campos que podem ser modificados
            produto.setNome(produtoDetalhes.getNome());
            produto.setDescricao(produtoDetalhes.getDescricao());
            produto.setPrecoVenda(produtoDetalhes.getPrecoVenda());
            produto.setPrecoCusto(produtoDetalhes.getPrecoCusto());
            produto.setEstoqueAtual(produtoDetalhes.getEstoqueAtual());
            produto.setEstoqueMinimo(produtoDetalhes.getEstoqueMinimo());
            produto.setCategoria(produtoDetalhes.getCategoria());
            produto.setCodigoBarras(produtoDetalhes.getCodigoBarras());
            produto.setAtivo(produtoDetalhes.getAtivo());

            return produtoRepository.save(produto); // Salva as alterações
        } else {
            // Caso o produto não seja encontrado, será lançada uma exceção ou retornar null/Optional.empty()
            return null;
        }
    }

    // Método para deletar um Produto por ID
    public boolean deletarProduto(Long id) {
        if (produtoRepository.existsById(id)) { // Verifica se o produto existe antes de deletar
            produtoRepository.deleteById(id); // Usa o método deleteById() do JpaRepository
            return true;
        }
        return false;
    }
}
