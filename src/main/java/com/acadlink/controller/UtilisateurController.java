package com.acadlink.controller;

import com.acadlink.dto.UtilisateurDTO;
import com.acadlink.security.CustomUserDetails;
import com.acadlink.service.UtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/me")
    public ResponseEntity<UtilisateurDTO> updateProfile(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody UtilisateurDTO dto) {
        return ResponseEntity.ok(utilisateurService.updateProfile(user.getId(), dto));
    }

    @GetMapping("/search")
    public ResponseEntity<List<UtilisateurDTO>> search(@RequestParam String q) {
        return ResponseEntity.ok(utilisateurService.search(q));
    }
}
