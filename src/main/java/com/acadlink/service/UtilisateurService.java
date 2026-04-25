package com.acadlink.service;

import com.acadlink.dto.UtilisateurDTO;
import com.acadlink.entity.Utilisateur;
import com.acadlink.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;

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

    public List<UtilisateurDTO> search(String query) {
        return utilisateurRepository
                .findByNomCompletContainingIgnoreCaseOrTitreContainingIgnoreCase(query, query)
                .stream().filter(Utilisateur::isActif).map(this::toDTO).toList();
    }

    public List<UtilisateurDTO> getAllUsers() {
        return utilisateurRepository.findAll().stream().map(this::toDTO).toList();
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

    public UtilisateurDTO toDTO(Utilisateur u) {
        return UtilisateurDTO.builder()
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
