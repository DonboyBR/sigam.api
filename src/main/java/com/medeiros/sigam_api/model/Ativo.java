package com.medeiros.sigam_api.model; // Seu pacote base

import jakarta.persistence.*; // Importa as anotações do JPA
import lombok.Data; // Importa a anotação do Lombok (se você adicionou Lombok)
import java.time.LocalDateTime;

@Table(name = "tb_ativo")
@Data
@Entity
public class Ativo{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column (nullable = false)
    private String nome;

    @Column (nullable = false)
    private String LocalFisico;

    @Column
    private String departamento;

    @Column
    private String marca;

    @Column
    private String modelo;

    @Column (nullable = false)
    private LocalDateTime dataRegistro;

    public Ativo () {
        this.dataRegistro = LocalDateTime.now();
    }
}