package com.medeiros.sigam_api.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Table(name = "tb_produto")
@Data
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;

    @Column(length = 500)
    private String descricao;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precoVenda;

    @Column(precision = 10, scale = 2)
    private BigDecimal precoCusto;

    @Column(nullable = false)
    private Integer estoqueAtual;

    @Column
    private Integer estoqueMinimo;

    @Column
    private String categoria;

    @Column(unique = true)
    private String codigoBarras;

    @Column(nullable = false)
    private Boolean ativo;

    public Produto() {
        this.ativo = true;
        this.estoqueAtual = 0;
        this.precoVenda = BigDecimal.ZERO;
        this.precoCusto = BigDecimal.ZERO;
    }
}