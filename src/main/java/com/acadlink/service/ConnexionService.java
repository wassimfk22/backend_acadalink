package com.acadlink.service;

import com.acadlink.dto.ConnexionDTO;
import com.acadlink.entity.*;
import com.acadlink.enums.StatutConnexion;
import com.acadlink.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConnexionService {

    private final ConnexionRepository connexionRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final NotificationService notificationService;

    @Transactional
    public ConnexionDTO sendRequest(Long expediteurId, Long destinataireId) {
        if (expediteurId.equals(destinataireId)) {
            throw new RuntimeException("Vous ne pouvez pas vous connecter à vous-même");
        }
        if (connexionRepository.findByExpediteurIdAndDestinataireId(expediteurId, destinataireId).isPresent() ||
            connexionRepository.findByExpediteurIdAndDestinataireId(destinataireId, expediteurId).isPresent()) {
            throw new RuntimeException("Une demande de connexion existe déjà");
        }

        Utilisateur exp = utilisateurRepository.findById(expediteurId).orElseThrow();
        Utilisateur dest = utilisateurRepository.findById(destinataireId).orElseThrow();

        Connexion c = Connexion.builder()
                .statut(StatutConnexion.EN_ATTENTE)
                .expediteur(exp)
                .destinataire(dest)
                .build();

        Connexion saved = connexionRepository.save(c);
        notificationService.create(destinataireId, exp.getNomComplet() + " vous a envoyé une demande de connexion");
        return toDTO(saved);
    }

    @Transactional
    public ConnexionDTO respond(Long connexionId, Long userId, StatutConnexion statut) {
        Connexion c = connexionRepository.findById(connexionId)
                .orElseThrow(() -> new RuntimeException("Demande non trouvée"));
        if (!c.getDestinataire().getId().equals(userId)) {
            throw new RuntimeException("Vous ne pouvez répondre qu'à vos propres demandes");
        }
        c.setStatut(statut);
        Connexion saved = connexionRepository.save(c);

        String msg = statut == StatutConnexion.CONFIRMEE ? " a accepté votre demande de connexion" : " a refusé votre demande de connexion";
        notificationService.create(c.getExpediteur().getId(), c.getDestinataire().getNomComplet() + msg);
        return toDTO(saved);
    }

    public List<ConnexionDTO> getPendingReceived(Long userId) {
        return connexionRepository.findByDestinataireIdAndStatut(userId, StatutConnexion.EN_ATTENTE)
                .stream().map(this::toDTO).toList();
    }

    public List<ConnexionDTO> getPendingSent(Long userId) {
        return connexionRepository.findByExpediteurIdAndStatut(userId, StatutConnexion.EN_ATTENTE)
                .stream().map(this::toDTO).toList();
    }

    public List<ConnexionDTO> getConnections(Long userId) {
        var sent = connexionRepository.findByExpediteurIdAndStatut(userId, StatutConnexion.CONFIRMEE);
        var received = connexionRepository.findByDestinataireIdAndStatut(userId, StatutConnexion.CONFIRMEE);
        sent.addAll(received);
        return sent.stream().map(this::toDTO).toList();
    }

    private ConnexionDTO toDTO(Connexion c) {
        return ConnexionDTO.builder()
                .id(c.getId())
                .statut(c.getStatut())
                .expediteurId(c.getExpediteur().getId())
                .expediteurNom(c.getExpediteur().getNomComplet())
                .expediteurPhotoUrl(c.getExpediteur().getPhotoUrl())
                .destinataireId(c.getDestinataire().getId())
                .destinataireNom(c.getDestinataire().getNomComplet())
                .build();
    }
}
