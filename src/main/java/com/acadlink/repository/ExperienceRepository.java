package com.acadlink.repository;

import com.acadlink.entity.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExperienceRepository extends JpaRepository<Experience, Long> {
    List<Experience> findByEtudiantId(Long etudiantId);
}
