package com.acadlink.controller;

import com.acadlink.dto.CommentaireDTO;
import com.acadlink.security.CustomUserDetails;
import com.acadlink.service.CommentaireService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/publications/{publicationId}/commentaires")
@RequiredArgsConstructor
public class CommentaireController {

    private final CommentaireService commentaireService;

    @GetMapping
    public ResponseEntity<List<CommentaireDTO>> getAll(@PathVariable Long publicationId) {
        return ResponseEntity.ok(commentaireService.getByPublication(publicationId));
    }

    @PostMapping
    public ResponseEntity<CommentaireDTO> create(@PathVariable Long publicationId,
                                                  @AuthenticationPrincipal CustomUserDetails user,
                                                  @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(commentaireService.create(publicationId, user.getId(), body.get("commentaire")));
    }

    @DeleteMapping("/{commentaireId}")
    public ResponseEntity<Void> delete(@PathVariable Long commentaireId,
                                        @AuthenticationPrincipal CustomUserDetails user) {
        commentaireService.delete(commentaireId, user.getId());
        return ResponseEntity.ok().build();
    }
}
