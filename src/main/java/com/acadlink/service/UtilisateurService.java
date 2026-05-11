package com.acadlink.service;

import com.acadlink.dto.UtilisateurDTO;
import com.acadlink.entity.Utilisateur;
import com.acadlink.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final FileStorageService fileStorageService; // AJOUT

    public UtilisateurDTO getProfile(Long id) {
        Utilisateur u = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return toDTO(u);
    }

    @Transactional
    public UtilisateurDTO updateProfile(Long id, UtilisateurDTO dto) {
        Utilisateur u = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        if (dto.getNomComplet() != null) u.setNomComplet(dto.getNomComplet());
        if (dto.getDescription() != null) u.setDescription(dto.getDescription());
        if (dto.getTitre() != null) u.setTitre(dto.getTitre());
        if (dto.getPhotoUrl() != null) u.setPhotoUrl(dto.getPhotoUrl());
        return toDTO(utilisateurRepository.save(u));
    }

    /**
     * AJOUT : Upload et sauvegarde de la photo de profil.
     * Supprime l'ancienne photo si elle existe.
     */
    @Transactional
    public UtilisateurDTO updatePhoto(Long id, MultipartFile file) {
        Utilisateur u = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Supprime l'ancienne photo
        if (u.getPhotoUrl() != null) {
            fileStorageService.delete(u.getPhotoUrl());
        }

        String photoUrl = fileStorageService.store(file, "profiles");
        u.setPhotoUrl(photoUrl);
        return toDTO(utilisateurRepository.save(u));
    }

    public List<UtilisateurDTO> search(String query) {
        return utilisateurRepository
                .findByNomCompletContainingIgnoreCaseOrTitreContainingIgnoreCase(query, query)
                .stream().filter(Utilisateur::isActif).map(this::toDTO).toList();
    }

    public List<UtilisateurDTO> getAllUsers() {
        return utilisateurRepository.findAll().stream().map(this::toDTO).toList();
    }

    // AJOUT : liste publique — seulement les comptes actifs, sans email ni password exposé
    public List<UtilisateurDTO> getAllActiveUsers() {
        return utilisateurRepository.findAll().stream()
                .filter(Utilisateur::isActif)
                .map(this::toDTO)
                .toList();
    }

    public List<UtilisateurDTO> getPendingUsers() {
        return utilisateurRepository.findByActifFalse().stream().map(this::toDTO).toList();
    }

    @Transactional
    public void activateUser(Long id) {
        Utilisateur u = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        u.setActif(true);
        utilisateurRepository.save(u);
    }

    @Transactional
    public void deleteUser(Long id) {
        utilisateurRepository.deleteById(id);
    }

    // CORRECTION : id ajouté dans le DTO
    public UtilisateurDTO toDTO(Utilisateur u) {
        return UtilisateurDTO.builder()
                .id(u.getId())           // ← WAS MISSING
                .nomComplet(u.getNomComplet())
                .email(u.getEmail())
                .photoUrl(u.getPhotoUrl())
                .description(u.getDescription())
                .titre(u.getTitre())
                .roleUtilisateur(u.getRoleUtilisateur())
                .actif(u.isActif())
                .build();
    }
}