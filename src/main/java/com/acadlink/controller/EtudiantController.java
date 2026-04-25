package com.acadlink.controller;

import com.acadlink.entity.*;
import com.acadlink.security.CustomUserDetails;
import com.acadlink.service.EtudiantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/etudiant")
@PreAuthorize("hasRole('ETUDIANT')")
@RequiredArgsConstructor
public class EtudiantController {

    private final EtudiantService etudiantService;

    // Compétences
    @GetMapping("/competences")
    public ResponseEntity<List<Competence>> getCompetences(@AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(etudiantService.getCompetences(user.getId()));
    }
    @PostMapping("/competences")
    public ResponseEntity<Competence> addCompetence(@AuthenticationPrincipal CustomUserDetails user, @RequestBody Competence c) {
        return ResponseEntity.ok(etudiantService.addCompetence(user.getId(), c));
    }
    @DeleteMapping("/competences/{id}")
    public ResponseEntity<Void> deleteCompetence(@PathVariable Long id) {
        etudiantService.deleteCompetence(id); return ResponseEntity.ok().build();
    }

    // Formations
    @GetMapping("/formations")
    public ResponseEntity<List<Formation>> getFormations(@AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(etudiantService.getFormations(user.getId()));
    }
    @PostMapping("/formations")
    public ResponseEntity<Formation> addFormation(@AuthenticationPrincipal CustomUserDetails user, @RequestBody Formation f) {
        return ResponseEntity.ok(etudiantService.addFormation(user.getId(), f));
    }
    @DeleteMapping("/formations/{id}")
    public ResponseEntity<Void> deleteFormation(@PathVariable Long id) {
        etudiantService.deleteFormation(id); return ResponseEntity.ok().build();
    }

    // Projets
    @GetMapping("/projets")
    public ResponseEntity<List<Projet>> getProjets(@AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(etudiantService.getProjets(user.getId()));
    }
    @PostMapping("/projets")
    public ResponseEntity<Projet> addProjet(@AuthenticationPrincipal CustomUserDetails user, @RequestBody Projet p) {
        return ResponseEntity.ok(etudiantService.addProjet(user.getId(), p));
    }
    @DeleteMapping("/projets/{id}")
    public ResponseEntity<Void> deleteProjet(@PathVariable Long id) {
        etudiantService.deleteProjet(id); return ResponseEntity.ok().build();
    }

    // Expériences
    @GetMapping("/experiences")
    public ResponseEntity<List<Experience>> getExperiences(@AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(etudiantService.getExperiences(user.getId()));
    }
    @PostMapping("/experiences")
    public ResponseEntity<Experience> addExperience(@AuthenticationPrincipal CustomUserDetails user, @RequestBody Experience e) {
        return ResponseEntity.ok(etudiantService.addExperience(user.getId(), e));
    }
    @DeleteMapping("/experiences/{id}")
    public ResponseEntity<Void> deleteExperience(@PathVariable Long id) {
        etudiantService.deleteExperience(id); return ResponseEntity.ok().build();
    }
}
