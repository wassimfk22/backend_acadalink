package com.acadlink.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CommentaireDTO {
    private Long id;
    private String commentaire;
    private LocalDateTime dateCommentaire;
    private Long utilisateurId;
    private String nomUser;
    private String imageUser;
}
