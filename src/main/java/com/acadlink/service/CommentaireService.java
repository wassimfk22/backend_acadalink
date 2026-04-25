package com.acadlink.service;

import com.acadlink.dto.CommentaireDTO;
import com.acadlink.entity.*;
import com.acadlink.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentaireService {

    private final CommentaireRepository commentaireRepository;
    private final PublicationRepository publicationRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final NotificationService notificationService;

    public List<CommentaireDTO> getByPublication(Long publicationId) {
        return commentaireRepository.findByPublicationIdOrderByDateCommentaireDesc(publicationId)
                .stream().map(this::toDTO).toList();
    }

    @Transactional
    public CommentaireDTO create(Long publicationId, Long userId, String contenu) {
        Publication pub = publicationRepository.findById(publicationId)
                .orElseThrow(() -> new RuntimeException("Publication non trouvée"));
        Utilisateur user = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Commentaire c = Commentaire.builder()
                .commentaire(contenu)
                .dateCommentaire(LocalDateTime.now())
                .publication(pub)
                .utilisateur(user)
                .build();

        Commentaire saved = commentaireRepository.save(c);

        if (!pub.getAuteur().getId().equals(userId)) {
            notificationService.create(pub.getAuteur().getId(),
                    user.getNomComplet() + " a commenté votre publication \"" + pub.getTitre() + "\"");
        }

        return toDTO(saved);
    }

    @Transactional
    public void delete(Long commentaireId, Long userId) {
        Commentaire c = commentaireRepository.findById(commentaireId)
                .orElseThrow(() -> new RuntimeException("Commentaire non trouvé"));
        if (!c.getUtilisateur().getId().equals(userId)) {
            throw new RuntimeException("Vous ne pouvez supprimer que vos propres commentaires");
        }
        commentaireRepository.delete(c);
    }

    private CommentaireDTO toDTO(Commentaire c) {
        return CommentaireDTO.builder()
                .id(c.getId())
                .commentaire(c.getCommentaire())
                .dateCommentaire(c.getDateCommentaire())
                .utilisateurId(c.getUtilisateur().getId())
                .nomUser(c.getUtilisateur().getNomComplet())
                .imageUser(c.getUtilisateur().getPhotoUrl())
                .build();
    }
}
