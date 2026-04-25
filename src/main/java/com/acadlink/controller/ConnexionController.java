package com.acadlink.controller;

import com.acadlink.dto.ConnexionDTO;
import com.acadlink.enums.StatutConnexion;
import com.acadlink.security.CustomUserDetails;
import com.acadlink.service.ConnexionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/connexions")
@RequiredArgsConstructor
public class ConnexionController {

    private final ConnexionService connexionService;

    @PostMapping("/{destinataireId}")
    public ResponseEntity<ConnexionDTO> send(@AuthenticationPrincipal CustomUserDetails user,
                                              @PathVariable Long destinataireId) {
        return ResponseEntity.ok(connexionService.sendRequest(user.getId(), destinataireId));
    }

    @PutMapping("/{connexionId}/accept")
    public ResponseEntity<ConnexionDTO> accept(@PathVariable Long connexionId,
                                                @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(connexionService.respond(connexionId, user.getId(), StatutConnexion.CONFIRMEE));
    }

    @PutMapping("/{connexionId}/reject")
    public ResponseEntity<ConnexionDTO> reject(@PathVariable Long connexionId,
                                                @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(connexionService.respond(connexionId, user.getId(), StatutConnexion.REFUSEE));
    }

    @GetMapping("/received")
    public ResponseEntity<List<ConnexionDTO>> getReceived(@AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(connexionService.getPendingReceived(user.getId()));
    }

    @GetMapping("/sent")
    public ResponseEntity<List<ConnexionDTO>> getSent(@AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(connexionService.getPendingSent(user.getId()));
    }

    @GetMapping
    public ResponseEntity<List<ConnexionDTO>> getConnections(@AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(connexionService.getConnections(user.getId()));
    }
}
