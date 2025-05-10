package com.example.hurtownia_ISI_ZAF.repository;

import com.example.hurtownia_ISI_ZAF.model.Dostawca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DostawcaRepository extends JpaRepository<Dostawca, Integer> {
}
