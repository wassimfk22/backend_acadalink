package com.acadlink.controller;

import com.acadlink.dto.RechercheDtoRequest;
import com.acadlink.dto.RechercheDtoResponse;
import com.acadlink.entity.*;
import com.acadlink.enums.Role;
import com.acadlink.repository.RechercheRepository;
import com.acadlink.repository.UtilisateurRepository;
import com.acadlink.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recherches")
@RequiredArgsConstructor
public class RechercheController {

    private final RechercheRepository rechercheRepository;
    private final UtilisateurRepository utilisateurRepository;

    @GetMapping
    public ResponseEntity<List<RechercheDtoResponse>> getAll() {
        return ResponseEntity.ok(rechercheRepository.findAll().stream().map(this::toDto).toList());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RechercheDtoResponse>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(rechercheRepository.findByUtilisateurId(userId).stream().map(this::toDto).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RechercheDtoResponse> getById(@PathVariable Long id) {
        Recherche r = rechercheRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recherche non trouvée"));
        return ResponseEntity.ok(toDto(r));
    }

    @GetMapping("/search")
    public ResponseEntity<List<RechercheDtoResponse>> search(@RequestParam String q) {
        String query = q == null ? "" : q.trim().toLowerCase();
        return ResponseEntity.ok(
                rechercheRepository.findAll().stream()
                        .filter(r -> matches(r.getTitre(), query)
                                || matches(r.getDomaine(), query)
                                || matches(r.getDescription(), query)
                                || matches(r.getProbleme(), query))
                        .map(this::toDto)
                        .toList()
        );
    }

    @PostMapping
    public ResponseEntity<RechercheDtoResponse> create(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody RechercheDtoRequest dto) {

        Utilisateur baseUser = utilisateurRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Accepte CHERCHEUR et ENSEIGNANT (les deux peuvent publier des recherches)
        if (baseUser.getRoleUtilisateur() == Role.CHERCHEUR || baseUser.getRoleUtilisateur() == Role.ENSEIGNANT ) {
            Recherche recherche = buildRecherche(dto, baseUser);
            return ResponseEntity.ok(toDto(rechercheRepository.save(recherche)));
        } else {
            throw new RuntimeException("Seul un enseignant ou un chercheur peut publier une recherche");
        }
    }

    // AJOUT : suppression sécurisée par le propriétaire
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id,
                                        @AuthenticationPrincipal CustomUserDetails user) {
        Recherche r = rechercheRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recherche non trouvée"));
        if (!r.getUtilisateur().getId().equals(user.getId())) {
            throw new RuntimeException("Vous ne pouvez supprimer que vos propres recherches");
        }
        rechercheRepository.delete(r);
        return ResponseEntity.ok().build();
    }

    private Recherche buildRecherche(RechercheDtoRequest dto, Utilisateur utilisateur) {
        return Recherche.builder()
                .titre(dto.getTitre())
                .domaine(dto.getDomaine())
                .description(dto.getDescription())
                .probleme(dto.getProbleme())
                .conclusion(dto.getConclusion())
                .source(dto.getSource())
                .ecole(dto.getEcole())
                .duree(dto.getDuree())
                .utilisateur(utilisateur)
                .build();
    }

    private boolean matches(String field, String query) {
        return field != null && field.toLowerCase().contains(query);
    }

    private RechercheDtoResponse toDto(Recherche r) {
        return RechercheDtoResponse.builder()
                .id(r.getId())
                .titre(r.getTitre())
                .domaine(r.getDomaine())
                .description(r.getDescription())
                .probleme(r.getProbleme())
                .conclusion(r.getConclusion())
                .source(r.getSource())
                .ecole(r.getEcole())
                .duree(r.getDuree())
                .auteurId(r.getUtilisateur().getId())
                .auteurNom(r.getUtilisateur().getNomComplet())
                .auteurPhotoUrl(r.getUtilisateur().getPhotoUrl())
                .typePublication("RECHERCHE")
                .build();
    }
    
    
    
}