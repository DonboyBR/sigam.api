package com.medeiros.sigam_api.repository;

import com.medeiros.sigam_api.model.FechamentoCaixa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional; // Importa Optional para o retorno do método

@Repository // Anotação: Indica que esta interface é um componente de repositório gerenciado pelo Spring
public interface FechamentoCaixaRepository extends JpaRepository<FechamentoCaixa, Long> {

    Optional<FechamentoCaixa> findTopByDataHoraFechamentoIsNullOrderByDataHoraAberturaDesc();
}