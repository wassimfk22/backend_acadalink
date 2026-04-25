package com.acadlink.service;

import com.acadlink.dto.PublicationDtoRequest;
import com.acadlink.dto.PublicationDtoResponse;
import com.acadlink.dto.SimpleUserDTO;
import com.acadlink.entity.*;
import com.acadlink.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicationService {

    private final PublicationRepository publicationRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final LikeRepository likeRepository;
    private final CommentaireRepository commentaireRepository;
    private final NotificationService notificationService;

    public List<PublicationDtoResponse> getFeed(Long currentUserId) {
        return publicationRepository.findAllByOrderByDateDesc()
                .stream().map(p -> toDTO(p, currentUserId)).toList();
    }

    public List<PublicationDtoResponse> getByUser(Long userId, Long currentUserId) {
        return publicationRepository.findByAuteurIdOrderByDateDesc(userId)
                .stream().map(p -> toDTO(p, currentUserId)).toList();
    }

    public PublicationDtoResponse getById(Long id, Long currentUserId) {
        Publication p = publicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Publication non trouvée"));
        return toDTO(p, currentUserId);
    }

    @Transactional
    public PublicationDtoResponse create(Long auteurId, PublicationDtoRequest dto) {
        Utilisateur auteur = utilisateurRepository.findById(auteurId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        Publication p = Publication.builder()
                .titre(dto.getTitre())
                .domaine(dto.getDomaine())
                .description(dto.getDescription())
                .imageUrl(dto.getImageUrl())
                .date(LocalDateTime.now())
                .auteur(auteur)
                .build();
        return toDTO(publicationRepository.save(p), auteurId);
    }

    @Transactional
    public void delete(Long publicationId, Long userId) {
        Publication p = publicationRepository.findById(publicationId)
                .orElseThrow(() -> new RuntimeException("Publication non trouvée"));
        // userId null = admin deletion (bypass owner check)
        if (userId != null && !p.getAuteur().getId().equals(userId)) {
            throw new RuntimeException("Vous ne pouvez supprimer que vos propres publications");
        }
        publicationRepository.delete(p); // cascade supprime likes et commentaires
    }

    @Transactional
    public void toggleLike(Long publicationId, Long userId) {
        Publication pub = publicationRepository.findById(publicationId)
                .orElseThrow(() -> new RuntimeException("Publication non trouvée"));
        Utilisateur user = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        var existing = likeRepository.findByPublicationIdAndUtilisateurId(publicationId, userId);
        if (existing.isPresent()) {
            likeRepository.delete(existing.get());
        } else {
            likeRepository.save(Like.builder().publication(pub).utilisateur(user).build());
            if (!pub.getAuteur().getId().equals(userId)) {
                notificationService.create(pub.getAuteur().getId(),
                        user.getNomComplet() + " a aimé votre publication \"" + pub.getTitre() + "\"");
            }
        }
    }

    public List<PublicationDtoResponse> search(String query, Long currentUserId) {
        return publicationRepository.findByDomaineContainingIgnoreCaseOrTitreContainingIgnoreCase(query, query)
                .stream().map(p -> toDTO(p, currentUserId)).toList();
    }

    public List<SimpleUserDTO> getLikers(Long publicationId) {
        return likeRepository.findByPublicationId(publicationId).stream()
                .map(like -> SimpleUserDTO.builder()
                        .id(like.getUtilisateur().getId())
                        .nomComplet(like.getUtilisateur().getNomComplet())
                        .photoUrl(like.getUtilisateur().getPhotoUrl())
                        .build())
                .toList();
    }

    private PublicationDtoResponse toDTO(Publication p, Long currentUserId) {
        return PublicationDtoResponse.builder()
                .id(p.getId())
                .titre(p.getTitre())
                .domaine(p.getDomaine())
                .description(p.getDescription())
                .imageUrl(p.getImageUrl())
                .date(p.getDate())
                .auteurId(p.getAuteur().getId())
                .auteurNom(p.getAuteur().getNomComplet())
                .auteurPhotoUrl(p.getAuteur().getPhotoUrl())
                .likesCount(likeRepository.countByPublicationId(p.getId()))
                .commentairesCount(p.getCommentaires().size())
                .likedByCurrentUser(currentUserId != null && likeRepository.existsByPublicationIdAndUtilisateurId(p.getId(), currentUserId))
                .build();
    }
}
