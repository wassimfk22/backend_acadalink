package com.acadlink.repository;

import com.acadlink.entity.Competence;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CompetenceRepository extends JpaRepository<Competence, Long> {
    List<Competence> findByEtudiantId(Long etudiantId);
}
