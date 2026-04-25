package com.acadlink.repository;

import com.acadlink.entity.Publication;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PublicationRepository extends JpaRepository<Publication, Long> {
    List<Publication> findByAuteurIdOrderByDateDesc(Long auteurId);
    List<Publication> findAllByOrderByDateDesc();
    List<Publication> findByDomaineContainingIgnoreCaseOrTitreContainingIgnoreCase(String domaine, String titre);
}
