package com.acadlink.controller;

import com.acadlink.dto.RechercheDtoRequest;
import com.acadlink.dto.RechercheDtoResponse;
import com.acadlink.entity.Chercheur;
import com.acadlink.entity.Recherche;
import com.acadlink.entity.Utilisateur;
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
        return ResponseEntity.ok(rechercheRepository.findByChercheurId(userId).stream().map(this::toDto).toList());
    }

    @GetMapping("/search")
    public ResponseEntity<List<RechercheDtoResponse>> search(@RequestParam String q) {
        String query = q == null ? "" : q.trim().toLowerCase();
        return ResponseEntity.ok(
                rechercheRepository.findAll().stream()
                        .filter(r -> (r.getTitre() != null && r.getTitre().toLowerCase().contains(query))
                                || (r.getDomaine() != null && r.getDomaine().toLowerCase().contains(query))
                                || (r.getDescription() != null && r.getDescription().toLowerCase().contains(query))
                                || (r.getProbleme() != null && r.getProbleme().toLowerCase().contains(query)))
                        .map(this::toDto)
                        .toList()
        );
    }

    @PostMapping
    public ResponseEntity<RechercheDtoResponse> create(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody RechercheDtoRequest dto
    ) {
        Utilisateur baseUser = utilisateurRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        if (baseUser.getRoleUtilisateur() != Role.CHERCHEUR || !(baseUser instanceof Chercheur chercheur)) {
            throw new RuntimeException("Seul un chercheur peut publier une recherche");
        }
        Recherche recherche = Recherche.builder()
                .titre(dto.getTitre())
                .domaine(dto.getDomaine())
                .description(dto.getDescription())
                .probleme(dto.getProbleme())
                .conclusion(dto.getConclusion())
                .source(dto.getSource())
                .ecole(dto.getEcole())
                .duree(dto.getDuree())
                .chercheur(chercheur)
                .build();
        return ResponseEntity.ok(toDto(rechercheRepository.save(recherche)));
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
                .auteurId(r.getChercheur().getId())
                .auteurNom(r.getChercheur().getNomComplet())
                .auteurPhotoUrl(r.getChercheur().getPhotoUrl())
                .typePublication("RECHERCHE")
                .build();
    }
}
