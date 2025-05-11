package com.example.hurtownia_ISI_ZAF.repository;

import com.example.hurtownia_ISI_ZAF.model.ProduktWZamowieniu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProduktWZamowieniuRepository extends JpaRepository<ProduktWZamowieniu, Integer> {
}
