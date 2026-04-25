package com.acadlink.repository;

import com.acadlink.entity.Formation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FormationRepository extends JpaRepository<Formation, Long> {
    List<Formation> findByEtudiantId(Long etudiantId);
}
