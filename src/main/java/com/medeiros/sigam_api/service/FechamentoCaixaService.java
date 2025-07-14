package com.medeiros.sigam_api.service;

import com.medeiros.sigam_api.model.FechamentoCaixa;
import com.medeiros.sigam_api.model.Venda;
import com.medeiros.sigam_api.repository.FechamentoCaixaRepository;
import com.medeiros.sigam_api.repository.VendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FechamentoCaixaService {

    private final FechamentoCaixaRepository fechamentoCaixaRepository; // Repositório de FechamentoCaixa
    private final VendaRepository vendaRepository; // Repositório de Vendas (para buscar vendas do período)

    @Autowired
    public FechamentoCaixaService(FechamentoCaixaRepository fechamentoCaixaRepository, VendaRepository vendaRepository) {
        this.fechamentoCaixaRepository = fechamentoCaixaRepository;
        this.vendaRepository = vendaRepository;
    }

    // Método para abrir um novo caixa
    public FechamentoCaixa abrirCaixa(BigDecimal valorInicialTroco, String responsavel) {
        FechamentoCaixa novoCaixa = new FechamentoCaixa();
        novoCaixa.setValorInicialTroco(valorInicialTroco);
        novoCaixa.setResponsavelFechamento(responsavel); // O responsável pela abertura, que será o mesmo do fechamento
        novoCaixa.setDataHoraAbertura(LocalDateTime.now());
        // Os totais de vendas serão ZERO na abertura
        return fechamentoCaixaRepository.save(novoCaixa);
    }

    // Método para fechar um caixa existente
    @Transactional // Garante que a operação seja atômica
    public FechamentoCaixa fecharCaixa(Long idCaixa, BigDecimal totalVendasEVO, String responsavel) {
        Optional<FechamentoCaixa> caixaOptional = fechamentoCaixaRepository.findById(idCaixa);

        if (caixaOptional.isEmpty()) {
            throw new RuntimeException("Caixa com ID " + idCaixa + " não encontrado para fechamento.");
        }

        FechamentoCaixa caixa = caixaOptional.get();

        // 1. Define a data e hora do fechamento
        caixa.setDataHoraFechamento(LocalDateTime.now());
        caixa.setResponsavelFechamento(responsavel); // Confirma o responsável pelo fechamento

        // 2. Define o total de vendas do EVO (input manual para a demo)
        caixa.setTotalVendasEVO(totalVendasEVO);

        // 3. Busca as vendas de produtos feitas desde a abertura do caixa
        List<Venda> vendasDoPeriodo = vendaRepository.findByDataHoraVendaBetween(caixa.getDataHoraAbertura(), caixa.getDataHoraFechamento());

        // 4. Calcula os totais das vendas de produtos por método de pagamento
        BigDecimal totalProdutosDinheiro = BigDecimal.ZERO;
        BigDecimal totalProdutosCredito = BigDecimal.ZERO;
        BigDecimal totalProdutosDebito = BigDecimal.ZERO;
        BigDecimal totalProdutosPix = BigDecimal.ZERO;
        BigDecimal totalProdutosOnline = BigDecimal.ZERO;
        BigDecimal totalProdutosGeral = BigDecimal.ZERO;

        for (Venda venda : vendasDoPeriodo) {
            totalProdutosGeral = totalProdutosGeral.add(venda.getValorTotal());
            switch (venda.getMetodoPagamento().toUpperCase()) {
                case "DINHEIRO":
                    totalProdutosDinheiro = totalProdutosDinheiro.add(venda.getValorTotal());
                    break;
                case "CREDITO":
                    totalProdutosCredito = totalProdutosCredito.add(venda.getValorTotal());
                    break;
                case "DEBITO":
                    totalProdutosDebito = totalProdutosDebito.add(venda.getValorTotal());
                    break;
                case "PIX":
                    totalProdutosPix = totalProdutosPix.add(venda.getValorTotal());
                    break;
                case "ONLINE":
                    totalProdutosOnline = totalProdutosOnline.add(venda.getValorTotal());
                    break;

            }
        }
        caixa.setTotalVendasProdutos(totalProdutosGeral);

        // Atualizando os totais por tipo de pagamento do SIGAM (vendas de produtos)
        caixa.setTotalDinheiro(totalProdutosDinheiro);
        caixa.setTotalCredito(totalProdutosCredito);
        caixa.setTotalDebito(totalProdutosDebito);
        caixa.setTotalPix(totalProdutosPix);
        caixa.setTotalOnline(totalProdutosOnline);

        // Calcula o total geral do caixa
        caixa.setTotalGeralCaixa(caixa.getValorInicialTroco()
                .add(caixa.getTotalVendasProdutos())
                .add(caixa.getTotalVendasEVO()));

        return fechamentoCaixaRepository.save(caixa);
    }

    // Método para buscar um FechamentoCaixa por ID
    public Optional<FechamentoCaixa> buscarFechamentoCaixaPorId(Long id) {
        return fechamentoCaixaRepository.findById(id);
    }

    // Método para buscar todos os Fechamentos de Caixa
    public List<FechamentoCaixa> buscarTodosFechamentosCaixa() {
        return fechamentoCaixaRepository.findAll();
    }

    // Método para buscar caixas abertos (para garantir que só um esteja aberto por vez)
    public Optional<FechamentoCaixa> buscarCaixaAberto() {

        return Optional.empty(); // Retorna vazio por enquanto, a lógica será no Controller/Front-End
    }
}
