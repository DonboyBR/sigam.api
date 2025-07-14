package com.medeiros.sigam_api.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Table(name = "tb_item_venda")
@Data
public class ItemVenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // Muitos ItemVenda para Um Produto
    @JoinColumn(name = "produto_id", nullable = false) // Coluna que liga a ItemVenda ao Produto
    private Produto produto; // Qual produto foi vendido

    @ManyToOne // Muitos ItemVenda para Uma Venda
    @JoinColumn(name = "venda_id", nullable = false) // Coluna que liga a ItemVenda à Venda
    private Venda venda; // A qual venda este item pertence

    @Column(nullable = false)
    private Integer quantidade; // Quantidade do produto vendida

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precoUnitarioVenda; // Preço do produto no momento da venda (pode variar por promoções)

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal; // Quantidade * precoUnitarioVenda

    public ItemVenda() {
        this.quantidade = 0;
        this.precoUnitarioVenda = BigDecimal.ZERO;
        this.subtotal = BigDecimal.ZERO;
    }
}
