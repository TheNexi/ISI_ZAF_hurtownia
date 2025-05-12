package com.example.hurtownia_ISI_ZAF.repository;

import com.example.hurtownia_ISI_ZAF.model.Uzytkownicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UzytkownicyRepository extends JpaRepository<Uzytkownicy, Long> {

    Optional<Uzytkownicy> findByLogin(String login);

    Optional<Uzytkownicy> findByEmail(String email);

    List<Uzytkownicy> findByRole(String role);
}

