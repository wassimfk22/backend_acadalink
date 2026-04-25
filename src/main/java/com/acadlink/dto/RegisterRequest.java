package com.acadlink.dto;

import com.acadlink.enums.Role;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class RegisterRequest {
    @NotBlank ( message = "Le nom complet est obligatoire !" )
    private String nomComplet;
    @NotBlank @Email
    private String email;
    @NotBlank @Size(min = 6)
    private String password;
    private String description;
    private String titre;
    @NotNull
    private Role roleUtilisateur;
    // Champs spécifiques selon le rôle
    private String specialite;
    private String objectif;
    private String domaine;
    private boolean doctorat;
}
