package com.acadlink.dto;

import lombok.*;

@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class PublicationDtoRequest {

    private String titre;
    private String domaine;
    private String description;
    // CORRECTION : ce champ était absent, causant imageUrl=null en base
    // Utilisé quand le client envoie une URL directe (optionnel si multipart)
    private String imageUrl;

}