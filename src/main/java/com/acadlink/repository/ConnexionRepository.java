package com.acadlink.repository;

import com.acadlink.entity.Connexion;
import com.acadlink.enums.StatutConnexion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ConnexionRepository extends JpaRepository<Connexion, Long> {
    List<Connexion> findByDestinataireIdAndStatut(Long destinataireId, StatutConnexion statut);
    List<Connexion> findByExpediteurIdAndStatut(Long expediteurId, StatutConnexion statut);
    Optional<Connexion> findByExpediteurIdAndDestinataireId(Long expediteurId, Long destinataireId);
    boolean existsByExpediteurIdAndDestinataireIdAndStatut(Long expediteurId, Long destinataireId, StatutConnexion statut);
}
