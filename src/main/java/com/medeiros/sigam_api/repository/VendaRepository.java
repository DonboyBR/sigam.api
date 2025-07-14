package com.medeiros.sigam_api.repository;

import com.medeiros.sigam_api.model.Venda; // Importa a entidade Venda
import org.springframework.data.jpa.repository.JpaRepository; // Importa a interface JpaRepository
import org.springframework.stereotype.Repository; // Opcional, mas boa prática

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {

}
    