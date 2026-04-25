package com.acadlink.dto;

import com.acadlink.enums.Role;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UtilisateurDTO {
    private String nomComplet;
    private String email;
    private String photoUrl;
    private String description;
    private String titre;
    private Role roleUtilisateur;
    private boolean actif;
}
