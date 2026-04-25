package com.acadlink.controller;

import com.acadlink.entity.Recherche;
import com.acadlink.security.CustomUserDetails;
import com.acadlink.service.ChercheurService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chercheur")
@PreAuthorize("hasRole('CHERCHEUR')")
@RequiredArgsConstructor
public class ChercheurController {

    private final ChercheurService chercheurService;

    @GetMapping("/recherches")
    public ResponseEntity<List<Recherche>> getRecherches(@AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(chercheurService.getRecherches(user.getId()));
    }

    @PostMapping("/recherches")
    public ResponseEntity<Recherche> addRecherche(@AuthenticationPrincipal CustomUserDetails user,
                                                   @RequestBody Recherche r) {
        return ResponseEntity.ok(chercheurService.addRecherche(user.getId(), r));
    }

    @DeleteMapping("/recherches/{id}")
    public ResponseEntity<Void> deleteRecherche(@PathVariable Long id) {
        chercheurService.deleteRecherche(id);
        return ResponseEntity.ok().build();
    }
}
