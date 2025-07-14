package com.medeiros.sigam_api.repository;

import com.medeiros.sigam_api.model.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {


    // O Spring Data JPA entende o nome deste método e cria a consulta SQL automaticamente:
    List<Venda> findByDataHoraVendaBetween(LocalDateTime dataInicio, LocalDateTime dataFim);

}