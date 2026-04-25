package com.acadlink.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "enseignants")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Enseignant extends Utilisateur {

    private String titreEnseignant;
    private String specialite;
    private String objectif;

    @OneToMany(mappedBy = "enseignant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExperienceProf> experiencesProf = new ArrayList<>();
}
