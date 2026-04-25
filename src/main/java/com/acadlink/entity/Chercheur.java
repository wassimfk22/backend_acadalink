package com.acadlink.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chercheurs")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Chercheur extends Utilisateur {

    private String titreChercheur;
    private String domaine;
    private String objectif;
    private boolean doctorat;

    @OneToMany(mappedBy = "chercheur", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Recherche> recherches = new ArrayList<>();
}
