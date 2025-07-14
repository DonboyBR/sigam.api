package com.medeiros.sigam_api.repository;

import com.medeiros.sigam_api.model.FechamentoCaixa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FechamentoCaixaRepository extends JpaRepository<FechamentoCaixa, Long> {

}
    