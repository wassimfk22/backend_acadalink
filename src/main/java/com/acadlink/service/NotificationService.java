package com.acadlink.service;

import com.acadlink.dto.NotificationDTO;
import com.acadlink.entity.*;
import com.acadlink.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public void create(Long destinataireId, String message) {
        Utilisateur dest = utilisateurRepository.findById(destinataireId).orElseThrow();
        Notification n = Notification.builder()
                .message(message)
                .date(LocalDateTime.now())
                .destinataire(dest)
                .build();
        Notification saved = notificationRepository.save(n);

        // Temps réel
        messagingTemplate.convertAndSendToUser(
                destinataireId.toString(), "/queue/notifications", toDTO(saved));
    }

    public List<NotificationDTO> getAll(Long userId) {
        return notificationRepository.findByDestinataireIdOrderByDateDesc(userId)
                .stream().map(this::toDTO).toList();
    }

    public long getUnreadCount(Long userId) {
        return notificationRepository.countByDestinataireIdAndLuFalse(userId);
    }

    @Transactional
    public void markAsRead(Long notificationId) {
        Notification n = notificationRepository.findById(notificationId).orElseThrow();
        n.setLu(true);
        notificationRepository.save(n);
    }

    @Transactional
    public void markAllAsRead(Long userId) {
        notificationRepository.findByDestinataireIdAndLuFalse(userId)
                .forEach(n -> { n.setLu(true); notificationRepository.save(n); });
    }

    private NotificationDTO toDTO(Notification n) {
        return NotificationDTO.builder()
                .id(n.getId())
                .message(n.getMessage())
                .date(n.getDate())
                .lu(n.isLu())
                .build();
    }
}
