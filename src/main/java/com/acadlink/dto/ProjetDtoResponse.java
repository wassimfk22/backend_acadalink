package com.acadlink.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProjetDtoResponse {
    private Long id;
    private String titre;
    private String domaine;
    private String description;
    private String sujet;
    private String tuteur;
    private Integer duree;
    private String apprentissage;
    private Long auteurId;
    private String auteurNom;
    private String auteurPhotoUrl;
    private String typePublication;
}
