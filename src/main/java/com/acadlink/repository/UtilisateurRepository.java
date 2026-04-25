package com.acadlink.repository;

import com.acadlink.entity.Utilisateur;
import com.acadlink.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    Optional<Utilisateur> findByEmail(String email);
    boolean existsByEmail(String email);
    List<Utilisateur> findByActifFalse();
    List<Utilisateur> findByRoleUtilisateur(Role role);
    List<Utilisateur> findByNomCompletContainingIgnoreCaseOrTitreContainingIgnoreCase(String nom, String titre);
}
