package com.acadlink.dto;

import lombok.*;
import java.time.LocalDateTime;

@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class PublicationDtoResponse {
    private Long id;
    private String titre;
    private String domaine;
    private String description;
    private String imageUrl;
    private LocalDateTime date;
    private Long auteurId;
    private String auteurNom;
    private String auteurPhotoUrl;
    private long likesCount;
    private long commentairesCount;
    private boolean likedByCurrentUser;
}
