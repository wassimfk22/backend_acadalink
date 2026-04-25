package com.acadlink.repository;

import com.acadlink.entity.Commentaire;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentaireRepository extends JpaRepository<Commentaire, Long> {
    List<Commentaire> findByPublicationIdOrderByDateCommentaireDesc(Long publicationId);
}
