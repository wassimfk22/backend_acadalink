package com.acadlink.repository;

import com.acadlink.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByPublicationIdAndUtilisateurId(Long publicationId, Long utilisateurId);
    List<Like> findByPublicationId(Long publicationId);
    long countByPublicationId(Long publicationId);
    boolean existsByPublicationIdAndUtilisateurId(Long publicationId, Long utilisateurId);
}
