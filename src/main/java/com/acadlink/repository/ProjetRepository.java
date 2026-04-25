package com.acadlink.repository;

import com.acadlink.entity.Projet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProjetRepository extends JpaRepository<Projet, Long> {
    List<Projet> findByEtudiantId(Long etudiantId);
}
