package com.acadlink.repository;

import com.acadlink.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("SELECT m FROM Message m WHERE (m.expediteur.id = :userId1 AND m.destinataire.id = :userId2) OR (m.expediteur.id = :userId2 AND m.destinataire.id = :userId1) ORDER BY m.date ASC")
    List<Message> findConversation(Long userId1, Long userId2);

    List<Message> findByDestinataireIdAndLuFalseOrderByDateDesc(Long destinataireId);

    @Query("SELECT DISTINCT CASE WHEN m.expediteur.id = :userId THEN m.destinataire ELSE m.expediteur END FROM Message m WHERE m.expediteur.id = :userId OR m.destinataire.id = :userId")
    List<Object> findConversationPartners(Long userId);
}
