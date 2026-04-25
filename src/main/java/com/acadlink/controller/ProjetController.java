package com.acadlink.controller;

import com.acadlink.dto.ProjetDtoRequest;
import com.acadlink.dto.ProjetDtoResponse;
import com.acadlink.entity.Etudiant;
import com.acadlink.entity.Projet;
import com.acadlink.entity.Utilisateur;
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

    @GetMapping("/search")
    public ResponseEntity<List<ProjetDtoResponse>> search(@RequestParam String q) {
        String query = q == null ? "" : q.trim().toLowerCase();
        return ResponseEntity.ok(
                projetRepository.findAll().stream()
                        .filter(p -> (p.getTitre() != null && p.getTitre().toLowerCase().contains(query))
                                || (p.getDomaine() != null && p.getDomaine().toLowerCase().contains(query))
                                || (p.getDescription() != null && p.getDescription().toLowerCase().contains(query)))
                        .map(this::toDto)
                        .toList()
        );
    }

    @PostMapping
    public ResponseEntity<ProjetDtoResponse> create(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody ProjetDtoRequest dto
    ) {
        Utilisateur baseUser = utilisateurRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        if (baseUser.getRoleUtilisateur() != Role.ETUDIANT || !(baseUser instanceof Etudiant etudiant)) {
            throw new RuntimeException("Seul un étudiant peut publier un projet");
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
                .build();
        return ResponseEntity.ok(toDto(projetRepository.save(projet)));
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
                .build();
    }
}
