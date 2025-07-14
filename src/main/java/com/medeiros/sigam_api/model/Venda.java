package com.medeiros.sigam_api.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List; // Para a lista de itens de venda

@Entity
@Table(name = "tb_venda")
@Data
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) // Não podem ser nulos!
    private LocalDateTime dataHoraVenda; // Data e hora exata da venda

    @Column(nullable = false, precision = 10, scale = 2) // Não podem ser nulos!
    private BigDecimal valorTotal; // Soma total da venda

    @Column(nullable = false)// Não podem ser nulos!
    private String metodoPagamento; // "CREDITO", "DEBITO", "PIX", "DINHEIRO", "ONLINE"

    @Column
    private String bandeiraCartao; // "VISA", "MASTERCARD" (opcional, se for cartão)

    @Column
    private String referenciaPagamento; // ID da transação na maquininha/PIX (NSU, código de autorização)

    @Column(nullable = false)
    private String statusVenda; // Ex: "CONCLUIDA", "CANCELADA"

    @Column
    private String atendenteResponsavel; // Nome do atendente que realizou a venda

    // Uma Venda pode ter MÚLTIPLOS ItemVenda (um para cada produto vendido)
    // mappedBy indica que o mapeamento é feito pelo campo 'venda' na classe ItemVenda
    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemVenda> itens; // Lista de produtos vendidos nesta venda


    public Venda() {
        this.dataHoraVenda = LocalDateTime.now(); // Define a data/hora da venda automaticamente
        this.valorTotal = BigDecimal.ZERO; // Inicializa o valor total como zero
        this.statusVenda = "CONCLUIDA"; // Por padrão, a venda é concluída ao ser criada
    }
}