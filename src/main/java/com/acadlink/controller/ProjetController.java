package com.acadlink.controller;

import com.acadlink.dto.ProjetDtoRequest;
import com.acadlink.dto.ProjetDtoResponse;
import com.acadlink.entity.*;
import com.acadlink.enums.Role;
import com.acadlink.repository.ProjetRepository;
import com.acadlink.repository.UtilisateurRepository;
import com.acadlink.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projets")
@RequiredArgsConstructor
public class ProjetController {

    private final ProjetRepository projetRepository;
    private final UtilisateurRepository utilisateurRepository;

    @GetMapping
    public ResponseEntity<List<ProjetDtoResponse>> getAll() {
        return ResponseEntity.ok(projetRepository.findAll().stream().map(this::toDto).toList());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ProjetDtoResponse>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(projetRepository.findByEtudiantId(userId).stream().map(this::toDto).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjetDtoResponse> getById(@PathVariable Long id) {
        Projet p = projetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Projet non trouvé"));
        return ResponseEntity.ok(toDto(p));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProjetDtoResponse>> search(@RequestParam String q) {
        String query = q == null ? "" : q.trim().toLowerCase();
        return ResponseEntity.ok(
                projetRepository.findAll().stream()
                        .filter(p -> matches(p.getTitre(), query)
                                || matches(p.getDomaine(), query)
                                || matches(p.getDescription(), query))
                        .map(this::toDto)
                        .toList()
        );
    }

    @PostMapping
    public ResponseEntity<ProjetDtoResponse> create(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody ProjetDtoRequest dto) {

        Utilisateur baseUser = utilisateurRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        if (baseUser.getRoleUtilisateur() != Role.ETUDIANT || !(baseUser instanceof Etudiant etudiant)) {
            throw new RuntimeException("Seul un étudiant peut publier un projet");
        }

        // AJOUT : résolution de l'enseignant collaborateur si fourni
        Enseignant collaborateur = null;
        if (dto.getEnseignantCollaborateurId() != null) {
            Utilisateur ensUser = utilisateurRepository.findById(dto.getEnseignantCollaborateurId())
                    .orElseThrow(() -> new RuntimeException("Enseignant non trouvé"));
            if (!(ensUser instanceof Enseignant ens)) {
                throw new RuntimeException("L'utilisateur spécifié n'est pas un enseignant");
            }
            collaborateur = ens;
        }

        Projet projet = Projet.builder()
                .titre(dto.getTitre())
                .domaine(dto.getDomaine())
                .description(dto.getDescription())
                .sujet(dto.getSujet())
                .tuteur(dto.getTuteur())
                .duree(dto.getDuree())
                .apprentissage(dto.getApprentissage())
                .etudiant(etudiant)
                .enseignantCollaborateur(collaborateur) // AJOUT
                .build();
        return ResponseEntity.ok(toDto(projetRepository.save(projet)));
    }

    /**
     * AJOUT : assigner ou retirer un enseignant collaborateur sur un projet existant.
     * PUT /api/projets/{id}/collaborateur?enseignantId=5   → assigne
     * PUT /api/projets/{id}/collaborateur                  → retire
     */
    @PutMapping("/{id}/collaborateur")
    public ResponseEntity<ProjetDtoResponse> setCollaborateur(
            @PathVariable Long id,
            @RequestParam(required = false) Long enseignantId,
            @AuthenticationPrincipal CustomUserDetails user) {

        Projet projet = projetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Projet non trouvé"));
        if (!projet.getEtudiant().getId().equals(user.getId())) {
            throw new RuntimeException("Vous ne pouvez modifier que vos propres projets");
        }

        if (enseignantId == null) {
            projet.setEnseignantCollaborateur(null);
        } else {
            Utilisateur ensUser = utilisateurRepository.findById(enseignantId)
                    .orElseThrow(() -> new RuntimeException("Enseignant non trouvé"));
            if (!(ensUser instanceof Enseignant ens)) {
                throw new RuntimeException("L'utilisateur spécifié n'est pas un enseignant");
            }
            projet.setEnseignantCollaborateur(ens);
        }
        return ResponseEntity.ok(toDto(projetRepository.save(projet)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id,
                                        @AuthenticationPrincipal CustomUserDetails user) {
        Projet p = projetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Projet non trouvé"));
        if (!p.getEtudiant().getId().equals(user.getId())) {
            throw new RuntimeException("Vous ne pouvez supprimer que vos propres projets");
        }
        projetRepository.delete(p);
        return ResponseEntity.ok().build();
    }

    private boolean matches(String field, String query) {
        return field != null && field.toLowerCase().contains(query);
    }

    private ProjetDtoResponse toDto(Projet p) {
        return ProjetDtoResponse.builder()
                .id(p.getId())
                .titre(p.getTitre())
                .domaine(p.getDomaine())
                .description(p.getDescription())
                .sujet(p.getSujet())
                .tuteur(p.getTuteur())
                .duree(p.getDuree())
                .apprentissage(p.getApprentissage())
                .auteurId(p.getEtudiant().getId())
                .auteurNom(p.getEtudiant().getNomComplet())
                .auteurPhotoUrl(p.getEtudiant().getPhotoUrl())
                .typePublication("PROJET")
                // AJOUT : info collaborateur
                .enseignantCollaborateurId(
                        p.getEnseignantCollaborateur() != null ? p.getEnseignantCollaborateur().getId() : null)
                .enseignantCollaborateurNom(
                        p.getEnseignantCollaborateur() != null ? p.getEnseignantCollaborateur().getNomComplet() : null)
                .build();
    }
    
    
    
}