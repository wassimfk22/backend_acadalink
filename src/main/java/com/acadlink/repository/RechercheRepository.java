package com.acadlink.repository;

import com.acadlink.entity.Recherche;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RechercheRepository extends JpaRepository<Recherche, Long> {
    List<Recherche> findByChercheurId(Long chercheurId);
}
