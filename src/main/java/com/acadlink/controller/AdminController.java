package com.acadlink.controller;

import com.acadlink.dto.UtilisateurDTO;
import com.acadlink.service.PublicationService;
import com.acadlink.service.UtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final UtilisateurService utilisateurService;
    private final PublicationService publicationService;

    @GetMapping("/users/pending")
    public ResponseEntity<List<UtilisateurDTO>> getPendingUsers() {
        return ResponseEntity.ok(utilisateurService.getPendingUsers());
    }

    @GetMapping("/users")
    public ResponseEntity<List<UtilisateurDTO>> getAllUsers() {
        return ResponseEntity.ok(utilisateurService.getAllUsers());
    }

    @PutMapping("/users/{id}/activate")
    public ResponseEntity<Void> activateUser(@PathVariable Long id) {
        utilisateurService.activateUser(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        utilisateurService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/publications/{id}")
    public ResponseEntity<Void> deletePublication(@PathVariable Long id) {
        // Admin peut supprimer n'importe quelle publication
        publicationService.delete(id, null);
        return ResponseEntity.ok().build();
    }
}
