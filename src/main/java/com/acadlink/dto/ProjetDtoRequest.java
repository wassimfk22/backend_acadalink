package com.acadlink.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProjetDtoRequest {
    private String titre;
    private String domaine;
    private String description;
    private String sujet;
    private String tuteur;
    private Integer duree;
    private String apprentissage;
    // AJOUT : id de l'enseignant collaborateur (optionnel)
    private Long enseignantCollaborateurId;
}