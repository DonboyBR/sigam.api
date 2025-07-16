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

    private final FechamentoCaixaRepository fechamentoCaixaRepository;
    private final VendaRepository vendaRepository;

    @Autowired
    public FechamentoCaixaService(FechamentoCaixaRepository fechamentoCaixaRepository, VendaRepository vendaRepository) {
        this.fechamentoCaixaRepository = fechamentoCaixaRepository;
        this.vendaRepository = vendaRepository;
    }

    public FechamentoCaixa abrirCaixa(BigDecimal valorInicialTroco, String responsavel) {
        // Verifica se já existe um caixa aberto
        Optional<FechamentoCaixa> caixaAbertoExistente = fechamentoCaixaRepository.findTopByDataHoraFechamentoIsNullOrderByDataHoraAberturaDesc();
        if (caixaAbertoExistente.isPresent()) {
            throw new RuntimeException("Já existe um caixa aberto. Feche o caixa atual antes de abrir um novo.");
        }

        FechamentoCaixa novoCaixa = new FechamentoCaixa();
        novoCaixa.setValorInicialTroco(valorInicialTroco);
        novoCaixa.setResponsavelFechamento(responsavel);
        novoCaixa.setDataHoraAbertura(LocalDateTime.now());
        // Inicializa todos os totais como ZERO na abertura
        novoCaixa.setTotalVendasProdutos(BigDecimal.ZERO);
        novoCaixa.setTotalVendasEVO(BigDecimal.ZERO);
        novoCaixa.setTotalGeralCaixa(BigDecimal.ZERO);
        novoCaixa.setTotalDinheiro(BigDecimal.ZERO);
        novoCaixa.setTotalCredito(BigDecimal.ZERO);
        novoCaixa.setTotalDebito(BigDecimal.ZERO);
        novoCaixa.setTotalPix(BigDecimal.ZERO);
        novoCaixa.setTotalOnline(BigDecimal.ZERO);

        return fechamentoCaixaRepository.save(novoCaixa);
    }

    @Transactional
    public FechamentoCaixa fecharCaixa(Long idCaixa, BigDecimal totalVendasEVO, String responsavel, String observacoes) {
        Optional<FechamentoCaixa> caixaOptional = fechamentoCaixaRepository.findById(idCaixa);

        if (caixaOptional.isEmpty()) {
            throw new RuntimeException("Caixa com ID " + idCaixa + " não encontrado para fechamento.");
        }

        FechamentoCaixa caixa = caixaOptional.get();

        // Verifica se o caixa já está fechado
        if (caixa.getDataHoraFechamento() != null) {
            throw new RuntimeException("Caixa com ID " + idCaixa + " já está fechado.");
        }

        // 1. Define a data e hora do fechamento
        caixa.setDataHoraFechamento(LocalDateTime.now());
        caixa.setResponsavelFechamento(responsavel);
        caixa.setObservacoes(observacoes);

        // 2. Define o total de vendas do EVO (input manual para a demo)
        caixa.setTotalVendasEVO(totalVendasEVO);

        // 3. Busca as vendas de produtos feitas no SIGAM desde a abertura do caixa atual
        List<Venda> vendasDoPeriodo = vendaRepository.findByDataHoraVendaBetween(caixa.getDataHoraAbertura(), caixa.getDataHoraFechamento());

        // 4. Calcula os totais das vendas de produtos do SIGAM por método de pagamento
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
                // Adicione outros métodos de pagamento conforme necessário
            }
        }
        caixa.setTotalVendasProdutos(totalProdutosGeral);

        // 5. Consolida os totais por tipo de pagamento (apenas produtos do SIGAM por enquanto)
        // A soma com os totais do EVO por tipo de pagamento seria feita na integração futura.
        caixa.setTotalDinheiro(totalProdutosDinheiro);
        caixa.setTotalCredito(totalProdutosCredito);
        caixa.setTotalDebito(totalProdutosDebito);
        caixa.setTotalPix(totalProdutosPix);
        caixa.setTotalOnline(totalProdutosOnline);

        // 6. Calcula o total geral do caixa (Troco Inicial + Vendas SIGAM + Vendas EVO)
        caixa.setTotalGeralCaixa(caixa.getValorInicialTroco()
                .add(caixa.getTotalVendasProdutos())
                .add(caixa.getTotalVendasEVO()));

        return fechamentoCaixaRepository.save(caixa);
    }

    public Optional<FechamentoCaixa> buscarFechamentoCaixaPorId(Long id) {
        return fechamentoCaixaRepository.findById(id);
    }

    public List<FechamentoCaixa> buscarTodosFechamentosCaixa() {
        return fechamentoCaixaRepository.findAll();
    }

    // Método para buscar o caixa atualmente aberto
    public Optional<FechamentoCaixa> buscarCaixaAberto() {
        return fechamentoCaixaRepository.findTopByDataHoraFechamentoIsNullOrderByDataHoraAberturaDesc();
    }
}