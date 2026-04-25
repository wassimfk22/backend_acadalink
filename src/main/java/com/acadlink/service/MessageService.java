package com.acadlink.service;

import com.acadlink.dto.MessageDTO;
import com.acadlink.entity.*;
import com.acadlink.repository.*;

import io.micrometer.common.lang.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public MessageDTO send(Long expediteurId, Long destinataireId, String sujet, String contenu) {
        Utilisateur exp = utilisateurRepository.findById(expediteurId).orElseThrow();
        Utilisateur dest = utilisateurRepository.findById(destinataireId).orElseThrow();
        
        String finalSujet = (sujet != null) ? sujet : "";

        Message msg = Message.builder()
                .sujet(finalSujet)
                .contenu(contenu)
                .date(LocalDateTime.now())
                .expediteur(exp)
                .destinataire(dest)
                .build();

        Message saved = messageRepository.save(msg);
        MessageDTO dto = toDTO(saved);

        // Envoyer en temps réel via WebSocket
        messagingTemplate.convertAndSendToUser(
                destinataireId.toString(), "/queue/messages", dto);

        notificationService.create(destinataireId,
                "Nouveau message de " + exp.getNomComplet());

        return dto;
    }

    public List<MessageDTO> getConversation(Long userId1, Long userId2) {
        return messageRepository.findConversation(userId1, userId2)
                .stream().map(this::toDTO).toList();
    }

    public List<MessageDTO> getUnread(Long userId) {
        return messageRepository.findByDestinataireIdAndLuFalseOrderByDateDesc(userId)
                .stream().map(this::toDTO).toList();
    }

    @Transactional
    public void markAsRead(Long messageId, Long userId) {
        Message msg = messageRepository.findById(messageId).orElseThrow();
        if (msg.getDestinataire().getId().equals(userId)) {
            msg.setLu(true);
            messageRepository.save(msg);
        }
    }

    @Transactional
    public void markConversationAsRead(Long userId, Long otherUserId) {
        messageRepository.findConversation(userId, otherUserId).stream()
                .filter(m -> m.getDestinataire().getId().equals(userId) && !m.isLu())
                .forEach(m -> { m.setLu(true); messageRepository.save(m); });
    }

    private MessageDTO toDTO(Message m) {
        return MessageDTO.builder()
                .id(m.getId())
                .sujet(m.getSujet())
                .contenu(m.getContenu())
                .date(m.getDate())
                .lu(m.isLu())
                .expediteurId(m.getExpediteur().getId())
                .expediteurNom(m.getExpediteur().getNomComplet())
                .destinataireId(m.getDestinataire().getId())
                .destinataireNom(m.getDestinataire().getNomComplet())
                .build();
    }
}
