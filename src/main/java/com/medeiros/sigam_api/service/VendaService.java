package com.medeiros.sigam_api.service;

import com.medeiros.sigam_api.model.ItemVenda;
import com.medeiros.sigam_api.model.Produto;
import com.medeiros.sigam_api.model.Venda;
import com.medeiros.sigam_api.repository.ProdutoRepository;
import com.medeiros.sigam_api.repository.VendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VendaService {

    private final VendaRepository vendaRepository; // Repositório de Vendas
    private final ProdutoRepository produtoRepository; // Repositório de Produtos (para controle de estoque)

    @Autowired
    public VendaService(VendaRepository vendaRepository, ProdutoRepository produtoRepository) {
        this.vendaRepository = vendaRepository;
        this.produtoRepository = produtoRepository;
    }

    // Método para criar/salvar uma nova Venda
    @Transactional // Garante que a operação seja atômica (tudo ou nada)
    public Venda criarVenda(Venda venda) {
        BigDecimal valorTotalCalculado = BigDecimal.ZERO;

        // 1. Processa cada item da venda
        if (venda.getItens() != null && !venda.getItens().isEmpty()) {
            for (ItemVenda item : venda.getItens()) {
                // Busca o produto pelo ID para garantir que ele existe e pegar o estoque atual
                Optional<Produto> produtoOptional = produtoRepository.findById(item.getProduto().getId());

                if (produtoOptional.isEmpty()) {
                    throw new RuntimeException("Produto com ID " + item.getProduto().getId() + " não encontrado.");
                }

                Produto produto = produtoOptional.get();

                // 2. Verifica o estoque
                if (produto.getEstoqueAtual() < item.getQuantidade()) {
                    throw new RuntimeException("Estoque insuficiente para o produto: " + produto.getNome() + ". Disponível: " + produto.getEstoqueAtual());
                }

                // 3. Calcula o subtotal do item
                item.setPrecoUnitarioVenda(produto.getPrecoVenda()); // Garante que o preço de venda é o atual do produto
                BigDecimal subtotalItem = item.getPrecoUnitarioVenda().multiply(BigDecimal.valueOf(item.getQuantidade()));
                item.setSubtotal(subtotalItem);

                // 4. Adiciona o subtotal ao valor total da venda
                valorTotalCalculado = valorTotalCalculado.add(subtotalItem);

                // 5. Atualiza o estoque do produto
                produto.setEstoqueAtual(produto.getEstoqueAtual() - item.getQuantidade());
                produtoRepository.save(produto); // Salva a atualização do estoque
            }
        } else {
            throw new RuntimeException("A venda deve conter pelo menos um item.");
        }

        // 6. Define o valor total da venda
        venda.setValorTotal(valorTotalCalculado);

        // 7. Garante o relacionamento bidirecional entre Venda e ItemVenda
        venda.getItens().forEach(item -> item.setVenda(venda));

        // 8. Salva a venda e seus itens (devido ao CascadeType.ALL na Venda)
        return vendaRepository.save(venda);
    }

    // Método para buscar todas as Vendas
    public List<Venda> buscarTodasVendas() {
        return vendaRepository.findAll();
    }

    // Método para buscar uma Venda por ID
    public Optional<Venda> buscarVendaPorId(Long id) {
        return vendaRepository.findById(id);
    }

    // Método para deletar uma Venda por ID
    @Transactional // Garante que a operação de deleção e retorno de estoque seja atômica
    public boolean deletarVenda(Long id) {
        Optional<Venda> vendaOptional = vendaRepository.findById(id);

        if (vendaOptional.isPresent()) {
            Venda venda = vendaOptional.get();

            // Retorna os itens da venda para o estoque antes de deletar
            for (ItemVenda item : venda.getItens()) {
                Optional<Produto> produtoOptional = produtoRepository.findById(item.getProduto().getId());
                if (produtoOptional.isPresent()) {
                    Produto produto = produtoOptional.get();
                    produto.setEstoqueAtual(produto.getEstoqueAtual() + item.getQuantidade());
                    produtoRepository.save(produto); // Salva a atualização do estoque
                }
            }
            vendaRepository.delete(venda); // Deleta a venda e seus itens (devido ao CascadeType.ALL)
            return true;
        }
        return false;
    }

    // Método para buscar vendas por método de pagamento (útil para FechamentoCaixa)
    public List<Venda> buscarVendasPorMetodoPagamento(String metodoPagamento, LocalDateTime dataInicio, LocalDateTime dataFim) {

        // buscar vendas em um período e por método
        return vendaRepository.findAll().stream()
                .filter(v -> v.getMetodoPagamento().equalsIgnoreCase(metodoPagamento) &&
                        v.getDataHoraVenda().isAfter(dataInicio) &&
                        v.getDataHoraVenda().isBefore(dataFim))
                .collect(Collectors.toList());
    }
}