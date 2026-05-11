package com.acadlink.controller;

import com.acadlink.dto.UtilisateurDTO;
import com.acadlink.security.CustomUserDetails;
import com.acadlink.service.UtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/utilisateurs")
@RequiredArgsConstructor
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    @GetMapping("/me")
    public ResponseEntity<UtilisateurDTO> getMe(@AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(utilisateurService.getProfile(user.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UtilisateurDTO> getProfile(@PathVariable Long id) {
        return ResponseEntity.ok(utilisateurService.getProfile(id));
    }

    // AJOUT : liste publique de tous les membres actifs
    @GetMapping
    public ResponseEntity<List<UtilisateurDTO>> getAll() {
        return ResponseEntity.ok(utilisateurService.getAllActiveUsers());
    }

    @PutMapping("/me")
    public ResponseEntity<UtilisateurDTO> updateProfile(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody UtilisateurDTO dto) {
        return ResponseEntity.ok(utilisateurService.updateProfile(user.getId(), dto));
    }

    /**
     * AJOUT : Upload de la photo de profil.
     * PUT /api/utilisateurs/me/photo
     * multipart/form-data avec une part "file"
     */
    @PutMapping(value = "/me/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UtilisateurDTO> updatePhoto(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestPart("file") MultipartFile file) {
        return ResponseEntity.ok(utilisateurService.updatePhoto(user.getId(), file));
    }

    @GetMapping("/search")
    public ResponseEntity<List<UtilisateurDTO>> search(@RequestParam String q) {
        return ResponseEntity.ok(utilisateurService.search(q));
    }
    
    
    
}