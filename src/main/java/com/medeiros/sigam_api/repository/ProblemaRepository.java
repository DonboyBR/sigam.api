package com.medeiros.sigam_api.repository;

import com.medeiros.sigam_api.model.Problema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProblemaRepository extends JpaRepository<Problema, Long> {

}
