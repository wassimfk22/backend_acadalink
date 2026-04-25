package com.acadlink.repository;

import com.acadlink.entity.ExperienceProf;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExperienceProfRepository extends JpaRepository<ExperienceProf, Long> {
    List<ExperienceProf> findByEnseignantId(Long enseignantId);
}
