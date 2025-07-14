package com.medeiros.sigam_api.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_fechamento_caixa")
@Data
public class FechamentoCaixa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dataHoraAbertura; // Quando o caixa foi aberto

    @Column(nullable = false)
    private LocalDateTime dataHoraFechamento; // Quando o caixa foi fechado

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valorInicialTroco; // Valor do fundo de caixa

    // Totais de vendas de PRODUTOS (do seu SIGAM)
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalVendasProdutos; // Soma total das vendas

    // Totais de vendas de PLANOS/DIÁRIAS (do EVO - inicialmente manual)
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalVendasEVO; // Soma total das vendas

    // Total Geral do Caixa (Soma de tudo)
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalGeralCaixa; // valorInicialTroco + totalVendasProdutos + totalVendasEVO

    // Detalhamento por forma de pagamento (consolida SIGAM + EVO)
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalDinheiro;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalCredito;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalDebito;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPix;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalOnline; // Para vendas online, se houver

    @Column(nullable = false)
    private String responsavelFechamento; // Nome do funcionário que fechou o caixa

    @Column(length = 500)
    private String observacoes; // Quaisquer observações sobre o fechamento

    public FechamentoCaixa() {
        this.dataHoraAbertura = LocalDateTime.now(); // Define a data/hora de abertura automaticamente
        this.dataHoraFechamento = LocalDateTime.now(); // Será atualizado no fechamento real
        this.valorInicialTroco = BigDecimal.ZERO;
        this.totalVendasProdutos = BigDecimal.ZERO;
        this.totalVendasEVO = BigDecimal.ZERO;
        this.totalGeralCaixa = BigDecimal.ZERO;
        this.totalDinheiro = BigDecimal.ZERO;
        this.totalCredito = BigDecimal.ZERO;
        this.totalDebito = BigDecimal.ZERO;
        this.totalPix = BigDecimal.ZERO;
        this.totalOnline = BigDecimal.ZERO;
        this.responsavelFechamento = ""; // Será preenchido na hora do fechamento
    }
}