package com.acadlink.dto;

import com.acadlink.enums.StatutConnexion;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ConnexionDTO {
    private Long id;
    private StatutConnexion statut;
    private Long expediteurId;
    private String expediteurNom;
    private String expediteurPhotoUrl;
    private Long destinataireId;
    private String destinataireNom;
}
