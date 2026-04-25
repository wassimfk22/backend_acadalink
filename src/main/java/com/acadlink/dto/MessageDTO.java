package com.acadlink.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MessageDTO {
    private Long id;
    private String sujet;
    private String contenu;
    private LocalDateTime date;
    private boolean lu;
    private Long expediteurId;
    private String expediteurNom;
    private Long destinataireId;
    private String destinataireNom;
}
