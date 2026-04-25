package com.acadlink.controller;

import com.acadlink.dto.PublicationDtoRequest;
import com.acadlink.dto.PublicationDtoResponse;
import com.acadlink.dto.SimpleUserDTO;
import com.acadlink.security.CustomUserDetails;
import com.acadlink.service.PublicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/publications")
@RequiredArgsConstructor
public class PublicationController {

    private final PublicationService publicationService;

    @GetMapping
    public ResponseEntity<List<PublicationDtoResponse>> getFeed(@AuthenticationPrincipal CustomUserDetails user) {
        Long userId = user != null ? user.getId() : null;
        return ResponseEntity.ok(publicationService.getFeed(userId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PublicationDtoResponse>> getByUser(@PathVariable Long userId,
                                                          @AuthenticationPrincipal CustomUserDetails user) {
        Long currentUserId = user != null ? user.getId() : null;
        return ResponseEntity.ok(publicationService.getByUser(userId, currentUserId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublicationDtoResponse> getById(@PathVariable Long id,
                                                   @AuthenticationPrincipal CustomUserDetails user) {
        Long currentUserId = user != null ? user.getId() : null;
        return ResponseEntity.ok(publicationService.getById(id, currentUserId));
    }

    @PostMapping
    public ResponseEntity<PublicationDtoResponse> create(@AuthenticationPrincipal CustomUserDetails user,
                                                  @RequestBody PublicationDtoRequest dto) {
        return ResponseEntity.ok(publicationService.create(user.getId(), dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id,
                                        @AuthenticationPrincipal CustomUserDetails user) {
        publicationService.delete(id, user.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<Void> toggleLike(@PathVariable Long id,
                                            @AuthenticationPrincipal CustomUserDetails user) {
        publicationService.toggleLike(id, user.getId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<PublicationDtoResponse>> search(@RequestParam String q,
                                                       @AuthenticationPrincipal CustomUserDetails user) {
        Long currentUserId = user != null ? user.getId() : null;
        return ResponseEntity.ok(publicationService.search(q, currentUserId));
    }

    @GetMapping("/{id}/likers")
    public ResponseEntity<List<SimpleUserDTO>> getLikers(@PathVariable Long id) {
        return ResponseEntity.ok(publicationService.getLikers(id));
    }
}
