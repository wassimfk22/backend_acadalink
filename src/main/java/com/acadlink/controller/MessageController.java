package com.acadlink.controller;

import com.acadlink.dto.MessageDTO;
import com.acadlink.security.CustomUserDetails;
import com.acadlink.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/{destinataireId}")
    public ResponseEntity<MessageDTO> send(@AuthenticationPrincipal CustomUserDetails user,
                                            @PathVariable Long destinataireId,
                                            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(messageService.send(user.getId(), destinataireId,
                body.get("sujet"), body.get("contenu")));
    }

    @GetMapping("/conversation/{otherUserId}")
    public ResponseEntity<List<MessageDTO>> getConversation(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long otherUserId) {
        return ResponseEntity.ok(messageService.getConversation(user.getId(), otherUserId));
    }

    @GetMapping("/unread")
    public ResponseEntity<List<MessageDTO>> getUnread(@AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(messageService.getUnread(user.getId()));
    }

    @PutMapping("/{messageId}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long messageId,
                                            @AuthenticationPrincipal CustomUserDetails user) {
        messageService.markAsRead(messageId, user.getId());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/conversation/{otherUserId}/read")
    public ResponseEntity<Void> markConversationAsRead(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long otherUserId) {
        messageService.markConversationAsRead(user.getId(), otherUserId);
        return ResponseEntity.ok().build();
    }

    // WebSocket endpoint pour messagerie temps réel
    @MessageMapping("/chat.send")
    public void handleWebSocketMessage(@Payload Map<String, Object> payload) {
        Long expediteurId = Long.valueOf(payload.get("expediteurId").toString());
        Long destinataireId = Long.valueOf(payload.get("destinataireId").toString());
        String sujet = (String) payload.getOrDefault("sujet", "");
        String contenu = (String) payload.get("contenu");
        messageService.send(expediteurId, destinataireId, sujet, contenu);
    }
}
