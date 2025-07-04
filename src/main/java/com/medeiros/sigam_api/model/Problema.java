package com.medeiros.sigam_api.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Table(name = "tb_problema")
@Entity


public class Problema {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column (nullable = false)
    private String descricao;

    @Column (nullable = false)
    private String tipoProblema;

    @Column (nullable = false)
    private LocalDateTime dataRegistro;

    @Column
    private String status;

    @Column
    private LocalDateTime dataResolucao;

    @Column(length = 500)
    private String observacoesTecnico;

    @ManyToOne
    @JoinColumn(name = "ativo_id", nullable = false)
    private Ativo ativo;

    public Problema() {
        this.dataRegistro = LocalDateTime.now();
        this.status = "Pendente";
    }
}
