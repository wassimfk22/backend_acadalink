package com.acadlink.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RechercheDtoResponse {
    private Long id;
    private String titre;
    private String domaine;
    private String description;
    private String probleme;
    private String conclusion;
    private String source;
    private String ecole;
    private Integer duree;
    private Long auteurId;
    private String auteurNom;
    private String auteurPhotoUrl;
    private String typePublication;
}
